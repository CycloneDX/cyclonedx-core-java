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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.cyclonedx.Version;
import org.cyclonedx.model.component.ModelCard;
import org.cyclonedx.model.component.crypto.CryptoProperties;
import org.cyclonedx.model.component.Tags;
import org.cyclonedx.model.component.modelCard.ComponentData;
import org.cyclonedx.util.deserializer.ExternalReferencesDeserializer;
import org.cyclonedx.util.deserializer.HashesDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.github.packageurl.PackageURL;
import org.cyclonedx.util.deserializer.LicenseDeserializer;
import org.cyclonedx.util.deserializer.PropertiesDeserializer;

@SuppressWarnings("unused")
@JacksonXmlRootElement(localName = "component")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(
    {
     "type",
     "bom-ref",
     "supplier",
     "manufacturer",
     "authors",
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
     "omniborId",
     "swhid",
     "swid",
     "modified",
     "pedigree",
     "externalReferences",
     "properties",
     "components",
     "evidence",
     "releaseNotes",
     "modelCard",
     "data",
     "cryptoProperties",
     "signature",
     "provides"
    })
public class Component extends ExtensibleElement {

    public enum Type {
        @JsonProperty("application")
        APPLICATION("application"),
        @JsonProperty("framework")
        FRAMEWORK("framework"),
        @JsonProperty("library")
        LIBRARY("library"),
        @JsonProperty("container")
        CONTAINER("container"),
        @JsonProperty("platform")
        PLATFORM("platform"),
        @JsonProperty("operating-system")
        OPERATING_SYSTEM("operating-system"),
        @JsonProperty("device")
        DEVICE("device"),
        @JsonProperty("device-driver")
        DEVICE_DRIVER("device-driver"),
        @JsonProperty("firmware")
        FIRMWARE("firmware"),
        @JsonProperty("file")
        FILE("file"),
        @JsonProperty("machine-learning-model")
        MACHINE_LEARNING_MODEL("machine-learning-model"),
        @JsonProperty("data")
        DATA("data"),
        @VersionFilter(Version.VERSION_16)
        @JsonProperty("cryptographic-asset")
        CRYPTOGRAPHIC_ASSET("cryptographic-asset");

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
    @VersionFilter(Version.VERSION_11)
    private String bomRef;

    @JacksonXmlProperty(isAttribute = true, localName = "mime-type")
    @JsonProperty("mime-type")
    private String mimeType;

    @JacksonXmlProperty(isAttribute = true)
    private Type type;

    @VersionFilter(Version.VERSION_12)
    private OrganizationalEntity supplier;

    @Deprecated
    @VersionFilter(Version.VERSION_12)
    private String author;

    @VersionFilter(Version.VERSION_11)
    private String publisher;
    private String group;
    private String name;
    private String version;
    private String description;
    private Scope scope;
    private List<Hash> hashes;
    private LicenseChoice licenses;
    private String copyright;
    private String cpe;
    private String purl;

    @VersionFilter(Version.VERSION_16)
    private List<String> omniborId;

    @VersionFilter(Version.VERSION_16)
    private List<String> swhid;
    @VersionFilter(Version.VERSION_12)
    private Swid swid;

    private Boolean modified;

    @VersionFilter(Version.VERSION_11)
    private Pedigree pedigree;

    @VersionFilter(Version.VERSION_11)
    private List<ExternalReference> externalReferences;

    @VersionFilter(Version.VERSION_13)
    private List<Property> properties;

    private List<Component> components;

    @VersionFilter(Version.VERSION_13)
    private Evidence evidence;

    @VersionFilter(Version.VERSION_14)
    private ReleaseNotes releaseNotes;

    @VersionFilter(Version.VERSION_15)
    @JsonProperty("modelCard")
    private ModelCard modelCard;

    @VersionFilter(Version.VERSION_15)
    @JsonProperty("data")
    private ComponentData data;

