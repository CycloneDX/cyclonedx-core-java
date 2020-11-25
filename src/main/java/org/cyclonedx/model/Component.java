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

import com.github.packageurl.PackageURL;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.cyclonedx.converters.ExtensionConverter;
import org.cyclonedx.model.vulnerability.Vulnerability1_0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.naming.ldap.ExtendedRequest;

@SuppressWarnings("unused")
public class Component extends ExtensibleElement {

    public enum Type {
        APPLICATION("application"),
        FRAMEWORK("framework"),
        LIBRARY("library"),
        CONTAINER("container"),
        OPERATING_SYSTEM("operating-system"),
        DEVICE("device"),
        FIRMWARE("firmware"),
        FILE("file");

        private final String name;

        public String getTypeName() {
            return this.name;
        }

        Type(String name) {
            this.name = name;
        }
    }

    public enum Scope {
        REQUIRED("required"),
        OPTIONAL("optional"),
        EXCLUDED("excluded");

        private final String name;

        public String getScopeName() {
            return this.name;
        }

        Scope(String name) {
            this.name = name;
        }
    }

    private String bomRef;
    private String mimeType;
    private OrganizationalEntity supplier;
    private String author;
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
    private Swid swid;
    private boolean modified;
    private Pedigree pedigree;
    private List<ExternalReference> externalReferences;
    private List<Component> components;
    private Type type;
    @XStreamConverter(value=ExtensionConverter.class)
    private Map<String, List<ExtensibleExtension>> extensions;

    public String getBomRef() {
        return bomRef;
    }

    public void setBomRef(String bomRef) {
        this.bomRef = bomRef;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public OrganizationalEntity getSupplier() {
        return supplier;
    }

    public void setSupplier(OrganizationalEntity supplier) {
        this.supplier = supplier;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

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

    public LicenseChoice getLicenseChoice() {
        return licenseChoice;
    }

    public void setLicenseChoice(LicenseChoice licenseChoice) {
        this.licenseChoice = licenseChoice;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * @deprecated CPE will be removed in a future version of the CycloneDX specification.
     * @return the Common Platform Enumeration of the component
     */
    @Deprecated
    public String getCpe() {
        return cpe;
    }

    /**
     * @deprecated CPE will be removed in a future version of the CycloneDX specification.
     * @param cpe a valid CPE 2.2 or CPE 2.3 string
     */
    @Deprecated
    public void setCpe(String cpe) {
        this.cpe = cpe;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public void setPurl(PackageURL purl) {
        this.purl = purl.canonicalize();
    }

    public Swid getSwid() {
        return swid;
    }

    public void setSwid(Swid swid) {
        this.swid = swid;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public Pedigree getPedigree() {
        return pedigree;
    }

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

    public void setExternalReferences(List<ExternalReference> externalReferences) {
        this.externalReferences = externalReferences;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public void addComponent(Component component) {
        if (this.components == null) {
            this.components = new ArrayList<>();
        }
        this.components.add(component);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Map<String, List<ExtensibleExtension>> getExtensions() {
        return extensions;
    }

    public void setExtensions(final Map<String, List<ExtensibleExtension>> extensions) {
        this.extensions = extensions;
    }

    public void addExtension(String extensionName, List<ExtensibleExtension> extension) {
        if (this.extensions == null) {
            this.extensions = new HashMap<>();
        }
        this.extensions.put(extensionName, extension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, publisher, group, name, version, description, scope, hashes, licenseChoice, copyright, cpe, purl, swid, modified, components, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return modified == component.modified &&
            Objects.equals(supplier, component.supplier) &&
            Objects.equals(author, component.author) &&
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
            Objects.equals(swid, component.swid) &&
            Objects.equals(components, component.components) &&
            Objects.equals(mimeType, component.mimeType) &&
            Objects.equals(type, component.type);
    }
}
