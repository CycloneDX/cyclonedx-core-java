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
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.cyclonedx.model;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class Pedigree {

    private List<Component> ancestors;
    private List<Component> descendants;
    private List<Component> variants;
    private List<Commit> commits;
    private String notes;

    public List<Component> getAncestors() {
        return ancestors;
    }

    public void setAncestors(List<Component> ancestors) {
        this.ancestors = ancestors;
    }

    public List<Component> getDescendants() {
        return descendants;
    }

    public void setDescendants(List<Component> descendants) {
        this.descendants = descendants;
    }

    public List<Component> getVariants() {
        return variants;
    }

    public void setVariants(List<Component> variants) {
        this.variants = variants;
    }

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
