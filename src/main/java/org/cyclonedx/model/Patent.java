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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.util.serializer.CustomDateSerializer;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Patent information.
 *
 * @since 10.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({
    "bomRef", "patentNumber", "applicationNumber", "jurisdiction", "priorityApplication",
    "publicationNumber", "title", "abstract", "filingDate", "grantDate", "patentExpirationDate",
    "patentLegalStatus", "patentAssignee", "externalReferences"
})
public class Patent extends ExtensibleElement {

    public enum PatentLegalStatus {
        @JsonProperty("pending")
        PENDING("pending"),
        @JsonProperty("granted")
        GRANTED("granted"),
        @JsonProperty("expired")
        EXPIRED("expired"),
        @JsonProperty("abandoned")
        ABANDONED("abandoned"),
        @JsonProperty("revoked")
        REVOKED("revoked"),
        @JsonProperty("withdrawn")
        WITHDRAWN("withdrawn"),
        @JsonProperty("lapsed")
        LAPSED("lapsed"),
        @JsonProperty("suspended")
        SUSPENDED("suspended"),
        @JsonProperty("reinstated")
        REINSTATED("reinstated"),
        @JsonProperty("opposed")
        OPPOSED("opposed"),
        @JsonProperty("terminated")
        TERMINATED("terminated"),
        @JsonProperty("invalidated")
        INVALIDATED("invalidated"),
        @JsonProperty("in-force")
        IN_FORCE("in-force");

        private final String value;

        PatentLegalStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static PatentLegalStatus fromValue(String value) {
            if (value != null) {
                for (PatentLegalStatus status : values()) {
                    if (status.value.equalsIgnoreCase(value)) {
                        return status;
                    }
                }
            }
            return null;
        }
    }

    @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
    @JsonProperty("bom-ref")
    private String bomRef;

    private String patentNumber;
    private String applicationNumber;
    private String jurisdiction;
    private PriorityApplication priorityApplication;
    private String publicationNumber;
    private String title;

    @JsonProperty("abstract")
    private String patentAbstract;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date filingDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date grantDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date patentExpirationDate;

    private PatentLegalStatus patentLegalStatus;

    @JacksonXmlElementWrapper(localName = "patentAssignee")
    @JacksonXmlProperty(localName = "patentAssignee")
    private List<OrganizationalChoice> patentAssignee;

    @JacksonXmlElementWrapper(localName = "externalReferences")
    @JacksonXmlProperty(localName = "reference")
    private List<ExternalReference> externalReferences;

    public String getBomRef() {
        return bomRef;
    }

    public void setBomRef(String bomRef) {
        this.bomRef = bomRef;
    }

    public String getPatentNumber() {
        return patentNumber;
    }

    public void setPatentNumber(String patentNumber) {
        this.patentNumber = patentNumber;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public PriorityApplication getPriorityApplication() {
        return priorityApplication;
    }

    public void setPriorityApplication(PriorityApplication priorityApplication) {
        this.priorityApplication = priorityApplication;
    }

    public String getPublicationNumber() {
        return publicationNumber;
    }

    public void setPublicationNumber(String publicationNumber) {
        this.publicationNumber = publicationNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPatentAbstract() {
        return patentAbstract;
    }

    public void setPatentAbstract(String patentAbstract) {
        this.patentAbstract = patentAbstract;
    }

    public Date getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(Date filingDate) {
        this.filingDate = filingDate;
    }

    public Date getGrantDate() {
        return grantDate;
    }

    public void setGrantDate(Date grantDate) {
        this.grantDate = grantDate;
    }

    public Date getPatentExpirationDate() {
        return patentExpirationDate;
    }

    public void setPatentExpirationDate(Date patentExpirationDate) {
        this.patentExpirationDate = patentExpirationDate;
    }

    public PatentLegalStatus getPatentLegalStatus() {
        return patentLegalStatus;
    }

    public void setPatentLegalStatus(PatentLegalStatus patentLegalStatus) {
        this.patentLegalStatus = patentLegalStatus;
    }

    public List<OrganizationalChoice> getPatentAssignee() {
        return patentAssignee;
    }

    public void setPatentAssignee(List<OrganizationalChoice> patentAssignee) {
        this.patentAssignee = patentAssignee;
    }

    public List<ExternalReference> getExternalReferences() {
        return externalReferences;
    }

    public void setExternalReferences(List<ExternalReference> externalReferences) {
        this.externalReferences = externalReferences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patent)) return false;
        Patent patent = (Patent) o;
        return Objects.equals(bomRef, patent.bomRef) &&
                Objects.equals(patentNumber, patent.patentNumber) &&
                Objects.equals(applicationNumber, patent.applicationNumber) &&
                Objects.equals(jurisdiction, patent.jurisdiction) &&
                Objects.equals(priorityApplication, patent.priorityApplication) &&
                Objects.equals(publicationNumber, patent.publicationNumber) &&
                Objects.equals(title, patent.title) &&
                Objects.equals(patentAbstract, patent.patentAbstract) &&
                Objects.equals(filingDate, patent.filingDate) &&
                Objects.equals(grantDate, patent.grantDate) &&
                Objects.equals(patentExpirationDate, patent.patentExpirationDate) &&
                patentLegalStatus == patent.patentLegalStatus &&
                Objects.equals(patentAssignee, patent.patentAssignee) &&
                Objects.equals(externalReferences, patent.externalReferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bomRef, patentNumber, applicationNumber, jurisdiction, priorityApplication,
                publicationNumber, title, patentAbstract, filingDate, grantDate, patentExpirationDate,
                patentLegalStatus, patentAssignee, externalReferences);
    }
}
