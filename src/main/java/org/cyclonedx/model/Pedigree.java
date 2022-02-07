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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.util.ComponentWrapperDeserializer;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"ancestors", "descendants", "variants", "commits", "patches", "notes"})
public class Pedigree extends ExtensibleElement {

    @JsonDeserialize(using = ComponentWrapperDeserializer.class)
    private Ancestors ancestors;

    @JsonDeserialize(using = ComponentWrapperDeserializer.class)
    private Descendants descendants;

    @JsonDeserialize(using = ComponentWrapperDeserializer.class)
    private Variants variants;

    private List<Commit> commits;

    @VersionFilter(versions = {"1.2", "1.3"})
    private List<Patch> patches;

    private String notes;

    public Ancestors getAncestors() {
        return ancestors;
    }
    public void setAncestors(final Ancestors ancestors) {
        this.ancestors = ancestors;
    }

    public Descendants getDescendants() {
        return descendants;
    }
    public void setDescendants(final Descendants descendants) {
        this.descendants = descendants;
    }

    public Variants getVariants() {
        return variants;
    }
    public void setVariants(final Variants variants) {
        this.variants = variants;
    }

    @JacksonXmlElementWrapper(localName = "commits")
    @JacksonXmlProperty(localName = "commit")
    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

    @JacksonXmlElementWrapper(localName = "patches")
    @JacksonXmlProperty(localName = "patch")
    public List<Patch> getPatches() {
        return patches;
    }

    public void setPatches(List<Patch> patches) {
        this.patches = patches;
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
        if (!(o instanceof Pedigree)) return false;
        Pedigree pedigree = (Pedigree) o;
        return Objects.equals(ancestors, pedigree.ancestors) &&
            Objects.equals(descendants, pedigree.descendants) &&
            Objects.equals(variants, pedigree.variants) &&
            Objects.equals(commits, pedigree.commits) &&
            Objects.equals(notes, pedigree.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ancestors, descendants, variants, commits, notes);
    }
}
