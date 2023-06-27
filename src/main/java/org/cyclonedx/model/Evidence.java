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
package org.cyclonedx.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.util.deserializer.LicenseDeserializer;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"licenses", "copyright"})
public class Evidence extends ExtensibleElement {

    private LicenseChoice license;
    private List<Copyright> copyright;

    @JacksonXmlProperty(localName = "licenses")
    @JsonProperty("licenses")
    @JsonDeserialize(using = LicenseDeserializer.class)
    public LicenseChoice getLicenseChoice() {
        return license;
    }

    public void setLicenseChoice(LicenseChoice licenseChoice) {
        this.license = licenseChoice;
    }

    @JacksonXmlElementWrapper(useWrapping = false)
    public List<Copyright> getCopyright() {
        return copyright;
    }

    public void setCopyright(List<Copyright> copyright) {
        this.copyright = copyright;
    }

    public void addCopyright(Copyright copyright) {
        if (this.copyright == null) {
            this.copyright = new ArrayList<>();
        }
        this.copyright.add(copyright);
    }
}
