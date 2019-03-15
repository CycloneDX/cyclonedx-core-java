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
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.cyclonedx;

import org.apache.commons.io.IOUtils;
import org.cyclonedx.model.Bom;
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

    private File writeToFile(String xmlString) throws Exception {
        try (FileWriter writer = new FileWriter(tempFile.getAbsolutePath())) {
            writer.write(xmlString);
        }
        return tempFile;
    }

    private Bom createCommonBom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.1.xml"));
        BomParser parser = new BomParser();
        return parser.parse(bomBytes);
    }
}
