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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.cyclonedx.Version;
import org.cyclonedx.model.organization.PostalAddress;
import org.cyclonedx.util.deserializer.OrganizationalEntityDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"name", "address", "url", "contact"})
@JsonDeserialize(using = OrganizationalEntityDeserializer.class)
public class OrganizationalEntity {

    @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
    @JsonProperty("bom-ref")
    @VersionFilter(Version.VERSION_15)
    private String bomRef;

    private String name;

    @VersionFilter(Version.VERSION_16)
    private PostalAddress address;

    private List<String> url;
    @JsonProperty("contact")
    private List<OrganizationalContact> contact;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "url")
    @JsonProperty("url")
    public List<String> getUrls() {
        return url;
    }

    public void setUrls(List<String> url) {
        this.url = url;
    }

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "contact")
    @JsonProperty("contact")
    public List<OrganizationalContact> getContacts() {
        return contact;
    }

    public void setContacts(List<OrganizationalContact> contacts) {
        this.contact = contacts;
    }

    public void addContact(OrganizationalContact contact) {
        if (this.contact == null) {
            this.contact = new ArrayList<>();
        }
        this.contact.add(contact);
    }

    public String getBomRef() {
        return bomRef;
    }

    public void setBomRef(final String bomRef) {
        this.bomRef = bomRef;
    }

    public PostalAddress getAddress() {
        return address;
    }

    public void setAddress(final PostalAddress address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationalEntity that = (OrganizationalEntity) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(url, that.url) &&
                Objects.equals(contact, that.contact) &&
                Objects.equals(address, that.address) &&
                Objects.equals(bomRef, that.bomRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, contact, bomRef, address);
    }
}
