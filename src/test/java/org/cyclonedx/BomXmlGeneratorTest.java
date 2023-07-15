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
import org.cyclonedx.CycloneDxSchema.Version;
import org.cyclonedx.generators.xml.*;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.ExtensibleType;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
        BomXmlGenerator generator = BomGeneratorFactory.createXml(CycloneDxSchema.Version.VERSION_10, createCommonBom("/bom-1.0.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertTrue(generator instanceof BomXmlGenerator10);
        assertEquals(CycloneDxSchema.Version.VERSION_10, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, Version.VERSION_10));
    }

    @Test
    public void schema11WithDependencyGraphGenerationTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(CycloneDxSchema.Version.VERSION_11, createCommonBom("/bom-1.1-dependency-graph-1.0.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertTrue(generator instanceof BomXmlGenerator11);
        assertEquals(CycloneDxSchema.Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_11));
    }

    @Test
    public void schema11WithVulnerabilitiesGenerationTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(CycloneDxSchema.Version.VERSION_11, createCommonBom("/bom-1.1-vulnerability-1.0.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertTrue(generator instanceof BomXmlGenerator11);
        assertEquals(CycloneDxSchema.Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_11));
    }

    @Test
    public void schema11GenerationTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(CycloneDxSchema.Version.VERSION_11, createCommonBom());
        Document doc = generator.generate();
        testDocument(doc);

        assertTrue(generator instanceof BomXmlGenerator11);
        assertEquals(CycloneDxSchema.Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_11));
    }

    @Test
    public void schema12GenerationTestWith11Data() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(CycloneDxSchema.Version.VERSION_12, createCommonBom());
        Document doc = generator.generate();
        testDocument(doc);

        assertTrue(generator instanceof BomXmlGenerator12);
        assertEquals(CycloneDxSchema.Version.VERSION_12, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_12));
    }

    @Test
    public void schema12GenerationTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(CycloneDxSchema.Version.VERSION_12, createCommonBom("/bom-1.2.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertTrue(generator instanceof BomXmlGenerator12);
        assertEquals(CycloneDxSchema.Version.VERSION_12, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_12));
    }

    @Test
    public void schema12MultipleDependenciesXmlTest() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.2.json"));
        final JsonParser parser = new JsonParser();
        final Bom bom = parser.parse(bomBytes);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_12, bom);
        assertTrue(generator instanceof BomXmlGenerator12);
        assertEquals(CycloneDxSchema.Version.VERSION_12, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser xmlParser = new XmlParser();
        assertTrue(xmlParser.isValid(file, CycloneDxSchema.Version.VERSION_12));
    }

    @Test
    public void schema12GenerationWithPedigreeDataTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(CycloneDxSchema.Version.VERSION_12, createCommonBom("/bom-1.2-pedigree.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertTrue(generator instanceof BomXmlGenerator12);
        assertEquals(CycloneDxSchema.Version.VERSION_12, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_12));
    }

    @Test
    public void schema13GenerationTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(CycloneDxSchema.Version.VERSION_13, createCommonBom("/bom-1.3.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertTrue(generator instanceof BomXmlGenerator13);
        assertEquals(CycloneDxSchema.Version.VERSION_13, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_13));
    }

    @Test
    public void schema13MultipleDependenciesXmlTest() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.3.json"));
        final JsonParser parser = new JsonParser();
        final Bom bom = parser.parse(bomBytes);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_13, bom);
        assertTrue(generator instanceof BomXmlGenerator13);
        assertEquals(CycloneDxSchema.Version.VERSION_13, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser xmlParser = new XmlParser();
        assertTrue(xmlParser.isValid(file, CycloneDxSchema.Version.VERSION_13));
    }

    @Test
    public void schema14GenerationTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(CycloneDxSchema.Version.VERSION_14, createCommonBom("/bom-1.4.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertTrue(generator instanceof BomXmlGenerator14);
        assertEquals(CycloneDxSchema.Version.VERSION_14, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_14));
    }

    @Test
    public void schema14MultipleDependenciesXmlTest() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.4.json"));
        final JsonParser parser = new JsonParser();
        final Bom bom = parser.parse(bomBytes);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_14, bom);
        assertTrue(generator instanceof BomXmlGenerator14);
        assertEquals(CycloneDxSchema.Version.VERSION_14, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser xmlParser = new XmlParser();
        assertTrue(xmlParser.isValid(file, CycloneDxSchema.Version.VERSION_14));
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
        BomXmlGenerator generator = BomGeneratorFactory.createXml(CycloneDxSchema.Version.VERSION_11, bom);
        Document doc = generator.generate();

        testDocument(doc);
        assertTrue(generator instanceof BomXmlGenerator11);
        assertEquals(CycloneDxSchema.Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_11));
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

        BomXmlGenerator generator = BomGeneratorFactory.createXml(CycloneDxSchema.Version.VERSION_11, bom);
        Document doc = generator.generate();
        testDocument(doc);

        assertTrue(generator instanceof BomXmlGenerator11);
        assertEquals(CycloneDxSchema.Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_11));
    }

    @Test
    public void schema13EmptyComponentsXmlTest() throws Exception {
        final Bom bom =  new Bom();
        bom.setComponents(new ArrayList<>());
        bom.setDependencies(new ArrayList<>());
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_13, bom);
        assertTrue(generator instanceof BomXmlGenerator13);
        assertEquals(CycloneDxSchema.Version.VERSION_13, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_13));
    }

    @Test
    public void schema14JBomLinkGenerationTest() throws Exception {
        Bom bom = createCommonBom("/bom-1.4-bomlink.xml");
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_14, bom);
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_14));
        Bom bom2 = parser.parse(file);
        assertNotNull(bom2.getComponents().get(0).getExternalReferences());
        assertEquals("bom", bom2.getComponents().get(0).getExternalReferences().get(0).getType().getTypeName());
        assertEquals("urn:cdx:f08a6ccd-4dce-4759-bd84-c626675d60a7/1", bom2.getComponents().get(0).getExternalReferences().get(0).getUrl());
    }

    @Test
    public void schema15GenerationTest() throws Exception {
        BomXmlGenerator generator = BomGeneratorFactory.createXml(CycloneDxSchema.Version.VERSION_15, createCommonBom("/bom-1.5.xml"));
        Document doc = generator.generate();
        testDocument(doc);

        assertTrue(generator instanceof BomXmlGenerator15);
        assertEquals(CycloneDxSchema.Version.VERSION_15, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        XmlParser parser = new XmlParser();
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_15));
    }

    private File writeToFile(String xmlString) throws Exception {
        try (FileWriter writer = new FileWriter(tempFile.getAbsolutePath())) {
            writer.write(xmlString);
        }
        return tempFile;
    }

    private Bom createCommonBom() throws Exception {
        return createCommonBom("/bom-1.1.xml");
    }

    private Bom createCommonBom(String resource) throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream(resource));
        XmlParser parser = new XmlParser();
        return parser.parse(bomBytes);
    }

    private void testDocument(Document doc) {
        assertNotNull(doc);
        assertNotNull(documentToString(doc));
    }

    private String documentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            transformer = tf.newTransformer();

            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.getBuffer().toString();
        } catch (TransformerException ex) {
            return null;
        }
    }
}
