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

import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.vulnerability.Vulnerability;
import org.cyclonedx.parsers.BomParserFactory;
import org.cyclonedx.parsers.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.commons.io.IOUtils.resourceToByteArray;
import static org.assertj.core.api.Assertions.assertThat;

class ObjectLocatorTest {

    private Bom bom;

    @BeforeEach
    void beforeEach() throws Exception {
        final byte[] bomBytes = resourceToByteArray("/bom-object-locator.json");
        final Parser parser = BomParserFactory.createParser(bomBytes);
        bom = parser.parse(bomBytes);
    }

    @Test
    void shouldLocateMetadataComponent() {
        final ObjectLocator objectLocator = new ObjectLocator(bom, "0a7ac0b2-0f52-45c6-8f73-4dbcfb25286b").locate();
        assertThat(objectLocator.found()).isTrue();
        assertThat(objectLocator.isMetadataComponent()).isTrue();
        assertThat(objectLocator.isComponent()).isTrue();
        assertThat(objectLocator.isService()).isFalse();
        assertThat(objectLocator.isVulnerability()).isFalse();
        assertThat(objectLocator.getObject()).isInstanceOf(Component.class);
    }

    @Test
    void shouldLocateComponent() {
        final ObjectLocator objectLocator = new ObjectLocator(bom, "ac9c4a17-2bc2-42ef-81af-01a8e363501f").locate();
        assertThat(objectLocator.found()).isTrue();
        assertThat(objectLocator.isMetadataComponent()).isFalse();
        assertThat(objectLocator.isComponent()).isTrue();
        assertThat(objectLocator.isService()).isFalse();
        assertThat(objectLocator.isVulnerability()).isFalse();
        assertThat(objectLocator.getObject()).isInstanceOf(Component.class);
    }

    @Test // https://github.com/CycloneDX/cyclonedx-core-java/issues/455
    void shouldLocateComponentWhenMetadataComponentIsNull() {
        bom.getMetadata().setComponent(null);

        final ObjectLocator objectLocator = new ObjectLocator(bom, "ac9c4a17-2bc2-42ef-81af-01a8e363501f").locate();
        assertThat(objectLocator.found()).isTrue();
        assertThat(objectLocator.isMetadataComponent()).isFalse();
        assertThat(objectLocator.isComponent()).isTrue();
        assertThat(objectLocator.isService()).isFalse();
        assertThat(objectLocator.isVulnerability()).isFalse();
        assertThat(objectLocator.getObject()).isInstanceOf(Component.class);
    }

    @Test
    void shouldLocateService() {
        final ObjectLocator objectLocator = new ObjectLocator(bom, "b2a46a4b-8367-4bae-9820-95557cfe03a8").locate();
        assertThat(objectLocator.found()).isTrue();
        assertThat(objectLocator.isMetadataComponent()).isFalse();
        assertThat(objectLocator.isComponent()).isFalse();
        assertThat(objectLocator.isService()).isTrue();
        assertThat(objectLocator.isVulnerability()).isFalse();
        assertThat(objectLocator.getObject()).isInstanceOf(Service.class);
    }

    @Test // https://github.com/CycloneDX/cyclonedx-core-java/issues/455
    void shouldLocateServiceWhenMetadataComponentIsNull() {
        bom.getMetadata().setComponent(null);

        final ObjectLocator objectLocator = new ObjectLocator(bom, "b2a46a4b-8367-4bae-9820-95557cfe03a8").locate();
        assertThat(objectLocator.found()).isTrue();
        assertThat(objectLocator.isMetadataComponent()).isFalse();
        assertThat(objectLocator.isComponent()).isFalse();
        assertThat(objectLocator.isService()).isTrue();
        assertThat(objectLocator.isVulnerability()).isFalse();
        assertThat(objectLocator.getObject()).isInstanceOf(Service.class);
    }

    @Test
    void shouldLocateVulnerability() {
        final ObjectLocator objectLocator = new ObjectLocator(bom, "6eee14da-8f42-4cc4-bb65-203235f02415").locate();
        assertThat(objectLocator.found()).isTrue();
        assertThat(objectLocator.isMetadataComponent()).isFalse();
        assertThat(objectLocator.isComponent()).isFalse();
        assertThat(objectLocator.isService()).isFalse();
        assertThat(objectLocator.isVulnerability()).isTrue();
        assertThat(objectLocator.getObject()).isInstanceOf(Vulnerability.class);
    }

    @Test // https://github.com/CycloneDX/cyclonedx-core-java/issues/455
    void shouldLocateVulnerabilityWhenMetadataComponentIsNull() {
        bom.getMetadata().setComponent(null);

        final ObjectLocator objectLocator = new ObjectLocator(bom, "6eee14da-8f42-4cc4-bb65-203235f02415").locate();
        assertThat(objectLocator.found()).isTrue();
        assertThat(objectLocator.isMetadataComponent()).isFalse();
        assertThat(objectLocator.isComponent()).isFalse();
        assertThat(objectLocator.isService()).isFalse();
        assertThat(objectLocator.isVulnerability()).isTrue();
        assertThat(objectLocator.getObject()).isInstanceOf(Vulnerability.class);
    }

    @Test
    void shouldNotFailWhenUnableToLocate() {
        final ObjectLocator objectLocator = new ObjectLocator(bom, "doesNotExist").locate();
        assertThat(objectLocator.found()).isFalse();
        assertThat(objectLocator.isMetadataComponent()).isFalse();
        assertThat(objectLocator.isComponent()).isFalse();
        assertThat(objectLocator.isService()).isFalse();
        assertThat(objectLocator.isVulnerability()).isFalse();
        assertThat(objectLocator.getObject()).isNull();
    }

}