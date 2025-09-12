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

import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.generators.BomGeneratorFactory;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.xml.*;
import org.cyclonedx.model.Attribute;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Component.Type;
import org.cyclonedx.model.ExtensibleType;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.license.Expression;
import org.cyclonedx.model.metadata.ToolInformation;
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
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
    public void testIssue439Regression_xmlEmptyLicense() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = new Bom();
        bom.addComponent(getComponentWithEmptyLicenseChoice());

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        String xmlString = generator.toXmlString();

        assertFalse(xmlString.isEmpty());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(xmlString.getBytes(StandardCharsets.UTF_8)));
    }

    private static Component getComponentWithEmptyLicenseChoice() {
        Component component = new Component();
        component.setName("xalan");
        component.setType(Component.Type.LIBRARY);
        component.setLicenses(new LicenseChoice());
        component.setPurl("pkg:maven/xalan/xalan@2.6.0?type=jar");
        return component;
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
    public void schema16_testExpressions() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonJsonBom("/1.6/valid-license-expression-1.6.json");

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void schema16_testAttestations() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonJsonBom("/1.6/valid-attestation-1.6.json");

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void schema16_testAttestations_xml() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonBomXml("/1.6/valid-attestation-1.6.xml");
        addSignature(bom);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    private void addSignature(Bom bom) {
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("xmlns", "http://www.w3.org/2000/09/xmldsig#"));
        ExtensibleType signature = new ExtensibleType("ds", "Signature",  attributes, "");
        bom.getDeclarations().getAffirmation().getSignatories().get(0).addExtensibleType(signature);
    }

    @Test
    public void schema16_testVulnerabilities() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonJsonBom("/1.6/valid-vulnerability-1.6.json");

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

    @Test
    public void schema16_testFormulation() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonJsonBom("/1.6/valid-formulation-1.6.json");

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void schema16_testCompositions() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonJsonBom("/1.6/valid-compositions-1.6.json");

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void schema16_testCrypto() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonJsonBom("/1.6/valid-cryptography-full-1.6.json");

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void schema16_testML() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonJsonBom("/1.6/valid-machine-learning-1.6.json");

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
        Bom bom = new Bom();
        bom.setSerialNumber("urn:uuid:" + UUID.randomUUID());

        Metadata meta = new Metadata();

        // ToolInformation test
        Component tool1 = new Component();
        tool1.setType(Component.Type.APPLICATION);
        tool1.setName("TOOL 1");
        tool1.setVersion("v1");

        Component tool2 = new Component();
        tool2.setType(Component.Type.APPLICATION);
        tool2.setName("TOOL 2");
        tool2.setVersion("v2");

        ToolInformation tools = new ToolInformation();
        List<Component> components = new LinkedList<>();
        components.add(tool1);
        components.add(tool2);
        tools.setComponents(components);
        meta.setToolChoice(tools);

        // Author test
        OrganizationalContact auth1 = new OrganizationalContact();
        auth1.setName("Author 1");
        meta.addAuthor(auth1);

        OrganizationalContact auth2 = new OrganizationalContact();
        auth2.setName("Author 2");
        meta.addAuthor(auth2);

        bom.setMetadata(meta);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_16, bom);
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, Version.VERSION_16));
    }

    @Test
    public void testIssue562() throws Exception {
        Version version = Version.VERSION_15;
        Bom bom = createCommonBomXml("/regression/issue562.xml");

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        
        File loadedFile = writeToFile(generator.toXmlString());

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void testIssue571() throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonBomXml("/regression/issue571.xml");

        Component component = new Component();
        component.setName("test");
        component.setVersion("v2");
        component.setType(Type.APPLICATION);
        bom.getMetadata().getToolChoice().getComponents().add(component);

        BomJsonGenerator generator = BomGeneratorFactory.createJson(version, bom);
        File loadedFile = writeToFile(generator.toJsonString());

        JsonParser parser = new JsonParser();
        assertTrue(parser.isValid(loadedFile, version));
    }

    @Test
    public void testIssue492() throws Exception {
        Version version = Version.VERSION_15;
        Bom bom = createCommonBomXml("/regression/issue492.xml");

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

    @ParameterizedTest
    @ValueSource(strings = {
            "/1.6/valid-component-authors-1.6.xml",
            "/1.6/invalid-component-authors-legacy-1.6.xml"
    })
    public void testComponentAuthorsSerializationAndDeserialization(String xmlFilePath) throws Exception {
        Version version = Version.VERSION_16;
        Bom bom = createCommonBomXml(xmlFilePath);

        assertNotNull(bom.getComponents());
        assertEquals(1, bom.getComponents().size());

        Component bomComponent = bom.getComponents().get(0);
        assertEquals("Outer Author with String value", bomComponent.getAuthor());

        List<OrganizationalContact> bomAuthors = bomComponent.getAuthors();
        assertNotNull(bomAuthors);
        assertEquals(2, bomAuthors.size());

        OrganizationalContact bomAuthor1 = bomAuthors.get(0);
        OrganizationalContact bomAuthor2 = bomAuthors.get(1);

        assertNotNull(bomAuthor1);
        assertEquals("Test Author 1", bomAuthor1.getName());
        assertEquals("author1@example.com", bomAuthor1.getEmail());
        assertEquals("123", bomAuthor1.getPhone());

        assertNotNull(bomAuthor2);
        assertEquals("Test Author 2", bomAuthor2.getName());
        assertEquals("author2@example.com", bomAuthor2.getEmail());
        assertEquals("456", bomAuthor2.getPhone());

        BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
        String xmlString = generator.toXmlString();
        File loadedFile = writeToFile(xmlString);

        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(loadedFile, version));

        // Verify the xml content
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder()
                .parse(new java.io.ByteArrayInputStream(xmlString.getBytes()));

        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                return "bom".equals(prefix) ? "http://cyclonedx.org/schema/bom/1.6" : null;
            }
            @Override
            public String getPrefix(String namespaceURI) {
                return "http://cyclonedx.org/schema/bom/1.6".equals(namespaceURI) ? "bom" : null;
            }
            @Override
            public Iterator<String> getPrefixes(String namespaceURI) {
                return Collections.singleton("bom").iterator();
            }
        });

        NodeList authors = (NodeList) xpath.evaluate(
                "//bom:component/bom:authors/bom:author",
                doc,
                XPathConstants.NODESET
        );
        assertEquals(2, authors.getLength(), "There should be exactly 2 <author> elements");

        String author1 = xpath.evaluate("//bom:component/bom:authors/bom:author[1]/bom:name", doc);
        String author2 = xpath.evaluate("//bom:component/bom:authors/bom:author[2]/bom:name", doc);

        assertEquals("Test Author 1", author1);
        assertEquals("Test Author 2", author2);

        String email1 = xpath.evaluate("//bom:component/bom:authors/bom:author[1]/bom:email", doc);
        String email2 = xpath.evaluate("//bom:component/bom:authors/bom:author[2]/bom:email", doc);

        assertEquals("author1@example.com", email1);
        assertEquals("author2@example.com", email2);

        String phone1 = xpath.evaluate("//bom:component/bom:authors/bom:author[1]/bom:phone", doc);
        String phone2 = xpath.evaluate("//bom:component/bom:authors/bom:author[2]/bom:phone", doc);

        assertEquals("123", phone1);
        assertEquals("456", phone2);

        String outerAuthorStr = (String) xpath.evaluate("//bom:component/bom:author", doc, XPathConstants.STRING);
        assertEquals("Outer Author with String value", outerAuthorStr);
    }

    @Test
    public void testComponentAuthorsWithInvalidItemTag(){
        assertThrows(ParseException.class, () -> createCommonBomXml("/1.6/invalid-component-authors-bad-item-name-1.6.xml"));
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
