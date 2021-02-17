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
package org.cyclonedx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonPropertyOrder({"name", "url", "contact"})
public class OrganizationalEntity extends ExtensibleElement {

    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationalEntity that = (OrganizationalEntity) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(url, that.url) &&
                Objects.equals(contact, that.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, contact);
    }
}
