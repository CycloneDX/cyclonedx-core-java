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
import org.cyclonedx.model.vulnerability.Vulnerability;
import org.cyclonedx.util.VulnerabilityDeserializer;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"aggregate", "assemblies", "dependencies", "vulnerabilities"})
public class Composition {

    public enum Aggregate {
        @JsonProperty("complete")
        COMPLETE("complete"),
        @JsonProperty("incomplete")
        INCOMPLETE("incomplete"),
        @JsonProperty("incomplete_first_party_only")
        INCOMPLETE_FIRST_PARTY_ONLY("incomplete_first_party_only"),
        @JsonProperty("incomplete_first_party_proprietary_only")
        INCOMPLETE_FIRST_PARTY_PROPRIETARY_ONLY("incomplete_first_party_proprietary_only"),
        @JsonProperty("incomplete_first_party_opensource_only")
        INCOMPLETE_FIRST_PARTY_OPENSOURCE_ONLY("incomplete_first_party_opensource_only"),
        @JsonProperty("incomplete_third_party_only")
        INCOMPLETE_THIRD_PARTY_ONLY("incomplete_third_party_only"),
        @JsonProperty("incomplete_third_party_proprietary_only")
        INCOMPLETE_THIRD_PARTY_PROPRIETARY_ONLY("incomplete_third_party_proprietary_only"),
        @JsonProperty("incomplete_third_party_opensource_only")
        INCOMPLETE_THIRD_PARTY_OPENSOURCE_ONLY("incomplete_third_party_opensource_only"),
        @JsonProperty("unknown")
        UNKNOWN("unknown"),
        @JsonProperty("not_specified")
        NOT_SPECIFIED("not_specified");

        private final String name;

        public String getAggregateName() {
            return this.name;
        }

        Aggregate(String name) {
            this.name = name;
        }
    }

    @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
    @JsonProperty("bom-ref")
    @VersionFilter(versions = {"1.0", "1.1", "1.2", "1.3", "1.4"})
    private String bomRef;

    private Aggregate aggregate;
    private List<BomReference> assemblies;
    private List<BomReference> dependencies;

    @VersionFilter(versions = {"1.0", "1.1", "1.2", "1.3", "1.5"})
    @JsonDeserialize(using = VulnerabilityDeserializer.class)
    private List<Vulnerability> vulnerabilities;

    public String getBomRef() {
        return bomRef;
    }

    public void setBomRef(String bomRef) {
        this.bomRef = bomRef;
    }

    public Aggregate getAggregate() {
        return aggregate;
    }

    public void setAggregate(Aggregate aggregate) {
        this.aggregate = aggregate;
    }

    @JacksonXmlElementWrapper(localName = "assemblies")
    @JacksonXmlProperty(localName = "assembly")
    public List<BomReference> getAssemblies() {
        return assemblies;
    }

    public void setAssemblies(List<BomReference> assemblies) {
        this.assemblies = assemblies;
    }

    public void addAssembly(BomReference assembly) {
        if (assemblies == null) {
            assemblies = new ArrayList<>();
        }
        assemblies.add(assembly);
    }

    @JacksonXmlElementWrapper(localName = "dependencies")
    @JacksonXmlProperty(localName = "dependency")
    public List<BomReference> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<BomReference> dependencies) {
        this.dependencies = dependencies;
    }

    public void addDependency(BomReference dependency) {
        if (dependencies == null) {
            dependencies = new ArrayList<>();
        }
        dependencies.add(dependency);
    }

    @JacksonXmlElementWrapper(localName = "vulnerabilities")
    @JacksonXmlProperty(localName = "vulnerability")
    public List<Vulnerability> getVulnerabilities() { return vulnerabilities; }

    public void setVulnerabilities(List<Vulnerability> vulnerabilities) { this.vulnerabilities = vulnerabilities; }
}
