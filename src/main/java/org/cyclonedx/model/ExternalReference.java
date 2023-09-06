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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.util.serializer.ExternalReferenceSerializer;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSerialize(using = ExternalReferenceSerializer.class)
@JsonPropertyOrder({"url", "comment", "hashes"})
public class ExternalReference {

    public enum Type {
        @JsonProperty("vcs")
        VCS("vcs"),
        @JsonProperty("issue-tracker")
        ISSUE_TRACKER("issue-tracker"),
        @JsonProperty("website")
        WEBSITE("website"),
        @JsonProperty("advisories")
        ADVISORIES("advisories"),
        @JsonProperty("bom")
        BOM("bom"),
        @JsonProperty("mailing-list")
        MAILING_LIST("mailing-list"),
        @JsonProperty("social")
        SOCIAL("social"),
        @JsonProperty("chat")
        CHAT("chat"),
        @JsonProperty("documentation")
        DOCUMENTATION("documentation"),
        @JsonProperty("support")
        SUPPORT("support"),
        @JsonProperty("distribution")
        DISTRIBUTION("distribution"),
        @JsonProperty("distribution-intake")
        DISTRIBUTION_INTAKE("distribution-intake"),
        @JsonProperty("license")
        LICENSE("license"),
        @JsonProperty("build-meta")
        BUILD_META("build-meta"),
        @JsonProperty("build-system")
        BUILD_SYSTEM("build-system"),
        @JsonProperty("release-notes")
        RELEASE_NOTES("release-notes"),
        @JsonProperty("security-contact")
        SECURITY_CONTACT("security-contact"),
        @JsonProperty("model_card")
        MODEL_CARD("model_card"),
        @JsonProperty("attestation")
        ATTESTATION("attestation"),
        @JsonProperty("threat-model")
        THREAT_MODEL("threat-model"),
        @JsonProperty("adversary-model")
        ADVERSARY_MODEL("adversary-model"),
        @JsonProperty("risk-assessment")
        RISK_ASSESSMENT("risk-assessment"),
        @JsonProperty("vulnerability-assertion")
        VULNERABILITY_ASSERTION("vulnerability-assertion"),
        @JsonProperty("exploitability-statement")
        EXPLOITABILITY_STATEMENT("exploitability-statement"),
        @JsonProperty("pentest-report")
        PENTEST_REPORT("pentest-report"),
        @JsonProperty("static-analysis-report")
        STATIC_ANALYSIS_REPORT("static-analysis-report"),
        @JsonProperty("dynamic-analysis-report")
        DYNAMIC_ANALYSIS_REPORT("dynamic-analysis-report"),
        @JsonProperty("runtime-analysis-report")
        RUNTIME_ANALYSIS_REPORT("runtime-analysis-report"),
        @JsonProperty("component-analysis-report")
        COMPONENT_ANALYSIS_REPORT("component-analysis-report"),
        @JsonProperty("maturity-report")
        MATURITY_REPORT("maturity-report"),
        @JsonProperty("certification-report")
        CERTIFICATION_REPORT("certification-report"),
        @JsonProperty("codified-infrastructure")
        CODIFIED_INFRASTRUCTURE("codified-infrastructure"),
        @JsonProperty("quality-metrics")
        QUALITY_METRICS("quality-metrics"),
        @JsonProperty("log")
        LOG("log"),
        @JsonProperty("configuration")
        CONFIGURATION("configuration"),
        @JsonProperty("evidence")
        EVIDENCE("evidence"),
        @JsonProperty("formulation")
        FORMULATION("formulation"),
        @JsonProperty("other")
        OTHER("other");

        private final String name;

        public String getTypeName() {
            return this.name;
        }

        Type(String name) {
            this.name = name;
        }

        public static Type fromString(String text) {
            for (Type t : Type.values()) {
                if (t.name.equals(text)) {
                    return t;
                }
            }
            return null;
        }
    }

    private String url;
    @JacksonXmlProperty(localName = "type", isAttribute = true)
    private Type type;
    private String comment;

    @VersionFilter(versions = {"1.0", "1.1", "1.2"})
    private List<Hash> hashes;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @JacksonXmlElementWrapper(localName = "hashes")
    @JacksonXmlProperty(localName = "hash")
    public List<Hash> getHashes() {
        return hashes;
    }

    public void setHashes(List<Hash> hashes) {
        this.hashes = hashes;
    }

    public void addHash(Hash hash) {
        if (this.hashes == null) {
            this.hashes = new ArrayList<>();
        }
        this.hashes.add(hash);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExternalReference)) return false;
        ExternalReference reference = (ExternalReference) o;
        return Objects.equals(url, reference.url) &&
                type == reference.type &&
                Objects.equals(comment, reference.comment) &&
                Objects.equals(hashes, reference.hashes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, type, comment, hashes);
    }
}
