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
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.cyclonedx;

import org.apache.commons.io.IOUtils;
import org.cyclonedx.model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BomGeneratorTest {

    private File tempFile;

    @Before
    public void before() throws IOException {
        Path path = Files.createTempDirectory("cyclonedx-core-java");
        this.tempFile = new File(path.toFile(), "bom.xml");
    }

    @After
    public void after() {
        tempFile.delete();
        tempFile.getParentFile().delete();
    }

    @Test
    public void schema10GenerationTest() throws Exception {
        BomGenerator generator = BomGeneratorFactory.create(CycloneDxSchema.Version.VERSION_10, createCommonBom());
        generator.generate();
        Assert.assertTrue(generator instanceof BomGenerator10);
        Assert.assertEquals(CycloneDxSchema.Version.VERSION_10, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        BomParser parser = new BomParser();
        Assert.assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_10));
    }

    @Test
    public void schema11GenerationTest() throws Exception {
        BomGenerator generator = BomGeneratorFactory.create(CycloneDxSchema.Version.VERSION_11, createCommonBom());
        generator.generate();
        Assert.assertTrue(generator instanceof BomGenerator11);
        Assert.assertEquals(CycloneDxSchema.Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        BomParser parser = new BomParser();
        Assert.assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_11));
    }

    @Test
    public void schema11WithDependencyGraphGenerationTest() throws Exception {
        BomGenerator generator = BomGeneratorFactory.create(CycloneDxSchema.Version.VERSION_11, createCommonBom("/bom-1.1-dependency-graph-1.0.xml"));
        generator.generate();
        Assert.assertTrue(generator instanceof BomGenerator11);
        Assert.assertEquals(CycloneDxSchema.Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        BomParser parser = new BomParser();
        Assert.assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_11));
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
        BomGenerator generator = BomGeneratorFactory.create(CycloneDxSchema.Version.VERSION_11, bom);
        generator.generate();
        Assert.assertTrue(generator instanceof BomGenerator11);
        Assert.assertEquals(CycloneDxSchema.Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        BomParser parser = new BomParser();
        Assert.assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_11));
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
        bom.addExtensibleType(new ExtensibleType("foo", "bom-bar", "my-bom-value"));
        BomGenerator generator = BomGeneratorFactory.create(CycloneDxSchema.Version.VERSION_11, bom);
        generator.generate();
        Assert.assertTrue(generator instanceof BomGenerator11);
        Assert.assertEquals(CycloneDxSchema.Version.VERSION_11, generator.getSchemaVersion());
        File file = writeToFile(generator.toXmlString());
        BomParser parser = new BomParser();
        Assert.assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_11));
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
        BomParser parser = new BomParser();
        return parser.parse(bomBytes);
    }
}
