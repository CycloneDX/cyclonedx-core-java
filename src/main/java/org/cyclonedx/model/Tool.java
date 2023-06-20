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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"vendor", "name", "version", "hashes", "externalReferences"})
@Deprecated
public class Tool extends ExtensibleElement {

    private String vendor;
    private String name;
    private String version;
    private List<Hash> hashes;
    private List<ExternalReference> externalReferences;

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @JacksonXmlElementWrapper(localName = "hashes")
    @JacksonXmlProperty(localName = "hash")
    public List<Hash> getHashes() {
        return hashes;
    }

    public void setHashes(List<Hash> hashes) {
        this.hashes = hashes;
    }

    @JacksonXmlElementWrapper(localName = "externalReferences")
    @JacksonXmlProperty(localName = "reference")
    public List<ExternalReference> getExternalReferences() {
        return externalReferences;
    }

    public void setExternalReferences(final List<ExternalReference> externalReferences) {
        this.externalReferences = externalReferences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tool tool = (Tool) o;
        return Objects.equals(vendor, tool.vendor) &&
                Objects.equals(name, tool.name) &&
                Objects.equals(version, tool.version) &&
                Objects.equals(hashes, tool.hashes) &&
                Objects.equals(externalReferences, tool.externalReferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendor, name, version, hashes, externalReferences);
    }
}
