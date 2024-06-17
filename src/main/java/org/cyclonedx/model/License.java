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

import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.Version;
import org.cyclonedx.util.deserializer.PropertiesDeserializer;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
@JsonPropertyOrder({"bom-ref", "id", "name", "acknowledgement", "licensing", "text", "url", "properties"})
@JsonRootName("license")
public class License extends ExtensibleElement {

    @VersionFilter(Version.VERSION_15)
    @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
    @JsonProperty("bom-ref")
    private String bomRef;
    @JacksonXmlProperty(localName = "id")
    @JsonProperty("id")
    private String id;
    private String name;

    @JacksonXmlProperty(isAttribute = true, localName = "acknowledgement")
    @JsonProperty("acknowledgement")
    @VersionFilter(Version.VERSION_16)
    private String acknowledgement;

    @VersionFilter(Version.VERSION_15)
    private Licensing licensing;

    @JacksonXmlProperty(localName = "text")
    @JsonProperty("text")
    @VersionFilter(Version.VERSION_11)
    private AttachmentText attachmentText;

    @VersionFilter(Version.VERSION_11)
    private String url;

    @VersionFilter(Version.VERSION_15)
    private List<Property> properties;

    public String getBomRef() {
        return bomRef;
    }

    public void setBomRef(final String bomRef) {
        this.bomRef = bomRef;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Licensing getLicensing() {
        return licensing;
    }

    public void setLicensing(final Licensing licensing) {
        this.licensing = licensing;
    }

    @VersionFilter(Version.VERSION_11)
    public String getUrl() {
        return url;
    }

    @VersionFilter(Version.VERSION_11)
    public void setUrl(String url) {
        this.url = url;
    }

    @JacksonXmlElementWrapper(localName = "properties")
    @JacksonXmlProperty(localName = "property")
    @JsonDeserialize(using = PropertiesDeserializer.class)
    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(final List<Property> properties) {
        this.properties = properties;
    }

    public AttachmentText getAttachmentText() {
        return attachmentText;
    }

    public void setLicenseText(AttachmentText attachmentText) {
        this.attachmentText = attachmentText;
    }

    public String getAcknowledgement() {
        return acknowledgement;
    }

    public void setAcknowledgement(final String acknowledgement) {
        this.acknowledgement = acknowledgement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        License license = (License) o;
        return Objects.equals(id, license.id) &&
                Objects.equals(name, license.name) &&
                Objects.equals(url, license.url) &&
                Objects.equals(attachmentText, license.attachmentText) &&
                Objects.equals(licensing, license.licensing) &&
                Objects.equals(acknowledgement, license.acknowledgement) &&
                Objects.equals(properties, license.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, attachmentText, properties, licensing, acknowledgement);
    }
}