    @VersionFilter(Version.VERSION_16)
    @JsonProperty("cryptoProperties")
    private CryptoProperties cryptoProperties;

    @VersionFilter(Version.VERSION_16)
    @JsonProperty("provides")
    private List<String> provides;

    @VersionFilter(Version.VERSION_16)
    @JsonUnwrapped
    private Tags tags;

    @VersionFilter(Version.VERSION_16)
    @JsonProperty("authors")
    private List<OrganizationalContact> authors;

    @VersionFilter(Version.VERSION_16)
    @JsonProperty("manufacturer")
    private OrganizationalEntity manufacturer;

    @JsonOnly
    @VersionFilter(Version.VERSION_14)
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
    @JsonDeserialize(using = HashesDeserializer.class)
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

    @JsonDeserialize(using = LicenseDeserializer.class)
    public LicenseChoice getLicenses() {
        return licenses;
    }

    @JacksonXmlElementWrapper (useWrapping = false)
    public void setLicenses(LicenseChoice licenses) {
        this.licenses = licenses;
    }

    @Deprecated
    public LicenseChoice getLicenseChoice() {
        return getLicenses();
    }

    @Deprecated
    @JsonIgnore
    public void setLicenseChoice(LicenseChoice licenseChoice) {
        setLicenses(licenseChoice);
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
    @JsonDeserialize(using = ExternalReferencesDeserializer.class)
    @VersionFilter(Version.VERSION_11)
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
    @JsonDeserialize(using = PropertiesDeserializer.class)
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

    public ModelCard getModelCard() {
        return modelCard;
    }

    public void setModelCard(final ModelCard modelCard) {
        this.modelCard = modelCard;
    }

    public ComponentData getData() {
        return data;
    }

    public void setData(final ComponentData data) {
        this.data = data;
    }

    @JacksonXmlElementWrapper(useWrapping = false)
    @VersionFilter(Version.VERSION_16)
    public List<String> getOmniborId() {
        return omniborId;
    }

    public void setOmniborId(final List<String> omniborId) {
        this.omniborId = omniborId;
    }

    @JacksonXmlElementWrapper(useWrapping = false)
    @VersionFilter(Version.VERSION_16)
    public List<String> getSwhid() {
        return swhid;
    }

    public void setSwhid(final List<String> swhid) {
        this.swhid = swhid;
    }

    public CryptoProperties getCryptoProperties() {
        return cryptoProperties;
    }

    public void setCryptoProperties(final CryptoProperties cryptoProperties) {
        this.cryptoProperties = cryptoProperties;
    }

    public List<String> getProvides() {
        return provides;
    }

    public void setProvides(final List<String> provides) {
        this.provides = provides;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(final Tags tags) {
        this.tags = tags;
    }

    public List<OrganizationalContact> getAuthors() {
        return authors;
    }

    public void setAuthors(final List<OrganizationalContact> authors) {
        this.authors = authors;
    }

    public OrganizationalEntity getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(final OrganizationalEntity manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, publisher, group, name, version, description, scope, hashes, licenses, copyright,
            cpe, purl, omniborId, swhid, swid, modified, components, evidence, releaseNotes, type, modelCard, data);
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
                Objects.equals(licenses, component.licenses) &&
                Objects.equals(copyright, component.copyright) &&
                Objects.equals(cpe, component.cpe) &&
                Objects.equals(purl, component.purl) &&
                Objects.equals(swid, component.swid) &&
                Objects.equals(swhid, component.swhid) &&
                Objects.equals(omniborId, component.omniborId) &&
                Objects.equals(components, component.components) &&
                Objects.equals(evidence, component.evidence) &&
                Objects.equals(mimeType, component.mimeType) &&
                Objects.equals(releaseNotes, component.releaseNotes) &&
                Objects.equals(data, component.data) &&
                Objects.equals(modelCard, component.modelCard) &&
                Objects.equals(type, component.type);
    }
}
