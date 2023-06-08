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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.github.packageurl.PackageURL;
import org.cyclonedx.util.LicenseDeserializer;

@SuppressWarnings("unused")
@JacksonXmlRootElement(localName = "formulation")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(
    {"supplier",
     "author",
     "publisher",
     "group",
     "name",
     "version",
     "description",
     "scope",
     "hashes",
     "licenses",
     "copyright",
     "cpe",
     "purl",
     "swid",
     "modified",
     "pedigree",
     "externalReferences",
     "properties",
     "components",
     "evidence",
     "releaseNotes",
     "signature"
    })
public class Formulation
    extends ExtensibleElement {

    public enum Type {
        @JsonProperty("application")
        APPLICATION("application"),
        @JsonProperty("framework")
        FRAMEWORK("framework"),
        @JsonProperty("library")
        LIBRARY("library"),
        @JsonProperty("container")
        CONTAINER("container"),
        @JsonProperty("operating-system")
        OPERATING_SYSTEM("operating-system"),
        @JsonProperty("device")
        DEVICE("device"),
        @JsonProperty("firmware")
        FIRMWARE("firmware"),
        @JsonProperty("file")
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
        @JsonProperty("required")
        REQUIRED("required"),
        @JsonProperty("optional")
        OPTIONAL("optional"),
        @JsonProperty("excluded")
        EXCLUDED("excluded");

        private final String name;

        public String getScopeName() {
            return this.name;
        }

        Scope(String name) {
            this.name = name;
        }
    }

    @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
    @JsonProperty("bom-ref")
    private String bomRef;
    @JacksonXmlProperty(isAttribute = true, localName = "mime-type")
    @JsonProperty("mime-type")
    private String mimeType;
    @VersionFilter(versions = {"1.0", "1.1"})
    private OrganizationalEntity supplier;
    @VersionFilter(versions = {"1.0", "1.1"})
    private String author;
    @VersionFilter(versions = {"1.0"})
    private String publisher;
    private String group;
    private String name;
    private String version;
    private String description;
    private Scope scope;
    private List<Hash> hashes;
    private LicenseChoice license;
    private String copyright;
    private String cpe;
    private String purl;
    @VersionFilter(versions = {"1.0", "1.1"})
    private Swid swid;
    private Boolean modified;
    @VersionFilter(versions = {"1.0"})
    private Pedigree pedigree;
    @VersionFilter(versions = {"1.0"})
    private List<ExternalReference> externalReferences;
    @VersionFilter(versions = {"1.0", "1.1", "1.2"})
    private List<Property> properties;
    private List<Formulation> components;
    @VersionFilter(versions = {"1.0", "1.1", "1.2"})
    private Evidence evidence;
    @JacksonXmlProperty(isAttribute = true)
    private Type type;
    @VersionFilter(versions = {"1.0", "1.1", "1.2", "1.3"})
    private ReleaseNotes releaseNotes;
    @JsonOnly
    @VersionFilter(versions = {"1.0", "1.1", "1.2", "1.3"})
    private Signature signature;

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

    @JacksonXmlElementWrapper(localName = "hashes")
    @JacksonXmlProperty(localName = "hash")
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

    @JacksonXmlProperty(localName = "licenses")
    @JsonProperty("licenses")
    @JsonDeserialize(using = LicenseDeserializer.class)
    public LicenseChoice getLicenseChoice() {
        return license;
    }

    public void setLicenseChoice(LicenseChoice licenseChoice) {
        this.license = licenseChoice;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * @return the Common Platform Enumeration of the component
     */
    public String getCpe() {
        return cpe;
    }

    /**
     * @param cpe a valid CPE 2.2 or CPE 2.3 string
     */
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean getModified() { return modified; }

    public Boolean isModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    public Pedigree getPedigree() {
        return pedigree;
    }

    public void setPedigree(Pedigree pedigree) {
        this.pedigree = pedigree;
    }

    @JacksonXmlElementWrapper(localName = "externalReferences")
    @JacksonXmlProperty(localName = "reference")
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

    @JacksonXmlElementWrapper(localName = "properties")
    @JacksonXmlProperty(localName = "property")
    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public void addProperty(Property property) {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        }
        this.properties.add(property);
    }

    @JacksonXmlElementWrapper(localName = "components")
    @JacksonXmlProperty(localName = "component")
    public List<Formulation> getComponents() {
        return components;
    }

    public void setComponents(List<Formulation> components) {
        this.components = components;
    }

    public void addComponent(Formulation component) {
        if (this.components == null) {
            this.components = new ArrayList<>();
        }
        this.components.add(component);
    }

    public Evidence getEvidence() {
        return evidence;
    }

    public void setEvidence(Evidence evidence) {
        this.evidence = evidence;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ReleaseNotes getReleaseNotes() { return releaseNotes; }

    public void setReleaseNotes(ReleaseNotes releaseNotes) { this.releaseNotes = releaseNotes; }

    public Signature getSignature() { return signature; }

    public void setSignature(Signature signature) { this.signature = signature; }

    @Override
    public int hashCode() {
        return Objects.hash(author, publisher, group, name, version, description, scope, hashes, license, copyright, cpe, purl, swid, modified, components, evidence, releaseNotes, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Formulation component = (Formulation) o;
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
                Objects.equals(license, component.license) &&
                Objects.equals(copyright, component.copyright) &&
                Objects.equals(cpe, component.cpe) &&
                Objects.equals(purl, component.purl) &&
                Objects.equals(swid, component.swid) &&
                Objects.equals(components, component.components) &&
                Objects.equals(evidence, component.evidence) &&
                Objects.equals(mimeType, component.mimeType) &&
                Objects.equals(releaseNotes, component.releaseNotes) &&
                Objects.equals(type, component.type);
    }
}
