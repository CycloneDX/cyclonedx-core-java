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
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
@JsonPropertyOrder({"name", "email", "phone"})
public class OrganizationalContact {

    @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
    @JsonProperty("bom-ref")
    @VersionFilter(versions = {"1.0", "1.1", "1.2", "1.3", "1.4"})
    private String bomRef;
    private String name;
    private String email;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBomRef() {
        return bomRef;
    }

    public void setBomRef(final String bomRef) {
        this.bomRef = bomRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationalContact that = (OrganizationalContact) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phone, that.phone) &&
            Objects.equals(bomRef, that.bomRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, phone, bomRef);
    }
}
