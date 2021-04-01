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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.AttachmentText;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

public final class LicenseResolver {

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
    public static LicenseChoice resolve(final String licenseString) {
        return resolve(licenseString, true);
    }

    /**
     * Attempts to resolve the specified license string via SPDX license identifier and expression
     * parsing first. If SPDX resolution is not successful, the method will attempt fuzzy matching.
     * @param licenseString the license string to resolve
     * @param includeLicenseText specifies is the resolved license will include the entire text of the license
     * @return a LicenseChoice object if resolution was successful, or null if unresolved
     */
    public static LicenseChoice resolve(final String licenseString, final boolean includeLicenseText) {
        final ObjectMapper mapper = new ObjectMapper();

        return resolve(licenseString, includeLicenseText, mapper);
    }

    static LicenseChoice resolve(final String licenseString, final boolean includeLicenseText, final ObjectMapper mapper) {
        try {
            LicenseChoice licenseChoice = resolveLicenseString(licenseString, includeLicenseText, mapper);

            if (licenseChoice == null) {
                licenseChoice = resolveFuzzyMatching(licenseString, includeLicenseText, mapper);
            }
            return licenseChoice;
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Given an SPDX license ID or expression, this method will resolve the license(s) and
     * return a LicenseChoice object.
     * @param licenseString the license string to resolve
     * @param includeLicenseText specifies is the resolved license will include the entire text of the license
     * @param mapper is to provide a Jackson ObjectMapper
     * @return a LicenseChoice object if resolved, or null
     * @throws IOException an exception while parsing the license string
     */
    private static LicenseChoice resolveLicenseString(String licenseString, boolean includeLicenseText, final ObjectMapper mapper)
        throws IOException
    {
        final InputStream is = LicenseResolver.class.getResourceAsStream("/licenses/licenses.json");

        final LicenseList licenses = mapper.readValue(is, LicenseList.class);

        if (licenses != null && licenses.licenses != null && !licenses.licenses.isEmpty()) {
            for (LicenseDetail licenseDetail : licenses.licenses) {

                final String primaryLicenseUrl = (licenseDetail.seeAlso != null && !licenseDetail.seeAlso.isEmpty()) ? licenseDetail.seeAlso.get(0) : null;

                if (licenseString.trim().equalsIgnoreCase(licenseDetail.licenseId)) {
                    return createLicenseChoice(licenseDetail.licenseId, primaryLicenseUrl, licenseDetail.isDeprecatedLicenseId, includeLicenseText);
                } else if (licenseString.trim().equalsIgnoreCase(licenseDetail.name)) {
                    return createLicenseChoice(licenseDetail.licenseId, primaryLicenseUrl, licenseDetail.isDeprecatedLicenseId, includeLicenseText);
                } else {

                    if (licenseDetail.isDeprecatedLicenseId) {
                        continue;
                    }

                    if (licenseDetail.seeAlso != null) {
                        for (String url : licenseDetail.seeAlso) {
                            if (url != null) {
                                final String licenseStringModified = urlNormalize(licenseString);

                                if (licenseStringModified.equalsIgnoreCase(urlNormalize(url))) {
                                    return createLicenseChoice(licenseDetail.licenseId, url, licenseDetail.isDeprecatedLicenseId, includeLicenseText);
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Attempts to perform high-confidence license resolution with unstructured text as input.
     * @param licenseString the license string (not the actual license text)
     * @param includeLicenseText specifies is the resolved license will include the entire text of the license
     * @param mapper is to provide a Jackson ObjectMapper
     * @return a LicenseChoice object if resolved, otherwise null
     */
    private static LicenseChoice resolveFuzzyMatching(final String licenseString, final boolean includeLicenseText, final ObjectMapper mapper) throws IOException {
        if (licenseString == null) {
            return null;
        }

        final InputStream is = LicenseResolver.class.getResourceAsStream("/license-mapping.json");

        final SpdxLicenseMapping[] mappings = mapper.readValue(is, SpdxLicenseMapping[].class);

        if (mappings != null) {
            for(final SpdxLicenseMapping licenseMapping : mappings) {
                if (licenseMapping.names != null && !licenseMapping.names.isEmpty()) {
                    for (final String name : licenseMapping.names) {
                        if (licenseString.equalsIgnoreCase(name)) {
                            if (licenseMapping.exp.startsWith("(") && licenseMapping.exp.endsWith(")")) {
                                final LicenseChoice lc = new LicenseChoice();
                                lc.setExpression(licenseMapping.exp);
                                return lc;
                            } else {
                                return createLicenseChoice(licenseMapping.exp, null, false, includeLicenseText);
                            }
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

    private static class LicenseDetail {
        public String reference;
        public boolean isDeprecatedLicenseId;
        public String detailsUrl;
        public String referenceNumber;
        public String name;
        public String licenseId;
        public List<String> seeAlso;
        public boolean isOsiApproved;
        public boolean isFsfLibre;
    }

    private static class LicenseList {
        public String licenseListVersion;
        public List<LicenseDetail> licenses;
        public String releaseDate;
    }

    private static class SpdxLicenseMapping{
        public String exp;
        public List<String> names;
    }
}
