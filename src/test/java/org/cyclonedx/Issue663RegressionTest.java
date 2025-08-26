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

import org.cyclonedx.Version;
import org.cyclonedx.generators.BomGeneratorFactory;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.parsers.BomParserFactory;
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.Parser;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * Regression test for GitHub issue #663: XMLParser fails when parsing metadata component
 * 
 * This test verifies that the XML parser can correctly handle BOMs that contain 
 * metadata components with nested components structures.
 */
public class Issue663RegressionTest {

    @Test
    @DisplayName("Should successfully parse BOM with nested components in metadata")
    public void testParsingMetadataWithNestedComponents() throws Exception {
        // Arrange
        File inputFile = new File("src/test/resources/issue-663-metadata-nested-components.xml");
        Parser parser = BomParserFactory.createParser(inputFile);
        
        // Act & Assert - This should not throw an exception
        assertThatNoException().isThrownBy(() -> {
            Bom bom = parser.parse(inputFile);
            
            // Verify the BOM structure was parsed correctly
            assertThat(bom).isNotNull();
            assertThat(bom.getSerialNumber()).isEqualTo("urn:uuid:12345678-1234-1234-1234-123456789abc");
            
            // Verify metadata component exists and has nested components
            Metadata metadata = bom.getMetadata();
            assertThat(metadata).isNotNull();
            
            Component metadataComponent = metadata.getComponent();
            assertThat(metadataComponent).isNotNull();
            assertThat(metadataComponent.getName()).isEqualTo("Main Application");
            assertThat(metadataComponent.getVersion()).isEqualTo("1.0.0");
            assertThat(metadataComponent.getType()).isEqualTo(Component.Type.APPLICATION);
            assertThat(metadataComponent.getBomRef()).isEqualTo("main-app");
            
            // Verify nested components in metadata component
            assertThat(metadataComponent.getComponents()).isNotNull();
            assertThat(metadataComponent.getComponents()).hasSize(2);
            
            // First nested component
            Component nestedLib1 = metadataComponent.getComponents().get(0);
            assertThat(nestedLib1.getName()).isEqualTo("Nested Library 1");
            assertThat(nestedLib1.getVersion()).isEqualTo("2.1.0");
            assertThat(nestedLib1.getBomRef()).isEqualTo("nested-lib-1");
            assertThat(nestedLib1.getType()).isEqualTo(Component.Type.LIBRARY);
            
            // Second nested component (which has its own nested component)
            Component nestedLib2 = metadataComponent.getComponents().get(1);
            assertThat(nestedLib2.getName()).isEqualTo("Nested Library 2");
            assertThat(nestedLib2.getVersion()).isEqualTo("3.2.1");
            assertThat(nestedLib2.getBomRef()).isEqualTo("nested-lib-2");
            assertThat(nestedLib2.getType()).isEqualTo(Component.Type.LIBRARY);
            
            // Verify deeply nested component
            assertThat(nestedLib2.getComponents()).isNotNull();
            assertThat(nestedLib2.getComponents()).hasSize(1);
            
            Component deeplyNested = nestedLib2.getComponents().get(0);
            assertThat(deeplyNested.getName()).isEqualTo("Deeply Nested Library");
            assertThat(deeplyNested.getVersion()).isEqualTo("1.5.0");
            assertThat(deeplyNested.getBomRef()).isEqualTo("deeply-nested-lib");
            assertThat(deeplyNested.getType()).isEqualTo(Component.Type.LIBRARY);
            
            // Verify root-level components still work
            assertThat(bom.getComponents()).isNotNull();
            assertThat(bom.getComponents()).hasSize(1);
            
            Component rootLib = bom.getComponents().get(0);
            assertThat(rootLib.getName()).isEqualTo("Root Level Library");
            assertThat(rootLib.getVersion()).isEqualTo("4.0.0");
            assertThat(rootLib.getBomRef()).isEqualTo("root-lib");
            assertThat(rootLib.getType()).isEqualTo(Component.Type.LIBRARY);
        });
    }

    @Test
    @DisplayName("Should handle metadata component without nested components")
    public void testParsingMetadataWithSimpleComponent() throws Exception {
        // Create a simpler XML for baseline comparison
        String simpleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<bom xmlns=\"http://cyclonedx.org/schema/bom/1.6\" serialNumber=\"urn:uuid:simple-test\" version=\"1\">\n" +
            "    <metadata>\n" +
            "        <component type=\"application\" bom-ref=\"simple-app\">\n" +
            "            <name>Simple App</name>\n" +
            "            <version>1.0.0</version>\n" +
            "        </component>\n" +
            "    </metadata>\n" +
            "</bom>";
        
        // Act & Assert
        assertThatNoException().isThrownBy(() -> {
            Parser parser = BomParserFactory.createParser(simpleXml.getBytes());
            Bom bom = parser.parse(simpleXml.getBytes());
            
            assertThat(bom).isNotNull();
            assertThat(bom.getMetadata()).isNotNull();
            assertThat(bom.getMetadata().getComponent()).isNotNull();
            assertThat(bom.getMetadata().getComponent().getName()).isEqualTo("Simple App");
        });
    }

