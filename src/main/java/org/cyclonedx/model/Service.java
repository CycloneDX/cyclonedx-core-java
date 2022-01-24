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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.util.LicenseDeserializer;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "provider",
        "group",
        "name",
        "version",
        "description",
        "endpoints",
        "authenticated",
        "xTrustBoundary",
        "data",
        "licenses",
        "externalReferences",
        "properties",
        "services",
        "signature"
})
public class Service extends ExtensibleElement {

    @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
    @JsonProperty("bom-ref")
    private String bomRef;
    private OrganizationalEntity provider;
    private String group;
    private String name;
    private String version;
    private String description;
    private List<String> endpoints;
    private Boolean authenticated;
    @JacksonXmlProperty(localName = "x-trust-boundary")
    @JsonProperty("x-trust-boundary")
    private Boolean xTrustBoundary;
    private List<ServiceData> data;
    private LicenseChoice license;
    private List<ExternalReference> externalReferences;
    @VersionFilter(versions = {"1.3"})
    private List<Property> properties;
    private List<Service> services;
    @JsonOnly
    @VersionFilter(versions = {"1.4"})
    private Signature signature;

    public String getBomRef() {
        return bomRef;
    }

    public void setBomRef(String bomRef) {
        this.bomRef = bomRef;
    }

    public OrganizationalEntity getProvider() {
        return provider;
    }

    public void setProvider(OrganizationalEntity provider) {
        this.provider = provider;
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

    @JacksonXmlElementWrapper(localName = "endpoints")
    @JacksonXmlProperty(localName = "endpoint")
    public List<String> getEndpoints() {
        return endpoints;
    }

    public void addEndpoint(String endpoint) {
        if (this.endpoints == null) {
            this.endpoints = new ArrayList<>();
        }
        this.endpoints.add(endpoint);
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Boolean getxTrustBoundary() {
        return xTrustBoundary;
    }

    public void setxTrustBoundary(Boolean xTrustBoundary) {
        this.xTrustBoundary = xTrustBoundary;
    }

    @JacksonXmlElementWrapper(localName = "data")
    @JacksonXmlProperty(localName = "classification")
    public List<ServiceData> getData() {
        return data;
    }

    public void addServiceData(ServiceData data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(data);
    }

    public void setData(List<ServiceData> data) {
        this.data = data;
    }

    @JacksonXmlProperty(localName = "licenses")
    @JsonProperty("licenses")
    @JsonDeserialize(using = LicenseDeserializer.class)
    public LicenseChoice getLicense() {
        return license;
    }

    public void setLicense(LicenseChoice license) {
        this.license = license;
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

    @JacksonXmlElementWrapper(localName = "services")
    @JacksonXmlProperty(localName = "service")
    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public Signature getSignature() { return signature; }

    public void setSignature(Signature signature) { this.signature = signature; }
}
