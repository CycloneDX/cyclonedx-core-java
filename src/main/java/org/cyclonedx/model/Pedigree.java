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

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@SuppressWarnings("unused")
@JsonPropertyOrder({"ancestors", "descendants", "variants", "commits", "notes"})
public class Pedigree extends ExtensibleElement {

    @JacksonXmlProperty(localName = "ancestors")
    private Ancestors ancestors;
    @JacksonXmlProperty(localName = "descendants")
    private Descendants descendants;
    @JacksonXmlProperty(localName = "variants")
    private Variants variants;
    private List<Commit> commits;
    private String notes;

    @JsonIgnore
    public List<Component> getAncestors() {
        if (null != ancestors) {
            return ancestors.getComponents();
        }
        return null;
    }

    @JsonIgnore
    public List<Component> getDescendants() {
        if (null != descendants) {
            return descendants.getComponents();
        }
        return null;
    }

    @JsonIgnore
    public List<Component> getVariants() {
        if (null != variants) {
            return variants.getComponents();
        }
        return null;
    }

    @JacksonXmlElementWrapper(localName = "commits")
    @JacksonXmlProperty(localName = "commit")
    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
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