    @Test
    @DisplayName("Should handle empty metadata component")
    public void testParsingEmptyMetadataComponent() throws Exception {
        String emptyMetadataXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<bom xmlns=\"http://cyclonedx.org/schema/bom/1.6\" serialNumber=\"urn:uuid:empty-test\" version=\"1\">\n" +
            "    <metadata>\n" +
            "        <timestamp>2023-01-01T12:00:00Z</timestamp>\n" +
            "    </metadata>\n" +
            "</bom>";
        
        // Act & Assert
        assertThatNoException().isThrownBy(() -> {
            Parser parser = BomParserFactory.createParser(emptyMetadataXml.getBytes());
            Bom bom = parser.parse(emptyMetadataXml.getBytes());
            
            assertThat(bom).isNotNull();
            assertThat(bom.getMetadata()).isNotNull();
            assertThat(bom.getMetadata().getComponent()).isNull();
        });
    }

    @Test 
    @DisplayName("Should successfully convert XML with nested components to JSON and back")
    public void testXmlToJsonRoundTripWithNestedComponents() throws Exception {
        // Arrange
        File inputFile = new File("src/test/resources/issue-663-metadata-nested-components.xml");
        XmlParser xmlParser = new XmlParser();
        JsonParser jsonParser = new JsonParser();
        
        // Act & Assert
        assertThatNoException().isThrownBy(() -> {
            // Step 1: Parse original XML
            Bom originalBom = xmlParser.parse(inputFile);
            
            // Step 2: Convert to JSON
            BomJsonGenerator jsonGenerator = BomGeneratorFactory.createJson(Version.VERSION_16, originalBom);
            String jsonOutput = jsonGenerator.toJsonString();
            assertThat(jsonOutput).isNotNull();
            assertThat(jsonOutput).contains("\"name\" : \"Main Application\"");
            assertThat(jsonOutput).contains("\"name\" : \"Nested Library 1\"");
            assertThat(jsonOutput).contains("\"name\" : \"Deeply Nested Library\"");
            
            // Step 3: Parse JSON back to BOM
            Bom parsedFromJson = jsonParser.parse(jsonOutput.getBytes());
            
            // Step 4: Verify the structure is intact
            assertBomStructureEquals(originalBom, parsedFromJson);
        });
    }

    @Test
    @DisplayName("Should successfully convert JSON with nested components to XML and back")
    public void testJsonToXmlRoundTripWithNestedComponents() throws Exception {
        // Arrange - Create JSON equivalent of our test XML
        String nestedComponentsJson = "{\n" +
            "  \"bomFormat\": \"CycloneDX\",\n" +
            "  \"specVersion\": \"1.6\",\n" +
            "  \"serialNumber\": \"urn:uuid:12345678-1234-1234-1234-123456789abc\",\n" +
            "  \"version\": 1,\n" +
            "  \"metadata\": {\n" +
            "    \"timestamp\": \"2023-01-01T12:00:00Z\",\n" +
            "    \"component\": {\n" +
            "      \"type\": \"application\",\n" +
            "      \"bom-ref\": \"main-app\",\n" +
            "      \"name\": \"Main Application\",\n" +
            "      \"version\": \"1.0.0\",\n" +
            "      \"description\": \"Main application component\",\n" +
            "      \"components\": [\n" +
            "        {\n" +
            "          \"type\": \"library\",\n" +
            "          \"bom-ref\": \"nested-lib-1\",\n" +
            "          \"name\": \"Nested Library 1\",\n" +
            "          \"version\": \"2.1.0\",\n" +
            "          \"description\": \"First nested library\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"type\": \"library\",\n" +
            "          \"bom-ref\": \"nested-lib-2\",\n" +
            "          \"name\": \"Nested Library 2\",\n" +
            "          \"version\": \"3.2.1\",\n" +
            "          \"description\": \"Second nested library\",\n" +
            "          \"components\": [\n" +
            "            {\n" +
            "              \"type\": \"library\",\n" +
            "              \"bom-ref\": \"deeply-nested-lib\",\n" +
            "              \"name\": \"Deeply Nested Library\",\n" +
            "              \"version\": \"1.5.0\",\n" +
            "              \"description\": \"Deeply nested component\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  },\n" +
            "  \"components\": [\n" +
            "    {\n" +
            "      \"type\": \"library\",\n" +
            "      \"bom-ref\": \"root-lib\",\n" +
            "      \"name\": \"Root Level Library\",\n" +
            "      \"version\": \"4.0.0\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
        
        JsonParser jsonParser = new JsonParser();
        XmlParser xmlParser = new XmlParser();
        
        // Act & Assert
        assertThatNoException().isThrownBy(() -> {
            // Step 1: Parse original JSON
            Bom originalBom = jsonParser.parse(nestedComponentsJson.getBytes());
            
            // Step 2: Convert to XML
            BomXmlGenerator xmlGenerator = BomGeneratorFactory.createXml(Version.VERSION_16, originalBom);
            String xmlOutput = xmlGenerator.toXmlString();
            assertThat(xmlOutput).isNotNull();
            assertThat(xmlOutput).contains("<name>Main Application</name>");
            assertThat(xmlOutput).contains("<name>Nested Library 1</name>");
            assertThat(xmlOutput).contains("<name>Deeply Nested Library</name>");
            
            // Step 3: Parse XML back to BOM
            Bom parsedFromXml = xmlParser.parse(xmlOutput.getBytes());
            
            // Step 4: Verify the structure is intact
            assertBomStructureEquals(originalBom, parsedFromXml);
        });
    }

