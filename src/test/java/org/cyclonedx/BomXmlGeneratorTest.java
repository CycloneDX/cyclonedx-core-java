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
package org.cyclonedx;

import org.apache.commons.io.IOUtils;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.generators.BomGeneratorFactory;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.xml.*;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.ExtensibleType;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.license.Expression;
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class BomXmlGeneratorTest {

    private File tempFile;

    @BeforeEach
    public void before() throws IOException {
        Path path = Files.createTempDirectory("cyclonedx-core-java");
        this.tempFile = new File(path.toFile(), "bom.xml");
    }

    @AfterEach
    public void after() {
        tempFile.delete();
        tempFile.getParentFile().delete();
    }

    @Test
    public void schema10GenerationTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_10, createCommonBomXml("/bom-1.0.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertEquals(Version.VERSION_10, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, Version.VERSION_10));
    }

    @Test
    public void schema11WithDependencyGraphGenerationTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_11, createCommonBomXml("/bom-1.1-dependency-graph-1.0.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertEquals(Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, Version.VERSION_11));
    }

    @Test
    public void schema11WithVulnerabilitiesGenerationTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_11, createCommonBomXml("/bom-1.1-vulnerability-1.0.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertEquals(Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, Version.VERSION_11));
    }

    @Test
    public void schema11GenerationTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_11, createCommonBomXml("/bom-1.1.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertEquals(Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, Version.VERSION_11));
    }

    @Test
    public void schema12GenerationTestWith11Data() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_12, createCommonBomXml("/bom-1.2.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertEquals(Version.VERSION_12, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, Version.VERSION_12));
    }

    @Test
    public void schema12GenerationTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_12, createCommonJsonBom("/bom-1.2.json"));
        Document doc = generator.generate();
        testDocument(doc);

        assertEquals(Version.VERSION_12, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, Version.VERSION_12));
    }

    @Test
    public void schema12MultipleDependenciesXmlTest() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(
            Objects.requireNonNull(this.getClass().getResourceAsStream("/bom-1.2.json")));
        final JsonParser parser = new JsonParser();
        final Bom bom = parser.parse(bomBytes);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_12, bom);
        assertEquals(Version.VERSION_12, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser xmlParser = new XmlParser();
        assertTrue(xmlParser.isValid(file, Version.VERSION_12));
    }

    @Test
    public void schema12GenerationWithPedigreeDataTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_12, createCommonBomXml("/bom-1.2-pedigree.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertEquals(Version.VERSION_12, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, Version.VERSION_12));
    }

    static Stream<Arguments> testData() {
        return Stream.of(
            Arguments.of(Version.VERSION_16, "/1.6/valid-bom-1.6.json"),
            Arguments.of(Version.VERSION_15, "/bom-1.5.json"),
            Arguments.of(Version.VERSION_14, "/bom-1.4.json"),
            Arguments.of(Version.VERSION_13, "/bom-1.3.json")
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void testXmlGeneration(Version version, String bomXmlPath)
        throws Exception
    {
        Bom bom = createCommonJsonBom(bomXmlPath);
        BomJsonGenerator generator = BomGeneratorFactory.createJson(version, bom);

        assertEquals(version, generator.getSchemaVersion());

        File file = writeToFile(generator.toJsonString());
        JsonParser parser = new JsonParser();
        assertTrue(parser.isValid(file, version));
    }

    @Test
    public void schema13MultipleDependenciesXmlTest() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(
            Objects.requireNonNull(this.getClass().getResourceAsStream("/bom-1.3.json")));
        final JsonParser parser = new JsonParser();
        final Bom bom = parser.parse(bomBytes);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_13, bom);
        assertEquals(Version.VERSION_13, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser xmlParser = new XmlParser();
        assertTrue(xmlParser.isValid(file, Version.VERSION_13));
    }

    @Test
    public void schema14MultipleDependenciesXmlTest() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(
            Objects.requireNonNull(this.getClass().getResourceAsStream("/bom-1.4.json")));
        final JsonParser parser = new JsonParser();
        final Bom bom = parser.parse(bomBytes);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_14, bom);
        assertEquals(Version.VERSION_14, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser xmlParser = new XmlParser();
        assertTrue(xmlParser.isValid(file, Version.VERSION_14));
    }

    @Test
    public void invalidUrlTest() throws Exception {
        Component c = new Component();
        c.setName("Component-A");
        c.setType(Component.Type.LIBRARY);
        c.setVersion("1.0.0");

        License l = new License();
        l.setId("Apache-2.0");
        l.setUrl("jklr3qm,lafuio43,a,fjal"); // invalid URL
        LicenseChoice lc = new LicenseChoice();
        lc.addLicense(l);
        c.setLicenseChoice(lc);

        ExternalReference r = new ExternalReference();
        r.setType(ExternalReference.Type.MAILING_LIST);
        r.setUrl("http://www.mail-archive.com/dev%karaf.apache.org/"); // invalid URL - violates rfc
        c.addExternalReference(r);

        Bom bom = new Bom();
        bom.addComponent(c);
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_11, bom);
        Document doc = generator.generate();

        testDocument(doc);
        assertEquals(Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, Version.VERSION_11));
    }

    @Test
    public void extensionPointTest() throws Exception {
        Component c = new Component();
        c.setName("Component-A");
        c.setType(Component.Type.LIBRARY);
        c.setVersion("1.0.0");

        c.addExtensibleType(new ExtensibleType("foo", "comp-bar", "my-comp-value"));

        License l = new License();
        l.setId("Apache-2.0");
        ExtensibleType et = new ExtensibleType("ort", "origin", "license-scanner");
        l.addExtensibleType(et);
        LicenseChoice lc = new LicenseChoice();
        lc.addLicense(l);
        c.setLicenseChoice(lc);

        Bom bom = new Bom();
        bom.addComponent(c);
        ExtensibleType t1 = new ExtensibleType("foo", "bom-bar-1");
        ExtensibleType t2 = new ExtensibleType("foo", "bom-bar-2");
        ExtensibleType t3 = new ExtensibleType("foo", "bom-bar-3", "my-foo-3-value");
        t2.addExtensibleType(t3);
        t1.addExtensibleType(t2);
        bom.addExtensibleType(t1);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_11, bom);
        Document doc = generator.generate();
        testDocument(doc);

        assertEquals(Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, Version.VERSION_11));
    }

    @Test
    public void schema13EmptyComponentsXmlTest() throws Exception {
        final Bom bom =  new Bom();
        bom.setComponents(new ArrayList<>());
        bom.setDependencies(new ArrayList<>());
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_13, bom);
        assertEquals(Version.VERSION_13, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, Version.VERSION_13));
    }

    @Test
    public void schema14JBomLinkGenerationTest() throws Exception {
        Bom bom = createCommonJsonBom("/bom-1.4-bomlink.json");
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_14, bom);
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, Version.VERSION_14));
        Bom bom2 = parser.parse(file);
        assertNotNull(bom2.getComponents().get(0).getExternalReferences());
        assertEquals("bom", bom2.getComponents().get(0).getExternalReferences().get(0).getType().getTypeName());
        assertEquals("urn:cdx:f08a6ccd-4dce-4759-bd84-c626675d60a7/1", bom2.getComponents().get(0).getExternalReferences().get(0).getUrl());
    }

    @Test
    public void testIssue408Regression_1_5() throws Exception {
        Version version = Version.VERSION_15;
        Bom bom = createCommonBomXml("/regression/issue408-1.5.xml");
        assertLicenseInformation(bom, version);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void testIssue408Regression_16To15() throws Exception {
        Version version = Version.VERSION_15;
        Bom bom = createCommonBomXml("/regression/issue408.xml");

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void testIssue408Regression_16To14() throws Exception {
        Version version = Version.VERSION_14;
        Bom bom = createCommonBomXml("/regression/issue408.xml");

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void testIssue408Regression() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonBomXml("/regression/issue408.xml");
        assertLicenseInformation(bom, version);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void testIssue408Regression_jsonToXml() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonJsonBom("/regression/issue408.json");
        assertLicenseInformation(bom, version);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void schema16_testEvidence() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonJsonBom("/1.6/valid-evidence-1.6.json");

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void schema15_testEvidence() throws Exception {
        Version version = Version.VERSION_15;
        Bom bom = createCommonJsonBom("/1.5/valid-evidence-1.5_2.json");

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    private void assertLicenseInformation(Bom bom, Version version) {

        //First Component
        Component component = bom.getComponents().get(0);
        assertNotNull(component);
        assertNotNull(component.getLicenseChoice());
        assertNotNull(component.getLicenses());
        assertNotNull(component.getLicenses().getLicenses());
        assertFalse(component.getLicenses().getLicenses().isEmpty());
        assertNull(component.getLicenses().getExpression());

        License license1 = component.getLicenses().getLicenses().get(0);
        assertNotNull(license1);
        assertNotNull(license1.getId());
        assertNull(license1.getName());

        if(version.getVersion() >= Version.VERSION_16.getVersion()) {
            assertNotNull(license1.getAcknowledgement());
        } else {
            assertNull(license1.getAcknowledgement());
        }
        assertNotNull(license1.getBomRef());

        License license2 = component.getLicenses().getLicenses().get(1);
        assertNotNull(license2);
        assertNotNull(license2.getName());
        assertNull(license2.getId());
        assertNull(license2.getAcknowledgement());
        assertNull(license2.getBomRef());

        //Second Component
        Component component2 = bom.getComponents().get(1);
        assertNotNull(component2);
        assertNotNull(component2.getLicenseChoice());
        assertNotNull(component2.getLicenses());
        assertNull(component2.getLicenses().getLicenses());
        assertNotNull(component2.getLicenses().getExpression());

        Expression expression = component2.getLicenses().getExpression();
        assertNotNull(expression.getValue());
        if(version.getVersion() >= Version.VERSION_16.getVersion()) {
            assertNotNull(expression.getAcknowledgement());
        } else {
            assertNull(expression.getAcknowledgement());
        }
        assertNotNull(expression.getBomRef());

        //Third Component Evidence
        Component component3 = bom.getComponents().get(2);
        assertNotNull(component3);
        LicenseChoice lcEvidence = component3.getEvidence().getLicenses();
        assertNotNull(lcEvidence);
        assertNotNull(lcEvidence.getLicenses());
        assertFalse(lcEvidence.getLicenses().isEmpty());
        assertNull(lcEvidence.getExpression());

        License license4 = lcEvidence.getLicenses().get(0);
        assertNotNull(license4);
        assertNotNull(license4.getId());
        assertNull(license4.getName());
        assertNull(license4.getAcknowledgement());
        assertNull(license4.getBomRef());
        assertNotNull(license4.getUrl());

        License license5 = lcEvidence.getLicenses().get(1);
        assertNotNull(license5);
        assertNotNull(license5.getId());
        assertNull(license5.getName());
        assertNull(license5.getAcknowledgement());
        assertNull(license5.getBomRef());
        assertNotNull(license5.getUrl());

        //Services
        Service service = bom.getServices().get(0);
        assertNotNull(service);
        LicenseChoice lcService = service.getLicenses();
        assertNotNull(lcService);
        assertNotNull(lcService.getLicenses());
        assertFalse(lcService.getLicenses().isEmpty());
        assertNull(lcService.getExpression());

        License license6 = lcService.getLicenses().get(0);
        assertNotNull(license6);
        assertNull(license6.getId());
        assertNotNull(license6.getName());
        assertNull(license6.getAcknowledgement());
        assertNull(license6.getBomRef());
        assertNull(license6.getUrl());

        License license7 = lcService.getLicenses().get(1);
        assertNotNull(license7);
        assertNull(license7.getId());
        assertNotNull(license7.getName());
        assertNull(license7.getAcknowledgement());
        assertNull(license7.getBomRef());
        assertNull(license7.getUrl());


        //Metadata
        Metadata metadata = bom.getMetadata();
        assertNotNull(metadata);
        assertNotNull(metadata.getLicenseChoice());
        assertNotNull(metadata.getLicenses());
        assertNotNull(metadata.getLicenses().getLicenses());
        assertFalse(metadata.getLicenses().getLicenses().isEmpty());
        assertNull(metadata.getLicenses().getExpression());

        License license8 = metadata.getLicenses().getLicenses().get(0);
        assertNotNull(license8);
        assertNotNull(license8.getId());
        assertNull(license8.getName());
        assertNull(license8.getAcknowledgement());
        assertNull(license8.getBomRef());

        License license9 = metadata.getLicenses().getLicenses().get(1);
        assertNotNull(license9);
        assertNotNull(license9.getName());
        assertNull(license9.getId());
        assertNull(license9.getAcknowledgement());
        assertNull(license9.getBomRef());
    }

    @Test
    public void testIssue408Regression_externalReferenceBom() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonBomXml("/regression/issue408-external-reference.xml");
        assertExternalReferenceInfo(bom);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void testXxeProtection() {
        assertThrows(ParseException.class, () -> {
            createCommonBomXml("/security/xxe-protection.xml");
        });
    }

    @Test
    public void testIssue408Regression_extensibleTypes() throws Exception {
        Version version = Version.VERSION_15;
        Bom bom = createCommonBomXml("/regression/issue408-extensible-type.xml");
        addExtensibleTypes(bom);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    private void addExtensibleTypes(Bom bom) {
        ExtensibleType t1 = new ExtensibleType("abc", "test", "test");
        ExtensibleType t2 = new ExtensibleType("abc", "test", "test1");

        bom.getComponents().get(0).getLicenses().getLicenses().get(0).addExtensibleType(t1);
        bom.getComponents().get(0).getLicenses().getLicenses().get(1).addExtensibleType(t2);

        ExtensibleType t3 = new ExtensibleType("abc", "info", "test");
        bom.getComponents().get(0).addExtensibleType(t3);
    }

    @Test
    public void testIssue408Regression_jsonToXml_externalReferenceBom() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonJsonBom("/regression/issue408-external-reference.json");
        assertExternalReferenceInfo(bom);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    private void assertExternalReferenceInfo(Bom bom) {
        assertEquals(3, bom.getExternalReferences().size());
        assertEquals(3, bom.getComponents().get(0).getExternalReferences().size());
    }

    private File writeToFile(String xmlString) throws Exception {
        try (FileWriter writer = new FileWriter(tempFile.getAbsolutePath())) {
            writer.write(xmlString);
        }
        return tempFile;
    }

    private Bom createCommonBomXml(String resource) throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(
            Objects.requireNonNull(this.getClass().getResourceAsStream(resource)));
        XmlParser parser = new XmlParser();
        return parser.parse(bomBytes);
    }

    private Bom createCommonJsonBom(String resource) throws Exception {
        final byte[] bomBytes =
            IOUtils.toByteArray(Objects.requireNonNull(this.getClass().getResourceAsStream(resource)));
        JsonParser parser = new JsonParser();
        return parser.parse(bomBytes);
    }

    private void testDocument(Document doc) {
        assertNotNull(doc);
        assertNotNull(doc.toString());
    }
}
