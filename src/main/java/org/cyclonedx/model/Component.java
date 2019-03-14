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

import com.github.packageurl.PackageURL;
import org.cyclonedx.CycloneDxSchema;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
    private String scope;
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

    @XmlElement(name = "publisher", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getGroup() {
        return group;
    }

    @XmlElement(name = "group", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    @XmlElement(name = "name", required = true, namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    @XmlElement(name = "version", required = true, namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement(name = "description", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setDescription(String description) {
        this.description = description;
    }

    public String getScope() {
        return scope;
    }

    @XmlElement(name = "scope", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<Hash> getHashes() {
        return hashes;
    }

    @XmlElementWrapper(name = "hashes", namespace = CycloneDxSchema.NS_BOM_LATEST)
    @XmlElement(name = "hash", namespace = CycloneDxSchema.NS_BOM_LATEST)
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

    @XmlElement(name = "licenses", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setLicenseChoice(LicenseChoice licenseChoice) {
        this.licenseChoice = licenseChoice;
    }

    public String getCopyright() {
        return copyright;
    }

    @XmlElement(name = "copyright", namespace = CycloneDxSchema.NS_BOM_LATEST)
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
    @XmlElement(name = "cpe", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setCpe(String cpe) {
        this.cpe = cpe;
    }

    public String getPurl() {
        return purl;
    }

    @XmlElement(name = "purl", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setPurl(String purl) {
        this.purl = purl;
    }

    public void setPurl(PackageURL purl) {
        this.purl = purl.canonicalize();
    }

    public boolean isModified() {
        return modified;
    }

    @XmlElement(name = "modified", required = true, namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public Pedigree getPedigree() {
        return pedigree;
    }

    @XmlElement(name = "pedigree", namespace = CycloneDxSchema.NS_BOM_LATEST)
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

    @XmlElementWrapper(name = "externalReferences", namespace = CycloneDxSchema.NS_BOM_LATEST)
    @XmlElement(name = "reference", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setExternalReferences(List<ExternalReference> externalReferences) {
        this.externalReferences = externalReferences;
    }

    public List<Component> getComponents() {
        return components;
    }

    @XmlElementWrapper(name = "components", namespace = CycloneDxSchema.NS_BOM_LATEST)
    @XmlElement(name = "component", namespace = CycloneDxSchema.NS_BOM_LATEST)
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

    @XmlAttribute(name = "type", required = true)
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
