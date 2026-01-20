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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;
import java.util.Objects;

/**
 * A patent assertion represents a claim about patent ownership or licensing.
 *
 * @since 10.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"bomRef", "assertionType", "patents", "patentFamilies", "properties"})
public class PatentAssertion extends ExtensibleElement {

    public enum AssertionType {
        @JsonProperty("ownership")
        OWNERSHIP("ownership"),
        @JsonProperty("license")
        LICENSE("license"),
        @JsonProperty("third-party-claim")
        THIRD_PARTY_CLAIM("third-party-claim"),
        @JsonProperty("standards-inclusion")
        STANDARDS_INCLUSION("standards-inclusion"),
        @JsonProperty("prior-art")
        PRIOR_ART("prior-art"),
        @JsonProperty("exclusive-rights")
        EXCLUSIVE_RIGHTS("exclusive-rights"),
        @JsonProperty("non-assertion")
        NON_ASSERTION("non-assertion"),
        @JsonProperty("research-or-evaluation")
        RESEARCH_OR_EVALUATION("research-or-evaluation");

        private final String value;

        AssertionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static AssertionType fromValue(String value) {
            if (value != null) {
                for (AssertionType type : values()) {
                    if (type.value.equalsIgnoreCase(value)) {
                        return type;
                    }
                }
            }
            return null;
        }
    }

    @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
    @JsonProperty("bom-ref")
    private String bomRef;

    private AssertionType assertionType;

    private OrganizationalChoice asserter;

    @JacksonXmlElementWrapper(localName = "patents")
    @JacksonXmlProperty(localName = "patent")
    private List<BomReference> patents;

    @JacksonXmlElementWrapper(localName = "patentFamilies")
    @JacksonXmlProperty(localName = "patentFamily")
    private List<BomReference> patentFamilies;

    private String notes;

    @JacksonXmlElementWrapper(localName = "properties")
    @JacksonXmlProperty(localName = "property")
    private List<Property> properties;

    public String getBomRef() {
        return bomRef;
    }

    public void setBomRef(String bomRef) {
        this.bomRef = bomRef;
    }

    public AssertionType getAssertionType() {
        return assertionType;
    }

    public void setAssertionType(AssertionType assertionType) {
        this.assertionType = assertionType;
    }

    public OrganizationalChoice getAsserter() {
        return asserter;
    }

    public void setAsserter(OrganizationalChoice asserter) {
        this.asserter = asserter;
    }

    public List<BomReference> getPatents() {
        return patents;
    }

    public void setPatents(List<BomReference> patents) {
        this.patents = patents;
    }

    public List<BomReference> getPatentFamilies() {
        return patentFamilies;
    }

    public void setPatentFamilies(List<BomReference> patentFamilies) {
        this.patentFamilies = patentFamilies;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatentAssertion)) return false;
        PatentAssertion that = (PatentAssertion) o;
        return Objects.equals(bomRef, that.bomRef) &&
                assertionType == that.assertionType &&
                Objects.equals(asserter, that.asserter) &&
                Objects.equals(patents, that.patents) &&
                Objects.equals(patentFamilies, that.patentFamilies) &&
                Objects.equals(notes, that.notes) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bomRef, assertionType, asserter, patents, patentFamilies, notes, properties);
    }
}
