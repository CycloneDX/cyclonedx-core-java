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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.cyclonedx.CycloneDxSchema;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@JacksonXmlRootElement(localName = "bom", namespace = CycloneDxSchema.NS_BOM_LATEST)
public class Bom {

    private List<Component> components;
    private List<ExternalReference> externalReferences;
    private int version = 1;
    private String serialNumber;

    public List<Component> getComponents() {
        return components;
    }

    @JacksonXmlElementWrapper(localName = "components", namespace = CycloneDxSchema.NS_BOM_LATEST)
    @JacksonXmlProperty(localName = "component", namespace = CycloneDxSchema.NS_BOM_LATEST)
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

    @JacksonXmlElementWrapper(localName = "externalReferences", namespace = CycloneDxSchema.NS_BOM_LATEST)
    @JacksonXmlProperty(localName = "reference", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setExternalReferences(List<ExternalReference> externalReferences) {
        this.externalReferences = externalReferences;
    }

    public int getVersion() {
        return version;
    }

    @JacksonXmlProperty(localName = "version")
    public void setVersion(int version) {
        this.version = version;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    @JacksonXmlProperty(localName = "serialNumber")
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}