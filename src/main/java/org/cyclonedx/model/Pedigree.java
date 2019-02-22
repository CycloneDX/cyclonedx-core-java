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

import org.cyclonedx.CycloneDxSchema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@XmlRootElement(name = "pedigree", namespace = CycloneDxSchema.NS_BOM_LATEST)
public class Pedigree {

    private List<Component> ancestors;
    private List<Component> descendants;
    private List<Component> variants;
    private List<Commit> commits;

    public List<Component> getAncestors() {
        return ancestors;
    }

    public void addAncestor(Component component) {
        if (this.ancestors == null) {
            this.ancestors = new ArrayList<>();
        }
        this.ancestors.add(component);
    }

    @XmlElementWrapper(name = "ancestors", namespace = CycloneDxSchema.NS_BOM_LATEST)
    @XmlElement(name = "component", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setAncestors(List<Component> ancestors) {
        this.ancestors = ancestors;
    }

    public List<Component> getDescendants() {
        return descendants;
    }

    public void addDescendant(Component component) {
        if (this.descendants == null) {
            this.descendants = new ArrayList<>();
        }
        this.descendants.add(component);
    }

    @XmlElementWrapper(name = "descendants", namespace = CycloneDxSchema.NS_BOM_LATEST)
    @XmlElement(name = "component", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setDescendants(List<Component> descendants) {
        this.descendants = descendants;
    }

    public List<Component> getVariants() {
        return variants;
    }

    public void addVariant(Component component) {
        if (this.variants == null) {
            this.variants = new ArrayList<>();
        }
        this.variants.add(component);
    }

    @XmlElementWrapper(name = "variants", namespace = CycloneDxSchema.NS_BOM_LATEST)
    @XmlElement(name = "component", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setVariants(List<Component> variants) {
        this.variants = variants;
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

    @XmlElementWrapper(name = "commits", namespace = CycloneDxSchema.NS_BOM_LATEST)
    @XmlElement(name = "commit", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }
}
