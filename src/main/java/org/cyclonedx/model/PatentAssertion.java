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
@JsonPropertyOrder({"bomRef", "assertionType", "patentRefs", "asserter", "notes"})
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

    private List<String> patentRefs;

    private OrganizationalChoice asserter;

    private String notes;

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

    @JacksonXmlElementWrapper(localName = "patentRefs")
    @JacksonXmlProperty(localName = "bom-ref")
    @JsonProperty("patentRefs")
    public List<String> getPatentRefs() {
        return patentRefs;
    }

    public void setPatentRefs(List<String> patentRefs) {
        this.patentRefs = patentRefs;
    }

    public OrganizationalChoice getAsserter() {
        return asserter;
    }

    public void setAsserter(OrganizationalChoice asserter) {
        this.asserter = asserter;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatentAssertion)) return false;
        PatentAssertion that = (PatentAssertion) o;
        return Objects.equals(bomRef, that.bomRef) &&
                assertionType == that.assertionType &&
                Objects.equals(patentRefs, that.patentRefs) &&
                Objects.equals(asserter, that.asserter) &&
                Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bomRef, assertionType, patentRefs, asserter, notes);
    }
}
