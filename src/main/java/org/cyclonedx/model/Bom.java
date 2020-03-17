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

import org.cyclonedx.model.ext.dependencyGraph.Dependency;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class Bom extends ExtensibleElement {

    private List<Component> components;
    private List<ExternalReference> externalReferences;
    private int version = 1;
    private String serialNumber;
    private String schemaVersion;

    // Extension models
    private List<Dependency> dependencies;

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public void addComponent(Component component) {
        if (components == null) {
            components = new ArrayList<>();
        }
        components.add(component);
    }

    public List<ExternalReference> getExternalReferences() {
        return externalReferences;
    }

    public void addExternalReference(ExternalReference externalReference) {
        if (externalReferences == null) {
            externalReferences = new ArrayList<>();
        }
        externalReferences.add(externalReference);
    }

    public void setExternalReferences(List<ExternalReference> externalReferences) {
        this.externalReferences = externalReferences;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Returns the CycloneDX schema version of a Bom. The schema version will
     * only be populated when paring a bom via {@link org.cyclonedx.BomParser}.
     * It has no affect on bom generation or any other functionality.
     * @return the String version representation of the schema version
     */
    public String getSchemaVersion() {
        return schemaVersion;
    }

    // Extension models
    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bom)) return false;
        Bom bom = (Bom) o;
        return version == bom.version &&
                Objects.equals(components, bom.components) &&
                Objects.equals(externalReferences, bom.externalReferences) &&
                Objects.equals(serialNumber, bom.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components, externalReferences, version, serialNumber);
    }
}
