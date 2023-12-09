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
 * Copyright (c) OWASP Foundation. All Rights Reserved.
 */
package org.cyclonedx.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.AttachmentText;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public final class LicenseResolver {

    private static LicenseList licenses;

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
        return resolve(licenseString,  new LicenseTextSettings(true, LicenseEncoding.BASE64));
    }

    /**
     * Attempts to resolve the specified license string via SPDX license identifier and expression
     * parsing first. If SPDX resolution is not successful, the method will attempt fuzzy matching.
     * @param licenseString the license string to resolve
     * @param includeLicenseText specifies is the resolved license will include the entire text of the license
     * @return a LicenseChoice object if resolution was successful, or null if unresolved
     */
    public static LicenseChoice resolve(final String licenseString, final boolean includeLicenseText) {
        return resolve(licenseString, new LicenseTextSettings( includeLicenseText, LicenseEncoding.BASE64));
    }

    static LicenseChoice resolve(final String licenseString, final boolean includeLicenseText, final ObjectMapper mapper) {
        return resolve(licenseString, new LicenseTextSettings( includeLicenseText, LicenseEncoding.BASE64), mapper);
    }

    /**
     * Attempts to resolve the specified license string via SPDX license identifier and expression
     * parsing first. If SPDX resolution is not successful, the method will attempt fuzzy matching.
     * @param licenseString the license string to resolve
     * @param licenseTextSettings specifies settings regarding the entire text of the resolved license
     * @return a LicenseChoice object if resolution was successful, or null if unresolved
     */
    public static LicenseChoice resolve(final String licenseString, final LicenseTextSettings licenseTextSettings) {
        final ObjectMapper mapper = new ObjectMapper();

        return resolve(licenseString, licenseTextSettings, mapper);
    }

    static LicenseChoice resolve(final String licenseString, final LicenseTextSettings licenseTextSettings, final ObjectMapper mapper) {
        try {
            LicenseChoice licenseChoice = resolveLicenseString(licenseString, licenseTextSettings, mapper);

            if (licenseChoice == null) {
                licenseChoice = resolveFuzzyMatching(licenseString, licenseTextSettings, mapper);
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
     * @param licenseTextSettings specifies settings regarding the entire text of the resolved license
     * @param mapper is to provide a Jackson ObjectMapper
     * @return a LicenseChoice object if resolved, or null
     * @throws IOException an exception while parsing the license string
     */
    private static LicenseChoice resolveLicenseString(String licenseString, LicenseTextSettings licenseTextSettings, final ObjectMapper mapper)
        throws IOException
    {
        if (licenses == null) {
          final InputStream is = LicenseResolver.class.getResourceAsStream("/licenses/licenses.json");

          licenses = mapper.readValue(is, LicenseList.class);        
        }

        if (licenses != null && licenses.licenses != null && !licenses.licenses.isEmpty()) {
            for (LicenseDetail licenseDetail : licenses.licenses) {

                final String primaryLicenseUrl = (licenseDetail.seeAlso != null && !licenseDetail.seeAlso.isEmpty()) ? licenseDetail.seeAlso.get(0) : null;

                if (licenseString.trim().equalsIgnoreCase(licenseDetail.licenseId)) {
                    return createLicenseChoice(licenseDetail.licenseId, primaryLicenseUrl, licenseDetail.isDeprecatedLicenseId, licenseTextSettings);
                } else if (licenseString.trim().equalsIgnoreCase(licenseDetail.name)) {
                    return createLicenseChoice(licenseDetail.licenseId, primaryLicenseUrl, licenseDetail.isDeprecatedLicenseId, licenseTextSettings);
                } else {

                    if (licenseDetail.isDeprecatedLicenseId) {
                        continue;
                    }

                    if (licenseDetail.seeAlso != null) {
                        for (String url : licenseDetail.seeAlso) {
                            if (url != null) {
                                final String licenseStringModified = urlNormalize(licenseString);

                                if (licenseStringModified.equalsIgnoreCase(urlNormalize(url))) {
                                    return createLicenseChoice(licenseDetail.licenseId, url, licenseDetail.isDeprecatedLicenseId, licenseTextSettings);
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
     * @param licenseTextSettings specifies settings regarding the entire text of the resolved license
     * @param mapper is to provide a Jackson ObjectMapper
     * @return a LicenseChoice object if resolved, otherwise null
     */
    private static LicenseChoice resolveFuzzyMatching(final String licenseString, final LicenseTextSettings licenseTextSettings, final ObjectMapper mapper) throws IOException {
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
                                return createLicenseChoice(licenseMapping.exp, null, false, licenseTextSettings);
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

    private static LicenseChoice createLicenseChoice(String licenseId, String primaryLicenseUrl, boolean isDeprecatedLicenseId, LicenseTextSettings licenseTextSettings ) throws IOException {
        final LicenseChoice choice = new LicenseChoice();
        final License license = new License();
        license.setId(licenseId);
        license.setUrl(primaryLicenseUrl);
        if (!isDeprecatedLicenseId && licenseTextSettings.isTextIncluded()) {
            final InputStream is = LicenseResolver.class.getResourceAsStream("/licenses/" + licenseId + ".txt");
            if (is != null) {
                final String text = IOUtils.toString(is, StandardCharsets.UTF_8);
                final AttachmentText attachment = new AttachmentText();
                attachment.setContentType("plain/text");
                switch(licenseTextSettings.getEncoding()){
                    case NONE:
                        attachment.setEncoding(null);
                        attachment.setText(text);
                        break;
                    case BASE64:
                        attachment.setEncoding(licenseTextSettings.getEncoding().toString());
                        attachment.setText(Base64.getEncoder().encodeToString(text.getBytes(Charset.defaultCharset())));
                        break;
                    default:
                        throw new IllegalArgumentException("Unhandled License Encoding:" + licenseTextSettings.getEncoding().toString() );
                }
                license.setLicenseText(attachment);
            }
        }
        choice.addLicense(license);
        return choice;
    }

    /**
     * Lists possible choices for license text encoding
     */
    public enum LicenseEncoding{
        BASE64("base64"),
        NONE("none");

        private final String encodingName;

        /**
         * Constructor with a string representation of the enum value
         * @param encodingName The string representation of the enum value
         */
        LicenseEncoding(String encodingName) {
            this.encodingName = encodingName;
        }
        public String toString() {
            return encodingName;
        }
    }

    /**
     * Data class aggregating settings for license text output
     */
    public static class LicenseTextSettings {
        public boolean isTextIncluded;
        public LicenseEncoding encoding;

        public LicenseTextSettings(boolean includeLicenseText, LicenseEncoding encoding) {
            this.isTextIncluded = includeLicenseText;
            this.encoding = encoding;
        }
        public boolean isTextIncluded() {
            return isTextIncluded;
        }
        public void setTextIncluded(boolean include) {
            this.isTextIncluded = include;
        }
        public LicenseEncoding getEncoding() {
            return encoding;
        }
        public void setEncoding(LicenseEncoding encoding) {
            this.encoding = encoding;
        }
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
