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
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.cyclonedx.util;

import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.LicenseText;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.license.AnyLicenseInfo;
import org.spdx.rdfparser.license.LicenseInfoFactory;
import org.spdx.rdfparser.license.LicenseSet;
import org.spdx.rdfparser.license.ListedLicenses;
import org.spdx.rdfparser.license.OrLaterOperator;
import org.spdx.rdfparser.license.SpdxListedLicense;
import org.spdx.spdxspreadsheet.InvalidLicenseStringException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class LicenseResolver {

    private static final Map<String, License> resolvedByUrl = new ConcurrentHashMap<>();

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
            final AnyLicenseInfo licenseInfo = LicenseInfoFactory.parseSPDXLicenseString(licenseString);
            if (licenseInfo instanceof SpdxListedLicense) {
                final SpdxListedLicense spdxListedLicense = (SpdxListedLicense)licenseInfo;
                final LicenseChoice choice = new LicenseChoice();
                choice.addLicense(createLicenseObject(spdxListedLicense));
                return choice;
            } else if (licenseInfo instanceof OrLaterOperator) {
                final OrLaterOperator orLaterOperator = (OrLaterOperator)licenseInfo;
                final SpdxListedLicense spdxListedLicense = (SpdxListedLicense)orLaterOperator.getLicense();
                final LicenseChoice choice = new LicenseChoice();
                choice.addLicense(createLicenseObject(spdxListedLicense));
                return choice;
            } else if (licenseInfo instanceof LicenseSet) {
                final LicenseChoice choice = new LicenseChoice();
                choice.setExpression(licenseString);
                return choice;
            }
        } catch (InvalidLicenseStringException e) {
            // Parsing licenseString as SPDX has failed. Attempt some manual fuzzy matching
            return fuzzyMatchLastResort(licenseString);
        }
        return null;
    }

    /**
     * Given a valid SPDX license ID, this method will return a LicenseChoice object.
     * @param licenseId a valid SPDX license ID
     * @return a LicenseChoice object
     * @throws InvalidSPDXAnalysisException
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
     * @throws InvalidLicenseStringException
     */
    public static License parseLicenseByUrl(String licenseUrl) throws InvalidLicenseStringException {
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
                    // Attempt to account for .txt, .html, and other extensions in the URLs being compared
                    if (licenseUrl.toLowerCase().contains(seeAlso.toLowerCase())
                            || seeAlso.toLowerCase().contains(licenseUrl.toLowerCase())) {
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
        License license = new License();
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
    private static LicenseChoice fuzzyMatchLastResort(String licenseString) {
        final String upLc = licenseString.toUpperCase();
        try {
            if (upLc.contains("APACHE") && upLc.contains("2")) {
                return resolveSpdxLicenseId("Apache-2.0");
            } else if (upLc.contains("APACHE") && upLc.contains("1.1")) {
                return resolveSpdxLicenseId("Apache-1.1");
            } else if (upLc.contains("APACHE") && upLc.contains("1")) {
                return resolveSpdxLicenseId("Apache-1.0");
            } else if (upLc.contains("CDDL") && upLc.contains("1.0")) {
                return resolveSpdxLicenseId("CDDL-1.0");
            } else if (upLc.contains("CDDL") && upLc.contains("1.1")) {
                return resolveSpdxLicenseId("CDDL-1.1");
            } else if (upLc.contains("COMMON DEVELOPMENT AND DISTRIBUTION LICENSE") && upLc.contains("1.0")) {
                return resolveSpdxLicenseId("CDDL-1.0");
            } else if (upLc.contains("COMMON DEVELOPMENT AND DISTRIBUTION LICENSE") && upLc.contains("1.1")) {
                return resolveSpdxLicenseId("CDDL-1.1");
            }
            //TODO build this up
        } catch (InvalidSPDXAnalysisException e) {
            // throw it away
        }
        return null;
    }

}
