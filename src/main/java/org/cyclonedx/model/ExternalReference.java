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
import org.cyclonedx.util.ExternalReferenceSerializer;

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
        @JsonProperty("license")
        LICENSE("license"),
        @JsonProperty("build-meta")
        BUILD_META("build-meta"),
        @JsonProperty("build-system")
        BUILD_SYSTEM("build-system"),
        @JsonProperty("release-notes")
        RELEASE_NOTES("release-notes"),
        @JsonProperty("other")
        OTHER("other");

        private final String name;

        public String getTypeName() {
            return this.name;
        }

        Type(String name) {
            this.name = name;
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
