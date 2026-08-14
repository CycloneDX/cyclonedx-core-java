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
package org.cyclonedx.parsers;

import org.cyclonedx.Version;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.generators.BomGeneratorFactory;
import org.cyclonedx.model.Bom;
import org.cyclonedx.util.XmlFactoryUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.util.SetSystemProperty;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Verifies that BOM parsing, validation, and generation remain functional and XXE-safe when an
 * outdated XML parser (Xerces 2.x) is chosen instead of the JDK's built-in implementation.
 *
 * <p>CI runs on Java 9+ where {@link XmlFactoryUtils} always prefers the JDK factories over the
 * classpath lookup, so a leaked Xerces would never be instantiated. To exercise the Xerces code
 * path (including the XXE compensations) on every JVM, Xerces is forced via the explicit JAXP
 * system properties, which {@link XmlFactoryUtils} deliberately honors.</p>
 */
@SetSystemProperty(key = "javax.xml.parsers.DocumentBuilderFactory", value = "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl")
@SetSystemProperty(key = "javax.xml.parsers.SAXParserFactory", value = "org.apache.xerces.jaxp.SAXParserFactoryImpl")
@SetSystemProperty(key = "javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema", value = "org.apache.xerces.jaxp.validation.XMLSchemaFactory")
class XercesFallbackTest {

    @Test
    void factoriesShouldBeXerces() {
        // guards the premise of this test class: Xerces is actually instantiated
        assertThat(XmlFactoryUtils.newDocumentBuilderFactory().getClass().getName())
                .isEqualTo("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        assertThat(XmlFactoryUtils.newSAXParserFactory().getClass().getName())
                .isEqualTo("org.apache.xerces.jaxp.SAXParserFactoryImpl");
        assertThat(XmlFactoryUtils.newSchemaFactory().getClass().getName())
                .isEqualTo("org.apache.xerces.jaxp.validation.XMLSchemaFactory");
    }

    @Test
    void parseShouldWorkWithXerces() throws Exception {
        final Bom bom = new XmlParser().parse(resource("/bom-1.5.xml"));
        assertThat(bom.getSpecVersion()).isEqualTo("1.5");
        assertThat(bom.getComponents()).isNotEmpty();
    }

    @Test
    void validateShouldWorkWithXerces() throws Exception {
        final List<ParseException> validationFailures =
                new XmlParser().validate(resource("/bom-1.5.xml"), Version.VERSION_15);
        assertThat(validationFailures).isEmpty();
    }

    @Test
    void generateShouldWorkWithXerces() throws Exception {
        final Bom bom = new XmlParser().parse(resource("/bom-1.5.xml"));
        final org.w3c.dom.Document document =
                BomGeneratorFactory.createXml(Version.VERSION_15, bom).generate();
        assertThat(document).isNotNull();
    }

    @Test
    void parseShouldNotBeVulnerableToXxeWithXerces() throws Exception {
        final byte[] bomBytes = resource("/security/xxe-protection.xml");
        // the doctype must be rejected before any external entity can be resolved
        assertThatExceptionOfType(ParseException.class)
                .isThrownBy(() -> new XmlParser().parse(bomBytes))
                .withMessageContaining("DOCTYPE");
    }

    @Test
    void validateShouldNotBeVulnerableToXxeWithXerces() throws Exception {
        final List<ParseException> validationFailures =
                new XmlParser().validate(resource("/security/xxe-protection.xml"));
        assertThat(validationFailures).isNotEmpty();
        assertThat(validationFailures).extracting(Throwable::getMessage).allSatisfy(
                failureMessage -> assertThat(failureMessage).contains("DOCTYPE"));
    }

    private static byte[] resource(final String name) throws Exception {
        try (final InputStream inputStream = XercesFallbackTest.class.getResourceAsStream(name)) {
            assertThat(inputStream).isNotNull();
            return inputStream.readAllBytes();
        }
    }
}