    @Test
    @DisplayName("Should maintain data integrity through complete round-trip XML->JSON->XML->JSON")
    public void testCompleteRoundTripDataIntegrity() throws Exception {
        // Arrange
        File inputFile = new File("src/test/resources/issue-663-metadata-nested-components.xml");
        XmlParser xmlParser = new XmlParser();
        JsonParser jsonParser = new JsonParser();
        
        // Act & Assert
        assertThatNoException().isThrownBy(() -> {
            // Step 1: XML -> BOM
            Bom step1Bom = xmlParser.parse(inputFile);
            
            // Step 2: BOM -> JSON
            BomJsonGenerator jsonGenerator1 = BomGeneratorFactory.createJson(Version.VERSION_16, step1Bom);
            String step2Json = jsonGenerator1.toJsonString();
            
            // Step 3: JSON -> BOM
            Bom step3Bom = jsonParser.parse(step2Json.getBytes());
            
            // Step 4: BOM -> XML
            BomXmlGenerator xmlGenerator = BomGeneratorFactory.createXml(Version.VERSION_16, step3Bom);
            String step4Xml = xmlGenerator.toXmlString();
            
            // Step 5: XML -> BOM
            Bom step5Bom = xmlParser.parse(step4Xml.getBytes());
            
            // Step 6: BOM -> JSON (final)
            BomJsonGenerator jsonGenerator2 = BomGeneratorFactory.createJson(Version.VERSION_16, step5Bom);
            String step6Json = jsonGenerator2.toJsonString();
            
            // Verify: All BOMs should be structurally equivalent
            assertBomStructureEquals(step1Bom, step3Bom);
            assertBomStructureEquals(step3Bom, step5Bom);
            assertBomStructureEquals(step1Bom, step5Bom);
            
            // Verify: JSON outputs should be functionally equivalent (ignoring formatting)
            Bom jsonComparison1 = jsonParser.parse(step2Json.getBytes());
            Bom jsonComparison2 = jsonParser.parse(step6Json.getBytes());
            assertBomStructureEquals(jsonComparison1, jsonComparison2);
        });
    }

    /**
     * Helper method to perform deep comparison of BOM structures,
     * focusing on the nested components that were problematic in issue #663
     */
    private void assertBomStructureEquals(Bom expected, Bom actual) {
        assertThat(actual.getSerialNumber()).isEqualTo(expected.getSerialNumber());
        assertThat(actual.getVersion()).isEqualTo(expected.getVersion());
        
        // Compare metadata component structure
        Metadata expectedMetadata = expected.getMetadata();
        Metadata actualMetadata = actual.getMetadata();
        
        if (expectedMetadata == null) {
            assertThat(actualMetadata).isNull();
            return;
        }
        
        assertThat(actualMetadata).isNotNull();
        
        Component expectedMetaComponent = expectedMetadata.getComponent();
        Component actualMetaComponent = actualMetadata.getComponent();
        
        if (expectedMetaComponent == null) {
            assertThat(actualMetaComponent).isNull();
        } else {
            assertThat(actualMetaComponent).isNotNull();
            assertComponentEquals(expectedMetaComponent, actualMetaComponent);
        }
        
        // Compare root-level components
        assertThat(actual.getComponents()).hasSameSizeAs(expected.getComponents());
        for (int i = 0; i < expected.getComponents().size(); i++) {
            assertComponentEquals(expected.getComponents().get(i), actual.getComponents().get(i));
        }
    }
    
    /**
     * Recursively compares Component objects including nested components
     */
    private void assertComponentEquals(Component expected, Component actual) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getVersion()).isEqualTo(expected.getVersion());
        assertThat(actual.getType()).isEqualTo(expected.getType());
        assertThat(actual.getBomRef()).isEqualTo(expected.getBomRef());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        
        // Compare nested components recursively
        if (expected.getComponents() == null) {
            assertThat(actual.getComponents()).isNull();
        } else {
            assertThat(actual.getComponents()).isNotNull();
            assertThat(actual.getComponents()).hasSameSizeAs(expected.getComponents());
            
            for (int i = 0; i < expected.getComponents().size(); i++) {
                assertComponentEquals(expected.getComponents().get(i), actual.getComponents().get(i));
            }
        }
    }
}