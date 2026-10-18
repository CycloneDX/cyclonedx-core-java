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

import org.cyclonedx.exception.ParseException;
import org.cyclonedx.parsers.BomParserFactory;
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.Parser;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BomParserFactoryTest {

    @TempDir
    Path tempDir;

    @ParameterizedTest
    @ValueSource(strings = {"", "\n", "    \t\r\n", "\uFEFF", "\uFEFF \r\n"})
    void testJsonWithLeadingWhitespace(String prefix) throws Exception {
        byte[] bytes = (prefix + "{\"bomFormat\":\"CycloneDX\",\"specVersion\":\"1.6\",\"version\":1}")
                .getBytes(StandardCharsets.UTF_8);
        Parser parser = BomParserFactory.createParser(bytes);
        assertInstanceOf(JsonParser.class, parser);
        assertEquals(1, parser.parse(bytes).getVersion());
        assertTrue(BomParserFactory.looksLikeCycloneDX(bytes));

        File file = Files.write(tempDir.resolve("bom.json"), bytes).toFile();
        Parser fileParser = BomParserFactory.createParser(file);
        assertInstanceOf(JsonParser.class, fileParser);
        assertEquals(1, fileParser.parse(file).getVersion());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "\n", "    \t\r\n", "\uFEFF", "\uFEFF \r\n"})
    void testXmlWithLeadingWhitespace(String prefix) throws Exception {
        byte[] bytes = (prefix + "<bom xmlns=\"http://cyclonedx.org/schema/bom/1.6\" version=\"1\"/>")
                .getBytes(StandardCharsets.UTF_8);
        Parser parser = BomParserFactory.createParser(bytes);
        assertInstanceOf(XmlParser.class, parser);
        assertEquals(1, parser.parse(bytes).getVersion());

        File file = Files.write(tempDir.resolve("bom.xml"), bytes).toFile();
        Parser fileParser = BomParserFactory.createParser(file);
        assertInstanceOf(XmlParser.class, fileParser);
        assertEquals(1, fileParser.parse(file).getVersion());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " \t\r\n", "\uFEFF", "\uFEFF \n", "x{}", " \u000B{}"})
    void testRejectsEmptyOrUnsupportedContent(String content) throws Exception {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        assertThrows(ParseException.class, () -> BomParserFactory.createParser(bytes));
        assertFalse(BomParserFactory.looksLikeCycloneDX(bytes));
        File file = Files.write(tempDir.resolve("unsupported"), bytes).toFile();
        assertThrows(ParseException.class, () -> BomParserFactory.createParser(file));
    }

    @Test
    public void testXMLFactory() throws Exception {
        Parser parser = BomParserFactory.createParser(
            new File(requireNonNull(BomParserFactory.class.getResource("/bom-1.2.xml")).getFile()));
        assertInstanceOf(XmlParser.class, parser);
    }

    @Test
    public void testJSONFactory() throws Exception {
        Parser parser = BomParserFactory.createParser(new File(
            requireNonNull(BomParserFactory.class.getResource("/bom-1.2.json")).getFile()));
        assertInstanceOf(JsonParser.class, parser);
    }

    @Test
    public void testXMLFactoryWithUtf8ByteOrderMarker() throws Exception {
        Parser parser = BomParserFactory.createParser(
            new File(requireNonNull(BomParserFactory.class.getResource("/bom-1.2-utf8bom.xml")).getFile()));
        assertInstanceOf(XmlParser.class, parser);
    }

    @Test
    public void testJSONFactoryWithUtf8ByteOrderMarker() throws Exception {
        Parser parser = BomParserFactory.createParser(new File(
            requireNonNull(BomParserFactory.class.getResource("/bom-1.2-utf8bom.json")).getFile()));
        assertInstanceOf(JsonParser.class, parser);
    }

    @Test
    public void testFactoryFromByteArrayWithUtf8ByteOrderMarker() throws Exception {
        byte[] xml = Files.readAllBytes(new File(
            requireNonNull(BomParserFactory.class.getResource("/bom-1.2-utf8bom.xml")).getFile()).toPath());
        byte[] json = Files.readAllBytes(new File(
            requireNonNull(BomParserFactory.class.getResource("/bom-1.2-utf8bom.json")).getFile()).toPath());
        assertInstanceOf(XmlParser.class, BomParserFactory.createParser(xml));
        assertInstanceOf(JsonParser.class, BomParserFactory.createParser(json));
    }

    @Test()
    public void testFactoryThrowsParseExceptionWithEmptyData() {
        byte[] emptyData = new byte[]{};
        assertThrows(ParseException.class, () ->
            BomParserFactory.createParser(emptyData)
        );
    }
}
