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
import org.cyclonedx.model.AttachmentText;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LicenseResolver {

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
     * @param includeLicenseText specifies is the resolved license will include the entire text of the license
     * @return a LicenseChoice object if resolution was successful, or null if unresolved
     */
    public static LicenseChoice resolve(String licenseString, boolean includeLicenseText) {
        try {
            LicenseChoice licenseChoice = resolveSpdxLicenseString(licenseString, includeLicenseText);
            if (licenseChoice == null) {
                licenseChoice = resolveViaAlternativeMapping(licenseString, includeLicenseText);
            }
            return licenseChoice;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Attempts to resolve the specified license string via SPDX license identifier and expression
     * parsing first. If SPDX resolution is not successful, the method will attempt fuzzy matching.
     * @param licenseString the license string to resolve
     * @return a LicenseChoice object if resolution was successful, or null if unresolved
     */
    public static LicenseChoice resolve(String licenseString) {
        return resolve(licenseString, true);
    }

    /**
     * Given an SPDX license ID or expression, this method will resolve the license(s) and
     * return a LicenseChoice object.
     * @param licenseString the license string to resolve
     * @param includeLicenseText specifies is the resolved license will include the entire text of the license
     * @return a LicenseChoice object if resolved, or null
     * @throws IOException an exception while parsing the license string
     */
    private static LicenseChoice resolveSpdxLicenseString(String licenseString, boolean includeLicenseText) throws IOException {
        final InputStream is = LicenseResolver.class.getResourceAsStream("/licenses/licenses.json");
        final String licenseStringModified = urlNormalize(licenseString);
        final String jsonTxt = IOUtils.toString(is, "UTF-8");
        final JSONObject json = new JSONObject(jsonTxt);
        final JSONArray licenses = json.getJSONArray("licenses");
        for (int i = 0; i < licenses.length(); i++) {
            final JSONObject license = licenses.getJSONObject(i);
            final boolean isDeprecatedLicenseId = license.getBoolean("isDeprecatedLicenseId");
            final String licenseId = license.getString("licenseId");
            final String name = license.getString("name");
            final JSONArray seeAlso = license.optJSONArray("seeAlso");
            final String primaryLicenseUrl = (seeAlso != null && seeAlso.length() > 0) ? seeAlso.getString(0) : null;
            if (licenseString.trim().equalsIgnoreCase(licenseId)) {
                return createLicenseChoice(licenseId, primaryLicenseUrl, isDeprecatedLicenseId, includeLicenseText);
            } else if (licenseString.trim().equalsIgnoreCase(name)) {
                return createLicenseChoice(licenseId, primaryLicenseUrl, isDeprecatedLicenseId, includeLicenseText);
            } else {
                if (isDeprecatedLicenseId) {
                    continue;
                }
                for (int s = 0; s < seeAlso.length(); s++) {
                    final String url = seeAlso.getString(s);
                    if (url != null) {
                        if (licenseStringModified.equalsIgnoreCase(urlNormalize(url))) {
                            return createLicenseChoice(licenseId, url, isDeprecatedLicenseId, includeLicenseText);
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String urlNormalize(String input) {
        return input.trim()
                .replace("https://www.", "")
                .replace("http://www.", "")
                .replace("https://", "")
                .replace("http://", "");
    }

    private static LicenseChoice createLicenseChoice(String licenseId, String primaryLicenseUrl, boolean isDeprecatedLicenseId, boolean includeLicenseText) throws IOException {
        final LicenseChoice choice = new LicenseChoice();
        final License license = new License();
        license.setId(licenseId);
        license.setUrl(primaryLicenseUrl);
        if (!isDeprecatedLicenseId && includeLicenseText) {
            final InputStream is = LicenseResolver.class.getResourceAsStream("/licenses/" + licenseId + ".txt");
            if (is != null) {
                final String text = IOUtils.toString(is, "UTF-8");
                final AttachmentText attachment = new AttachmentText();
                attachment.setContentType("plain/text");
                attachment.setEncoding("base64");
                attachment.setText(Base64.getEncoder().encodeToString(text.getBytes()));
                license.setLicenseText(attachment);
            }
        }
        choice.addLicense(license);
        return choice;
    }

    /**
     * Attempts to perform high-confidence license resolution with unstructured text as input.
     * @param licenseString the license string (not the actual license text)
     * @param includeLicenseText specifies is the resolved license will include the entire text of the license
     * @return a LicenseChoice object if resolved, otherwise null
     */
    private static LicenseChoice resolveViaAlternativeMapping(String licenseString, boolean includeLicenseText) throws IOException {
        if (licenseString == null) {
            return null;
        }
        for (final Map.Entry<String, List<String>> mapping : mappings.entrySet()) {
            final List<String> names = mapping.getValue();
            if (names != null) {
                for (final String name: names) {
                    if (licenseString.equalsIgnoreCase(name)) {
                        return createLicenseChoice(mapping.getKey(), null, false, includeLicenseText);
                    }
                }
            }
        }
        return null;
    }
}
