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
import com.github.packageurl.PackageURL;
import org.cyclonedx.CycloneDxSchema;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class Component {

    private String publisher;
    private String group;
    private String name;
    private String version;
    private String description;
    private Scope scope;
    private List<Hash> hashes;
    private LicenseChoice licenseChoice;
    private String copyright;
    private String cpe;
    private String purl;
    private boolean modified;
    private Pedigree pedigree;
    private List<ExternalReference> externalReferences;
    private List<Component> components;
    private String type;

    public enum Classification {
        APPLICATION("application"),
        FRAMEWORK("framework"),
        LIBRARY("library"),
        FILE("file"),
        OPERATING_SYSTEM("operating-system"),
        DEVICE("device");

        private String value;
        Classification(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public String getPublisher() {
        return publisher;
    }

    @JacksonXmlProperty(localName = "publisher", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getGroup() {
        return group;
    }

    @JacksonXmlProperty(localName = "group", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    @JacksonXmlProperty(localName = "name", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    @JacksonXmlProperty(localName = "version", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    @JacksonXmlProperty(localName = "description", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setDescription(String description) {
        this.description = description;
    }

    public Scope getScope() {
        return scope;
    }

    @JacksonXmlProperty(localName = "scope", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public List<Hash> getHashes() {
        return hashes;
    }

    @JacksonXmlElementWrapper(localName = "hashes", namespace = CycloneDxSchema.NS_BOM_LATEST)
    @JacksonXmlProperty(localName = "hash", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setHashes(List<Hash> hashes) {
        this.hashes = hashes;
    }

    public void addHash(Hash hash) {
        if (this.hashes == null) {
            this.hashes = new ArrayList<>();
        }
        this.hashes.add(hash);
    }

    public LicenseChoice getLicenseChoice() {
        return licenseChoice;
    }

    @JacksonXmlProperty(localName = "licenses", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setLicenseChoice(LicenseChoice licenseChoice) {
        this.licenseChoice = licenseChoice;
    }

    public String getCopyright() {
        return copyright;
    }

    @JacksonXmlProperty(localName = "copyright", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * @deprecated CPE will be removed in a future version of the CycloneDX specification/
     * @return the Common Platform Enumeration of the component
     */
    @Deprecated
    public String getCpe() {
        return cpe;
    }

    /**
     * @deprecated CPE will be removed in a future version of the CycloneDX specification/
     */
    @Deprecated
    @JacksonXmlProperty(localName = "cpe", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setCpe(String cpe) {
        this.cpe = cpe;
    }

    public String getPurl() {
        return purl;
    }

    @JacksonXmlProperty(localName = "purl", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setPurl(String purl) {
        this.purl = purl;
    }

    public void setPurl(PackageURL purl) {
        this.purl = purl.canonicalize();
    }

    public boolean isModified() {
        return modified;
    }

    @JacksonXmlProperty(localName = "modified", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public Pedigree getPedigree() {
        return pedigree;
    }

    @JacksonXmlProperty(localName = "pedigree", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setPedigree(Pedigree pedigree) {
        this.pedigree = pedigree;
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

    public List<Component> getComponents() {
        return components;
    }

    @JacksonXmlElementWrapper(localName = "components", namespace = CycloneDxSchema.NS_BOM_LATEST)
    @JacksonXmlProperty(localName = "component", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public void addComponent(Component component) {
        if (this.components == null) {
            this.components = new ArrayList<>();
        }
        this.components.add(component);
    }

    public String getType() {
        return type;
    }

    @JacksonXmlProperty(localName = "type", isAttribute = true)
    public void setType(String type) {
        this.type = type;
    }

    public void setType(Classification classification) {
        this.type = classification.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(publisher, group, name, version, description, scope, hashes, licenseChoice, copyright, cpe, purl, modified, components, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return modified == component.modified &&
                Objects.equals(publisher, component.publisher) &&
                Objects.equals(group, component.group) &&
                Objects.equals(name, component.name) &&
                Objects.equals(version, component.version) &&
                Objects.equals(description, component.description) &&
                Objects.equals(scope, component.scope) &&
                Objects.equals(hashes, component.hashes) &&
                Objects.equals(licenseChoice, component.licenseChoice) &&
                Objects.equals(copyright, component.copyright) &&
                Objects.equals(cpe, component.cpe) &&
                Objects.equals(purl, component.purl) &&
                Objects.equals(components, component.components) &&
                Objects.equals(type, component.type);
    }
}
