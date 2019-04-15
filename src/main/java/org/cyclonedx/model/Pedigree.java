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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.CycloneDxSchema;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Pedigree {

    @JacksonXmlProperty(localName = "ancestors", namespace = CycloneDxSchema.NS_BOM_LATEST)
    private Ancestors ancestors;

    @JacksonXmlProperty(localName = "descendants", namespace = CycloneDxSchema.NS_BOM_LATEST)
    private Descendants descendants;

    @JacksonXmlProperty(localName = "variants", namespace = CycloneDxSchema.NS_BOM_LATEST)
    private Variants variants;

    private List<Commit> commits;
    private String notes;

    public Ancestors getAncestors() {
        return ancestors;
    }

    public Descendants getDescendants() {
        return descendants;
    }

    public Variants getVariants() {
        return variants;
    }

    public static class Ancestors {
        private List<Component> components;

        public List<Component> getComponents() {
            return components;
        }

        public void addComponent(Component component) {
            if (this.components == null) {
                this.components = new ArrayList<>();
            }
            this.components.add(component);
        }

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "component", namespace = CycloneDxSchema.NS_BOM_LATEST)
        public void setComponents(List<Component> ancestors) {
            this.components = ancestors;
        }
    }

    public static class Descendants {
        private List<Component> components;

        public List<Component> getComponents() {
            return components;
        }

        public void addComponent(Component component) {
            if (this.components == null) {
                this.components = new ArrayList<>();
            }
            this.components.add(component);
        }

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "component", namespace = CycloneDxSchema.NS_BOM_LATEST)
        public void setComponents(List<Component> ancestors) {
            this.components = ancestors;
        }
    }

    public static class Variants {
        private List<Component> components;

        public List<Component> getComponents() {
            return components;
        }

        public void addComponent(Component component) {
            if (this.components == null) {
                this.components = new ArrayList<>();
            }
            this.components.add(component);
        }

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "component", namespace = CycloneDxSchema.NS_BOM_LATEST)
        public void setComponents(List<Component> ancestors) {
            this.components = ancestors;
        }
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public void addCommit(Commit commit) {
        if (this.commits == null) {
            this.commits = new ArrayList<>();
        }
        this.commits.add(commit);
    }

    @JacksonXmlElementWrapper(localName = "commits", namespace = CycloneDxSchema.NS_BOM_LATEST)
    @JacksonXmlProperty(localName = "commit", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

    public String getNotes() {
        return notes;
    }

    @JacksonXmlProperty(localName = "notes", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
