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
package org.cyclonedx.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.UUID;

import org.cyclonedx.BomGeneratorFactory;
import org.cyclonedx.CycloneDxSchema.Version;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.BomReference;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Composition;
import org.cyclonedx.model.Composition.Aggregate;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.vulnerability.Vulnerability;
import org.cyclonedx.model.vulnerability.Vulnerability.Affect;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BomMergerTest {

    @Test
    public void hierarchicalMergeComponentsTest() throws Exception {
        Component subject = new Component();
        subject.setName("Thing");
        subject.setVersion("1");

        Component sbom1System1 = new Component();
        sbom1System1.setName("System1");
        sbom1System1.setVersion("1");
        sbom1System1.setBomRef("System1@1");

        Metadata sbom1Metadata = new Metadata();
        sbom1Metadata.setComponent(sbom1System1);

        Component sbom1Component1 = new Component();
        sbom1Component1.setName("Component1");
        sbom1Component1.setVersion("1");
        sbom1Component1.setBomRef("Component1@1");

        Dependency sbom1Dependency1 = new Dependency("System1@1");
        sbom1Dependency1.addDependency(new Dependency("Component1@1"));

        Composition sbom1Composition1 = new Composition();
        sbom1Composition1.setAggregate(Aggregate.COMPLETE);
        sbom1Composition1.addAssembly(new BomReference("System1@1"));
        sbom1Composition1.addDependency(new BomReference("System1@1"));

        Bom sbom1 = new Bom();
        sbom1.setSerialNumber("urn:uuid:" + UUID.randomUUID());
        sbom1.setMetadata(sbom1Metadata);
        sbom1.addComponent(sbom1Component1);
        sbom1.addDependency(sbom1Dependency1);
        sbom1.addComposition(sbom1Composition1);

        Component sbom2System2 = new Component();
        sbom2System2.setName("System2");
        sbom2System2.setVersion("1");
        sbom2System2.setBomRef("System2@1");

        Metadata sbom2Metadata = new Metadata();
        sbom2Metadata.setComponent(sbom2System2);

        Component sbom2Component2 = new Component();
        sbom2Component2.setName("Component2");
        sbom2Component2.setVersion("1");
        sbom2Component2.setBomRef("Component2@1");

        Dependency sbom2Dependency1 = new Dependency("System2@1");
        sbom2Dependency1.addDependency(new Dependency("Component2@1"));

        Composition sbom2Composition1 = new Composition();
        sbom2Composition1.setAggregate(Aggregate.COMPLETE);
        sbom2Composition1.addAssembly(new BomReference("System2@1"));
        sbom2Composition1.addDependency(new BomReference("System2@1"));

        Bom sbom2 = new Bom();
        sbom2.setSerialNumber("urn:uuid:" + UUID.randomUUID());
        sbom2.setMetadata(sbom2Metadata);
        sbom2.addComponent(sbom2Component2);
        sbom2.addDependency(sbom2Dependency1);
        sbom2.addComposition(sbom2Composition1);

        Bom result = new BomMerger().hierarchicalMerge(Arrays.asList(sbom1, sbom2), subject);
        //System.out.println(BomGeneratorFactory.createJson(Version.VERSION_14, result).toJsonString());

        JsonNode actualResult = BomGeneratorFactory.createJson(Version.VERSION_14, result).toJsonNode();

        JsonNode expectedResult = new ObjectMapper()
                .readTree(getClass().getResourceAsStream("/merge/components-test.json"));

        assertBomsEqual(actualResult, expectedResult);
    }

    @Test
    public void hierarchicalMergeVulnerabilitiesTest() throws Exception {
        Component subject = new Component();
        subject.setName("Thing");
        subject.setVersion("1");

        Component sbom1System1 = new Component();
        sbom1System1.setName("System1");
        sbom1System1.setVersion("1");
        sbom1System1.setBomRef("System1@1");

        Metadata sbom1Metadata = new Metadata();
        sbom1Metadata.setComponent(sbom1System1);

        Affect sbom1Affect1 = new Affect();
        sbom1Affect1.setRef("ref1");

        Vulnerability sbom1Vulnerability1 = new Vulnerability();
        sbom1Vulnerability1.setId("cve1");
        sbom1Vulnerability1.setAffects(Arrays.asList(sbom1Affect1));

        Bom sbom1 = new Bom();
        sbom1.setSerialNumber("urn:uuid:" + UUID.randomUUID());
        sbom1.setMetadata(sbom1Metadata);
        sbom1.addVulnerability(sbom1Vulnerability1);

        Component sbom2System2 = new Component();
        sbom2System2.setName("System2");
        sbom2System2.setVersion("1");
        sbom2System2.setBomRef("System2@1");

        Metadata sbom2Metadata = new Metadata();
        sbom2Metadata.setComponent(sbom2System2);

        Affect sbom2Affect1 = new Affect();
        sbom2Affect1.setRef("ref2");

        Vulnerability sbom2Vulnerability1 = new Vulnerability();
        sbom2Vulnerability1.setId("cve2");
        sbom2Vulnerability1.setAffects(Arrays.asList(sbom2Affect1));

        Bom sbom2 = new Bom();
        sbom2.setSerialNumber("urn:uuid:" + UUID.randomUUID());
        sbom2.setMetadata(sbom2Metadata);
        sbom2.addVulnerability(sbom2Vulnerability1);

        Bom result = new BomMerger().hierarchicalMerge(Arrays.asList(sbom1, sbom2), subject);
        //System.out.println(BomGeneratorFactory.createJson(Version.VERSION_14, result).toJsonString());

        JsonNode actualResult = BomGeneratorFactory.createJson(Version.VERSION_14, result).toJsonNode();

        JsonNode expectedResult = new ObjectMapper()
                .readTree(getClass().getResourceAsStream("/merge/vulnerabilities-test.json"));

        assertBomsEqual(actualResult, expectedResult);
    }

    private void assertBomsEqual(JsonNode actual, JsonNode expected) {
        prepareBomForAssertion(actual);
        prepareBomForAssertion(expected);
        assertThat(actual).isEqualTo(expected);
    }

    private void prepareBomForAssertion(JsonNode bomTree) {
        ((ObjectNode) bomTree.path("metadata")).remove("timestamp");
    }

}
