/*
 * This file is part of CycloneDX Core (Java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.cyclonedx.util;

import org.apache.commons.io.IOUtils;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.LicenseText;
import org.json.JSONArray;
import org.json.JSONObject;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.license.AnyLicenseInfo;
import org.spdx.rdfparser.license.LicenseInfoFactory;
import org.spdx.rdfparser.license.LicenseSet;
import org.spdx.rdfparser.license.ListedLicenses;
import org.spdx.rdfparser.license.OrLaterOperator;
import org.spdx.rdfparser.license.SpdxListedLicense;
import org.spdx.spdxspreadsheet.InvalidLicenseStringException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class LicenseResolver {

    private static final Map<String, License> resolvedByUrl = new ConcurrentHashMap<>();

    private static Map<String, List<String>> mappings = new HashMap<>();
    static {
        final InputStream is = LicenseResolver.class.getResourceAsStream("/license-mapping.json");
        try {
            final String jsonTxt = IOUtils.toString(is, "UTF-8");
            final JSONArray json = new JSONArray(jsonTxt);
            for (int i = 0; i < json.length(); i++) {
                final JSONObject mapping = json.getJSONObject(i);
                if (!mappings.containsKey(mapping.getString("exp"))) {
                    mappings.put(mapping.getString("exp"), new ArrayList<>());
                }
                final List<String> names = mappings.get(mapping.getString("exp"));
                for (int n = 0; n < mapping.getJSONArray("names").length(); n++) {
                    names.add(mapping.getJSONArray("names").getString(n));
                }
                mappings.replace(mapping.getString("exp"), names);
            }
        } catch (IOException e) {
            // This should be logged
        }
    }

    /**
     * Private constructor.
     */
    private LicenseResolver() {
    }

    /**
     * Attempts to resolve the specified license string via SPDX license identifier and expression
     * parsing first. If SPDX resolution is not successful, the method will attempt fuzzy matching.
     * @param licenseString the license string to resolve
     * @return a LicenseChoice object if resolution was successful, or null if unresolved
     */
    public static LicenseChoice resolve(String licenseString) {
        try {
            return resolveSpdxLicenseString(licenseString);
        } catch (InvalidLicenseStringException e1) {
            final LicenseChoice licenseChoice = resolveViaAlternativeMapping(licenseString);
            if (licenseChoice != null) {
                return licenseChoice;
            }
            try {
                new URL(licenseString);
                // We want to throw an exception if its not a valid URL, as the remaining block may impact performance
                final LicenseChoice choice = new LicenseChoice();
                choice.addLicense(parseLicenseByUrl(licenseString));
                return choice;
            } catch (MalformedURLException | InvalidLicenseStringException e2) {
                // throw it away
            }
        }
        return null;
    }

    /**
     * Given an SPDX license ID or expression, this method will resolve the license(s) and
     * return a LicenseChoice object.
     * @param licenseString the license string to resolve
     * @return a LicenseChoice object if resolved, or null
     * @throws InvalidLicenseStringException an exception while parsing the license string
     */
    public static LicenseChoice resolveSpdxLicenseString(String licenseString) throws InvalidLicenseStringException {
        final LicenseChoice choice;
        final AnyLicenseInfo licenseInfo = LicenseInfoFactory.parseSPDXLicenseString(licenseString);
        if (licenseInfo instanceof SpdxListedLicense) {
            final SpdxListedLicense spdxListedLicense = (SpdxListedLicense)licenseInfo;
            choice = new LicenseChoice();
            choice.addLicense(createLicenseObject(spdxListedLicense));
            return choice;
        } else if (licenseInfo instanceof OrLaterOperator) {
            final OrLaterOperator orLaterOperator = (OrLaterOperator)licenseInfo;
            final SpdxListedLicense spdxListedLicense = (SpdxListedLicense)orLaterOperator.getLicense();
            choice = new LicenseChoice();
            choice.addLicense(createLicenseObject(spdxListedLicense));
            return choice;
        } else if (licenseInfo instanceof LicenseSet) {
            choice = new LicenseChoice();
            choice.setExpression(licenseString);
            return choice;
        }
        return null;
    }

    /**
     * Given a valid SPDX license ID, this method will return a LicenseChoice object.
     * @param licenseId a valid SPDX license ID
     * @return a LicenseChoice object
     * @throws InvalidSPDXAnalysisException an exception while parsing the license ID
     */
    public static LicenseChoice resolveSpdxLicenseId(String licenseId) throws InvalidSPDXAnalysisException {
        final SpdxListedLicense spdxLicense = LicenseInfoFactory.getListedLicenseById(licenseId);
        final LicenseChoice choice = new LicenseChoice();
        choice.addLicense(createLicenseObject(spdxLicense));
        return choice;
    }

    /**
     * Given a URL, this method will attempt to resolve the SPDX license. This method will
     * not retrieve the URL, rather, it will interrogate it's internal list of SPDX licenses
     * and the URLs defined for each. This method may impact performance for URLs that are
     * not associated with an SPDX license or otherwise have not been queried on previously.
     * This method will cache resolved licenses and their URLs for faster access on subsequent
     * calls.
     * @param licenseUrl the URL to the license
     * @return a License object
     * @throws InvalidLicenseStringException an exception while parsing the license URL
     */
    public static License parseLicenseByUrl(String licenseUrl) throws InvalidLicenseStringException {
        final String protocolExcludedUrl = licenseUrl.replace("http://", "").replace("https://", "");
        final ListedLicenses ll = ListedLicenses.getListedLicenses();
        // Check cache. If hit, return, otherwise go through the native SPDX model which is terribly slow
        License license = resolvedByUrl.get(licenseUrl);
        if (license != null) {
            return license;
        }
        for (final String licenseId: ll.getSpdxListedLicenseIds()) {
            final AnyLicenseInfo licenseInfo = LicenseInfoFactory.parseSPDXLicenseString(licenseId);
            if (licenseInfo instanceof SpdxListedLicense) {
                final SpdxListedLicense spdxListedLicense = (SpdxListedLicense) licenseInfo;
                for (final String seeAlso: spdxListedLicense.getSeeAlso()) {
                    final String protocolExcludedSeeAlsoUrl = seeAlso.replace("http://", "").replace("https://", "");
                    // Attempt to account for .txt, .html, and other extensions in the URLs being compared
                    if (protocolExcludedUrl.toLowerCase().contains(protocolExcludedSeeAlsoUrl.toLowerCase())
                            || protocolExcludedSeeAlsoUrl.toLowerCase().contains(protocolExcludedUrl.toLowerCase())) {
                        license = createLicenseObject(spdxListedLicense);
                        // Lazily cache the mapping for future performance improvements
                        resolvedByUrl.put(licenseUrl, license);
                        return license;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Creates a License object from the specified SpdxListedLicense object.
     * @param spdxListedLicense the object to convert
     * @return a CycloneDX License object
     */
    private static License createLicenseObject(SpdxListedLicense spdxListedLicense) {
        final License license = new License();
        license.setId(spdxListedLicense.getLicenseId());
        license.setName(spdxListedLicense.getName());
        if (spdxListedLicense.getSeeAlso() != null && spdxListedLicense.getSeeAlso().length > 0) {
            license.setUrl(spdxListedLicense.getSeeAlso()[0]);
        }
        if (spdxListedLicense.getLicenseText() != null) {
            final LicenseText text = new LicenseText();
            text.setContentType("plain/text");
            text.setEncoding("base64");
            text.setText(Base64.getEncoder().encodeToString(spdxListedLicense.getLicenseText().getBytes()));
            license.setLicenseText(text);
        }
        return license;
    }

    /**
     * Attempts to perform high-confidence license resolution with unstructured text as input.
     * @param licenseString the license string (not the actual license text)
     * @return a LicenseChoice object if resolved, otherwise null
     */
    private static LicenseChoice resolveViaAlternativeMapping(String licenseString) {
        if (licenseString == null) {
            return null;
        }
        try {
            for (final Map.Entry<String, List<String>> mapping : mappings.entrySet()) {
                final List<String> names = mapping.getValue();
                if (names != null) {
                    for (final String name: names) {
                        if (licenseString.equalsIgnoreCase(name)) {
                            return resolveSpdxLicenseString(mapping.getKey());
                        }
                    }
                }
            }
        } catch (InvalidLicenseStringException e) {
            // throw it away
        }
        return null;
    }
}
