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
import org.cyclonedx.generators.BomGeneratorFactory;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive TDD test suite for Issue #638: author/authors field handling across versions and formats.
 *
 * This test suite covers:
 * - XML serialization and deserialization for versions 1.2, 1.5, and 1.6
 * - JSON serialization and deserialization for versions 1.2, 1.5, and 1.6
 * - Cross-format conversions (XML to JSON, JSON to XML)
 * - All field combinations: only author (string), only authors (list), both fields
 * - Schema validation for all generated outputs
 * - Migration scenarios across versions
 */
@DisplayName("Issue #638: Comprehensive author/authors field handling")
public class Issue638ComprehensiveTest {

    // ============================================================================
    // VERSION 1.2 TESTS - Only 'author' (String) field exists
    // ============================================================================

    @Test
    @DisplayName("v1.2 XML: Serialize component with author string")
    public void testV12_XML_SerializeAuthorString() throws Exception {
        Component component = createComponentWithAuthorString("John Doe");
        Bom bom = createBom(component);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_12, bom);
        String xml = generator.toXmlString();

        assertThat(xml).contains("<author>John Doe</author>");
        assertThat(xml).doesNotContain("<authors>");

        // Validate against schema
        XmlParser parser = new XmlParser();
        List<ParseException> errors = parser.validate(xml.getBytes(), Version.VERSION_12);
        assertThat(errors).isEmpty();
    }

    @Test
    @DisplayName("v1.2 XML: Deserialize author string from XML")
    public void testV12_XML_DeserializeAuthorString() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<bom serialNumber=\"urn:uuid:3e671687-395b-41f5-a30f-a58921a69b79\" version=\"1\" xmlns=\"http://cyclonedx.org/schema/bom/1.2\">\n" +
            "  <components>\n" +
            "    <component type=\"library\">\n" +
            "      <author>Jane Smith</author>\n" +
            "      <name>test-lib</name>\n" +
            "      <version>1.0.0</version>\n" +
            "    </component>\n" +
            "  </components>\n" +
            "</bom>";

        XmlParser parser = new XmlParser();
        Bom bom = parser.parse(xml.getBytes());

        assertThat(bom.getComponents()).hasSize(1);
        Component component = bom.getComponents().get(0);
        assertThat(component.getAuthor()).isEqualTo("Jane Smith");
        assertThat(component.getAuthors()).isNull();
    }

    @Test
    @DisplayName("v1.2 XML: Roundtrip with author string")
    public void testV12_XML_Roundtrip() throws Exception {
        String originalXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<bom serialNumber=\"urn:uuid:3e671687-395b-41f5-a30f-a58921a69b79\" version=\"1\" xmlns=\"http://cyclonedx.org/schema/bom/1.2\">\n" +
            "  <components>\n" +
            "    <component type=\"library\">\n" +
            "      <author>Original Author</author>\n" +
            "      <name>test-lib</name>\n" +
            "      <version>1.0.0</version>\n" +
            "    </component>\n" +
            "  </components>\n" +
            "</bom>";

        XmlParser parser = new XmlParser();
        Bom bom = parser.parse(originalXml.getBytes());

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_12, bom);
        String regeneratedXml = generator.toXmlString();

        assertThat(regeneratedXml).contains("<author>Original Author</author>");
        assertThat(regeneratedXml).doesNotContain("<authors>");

        // Validate regenerated XML
        List<ParseException> errors = parser.validate(regeneratedXml.getBytes(), Version.VERSION_12);
        assertThat(errors).isEmpty();
    }

    @Test
    @DisplayName("v1.2 JSON: Serialize component with author string")
    public void testV12_JSON_SerializeAuthorString() throws Exception {
        Component component = createComponentWithAuthorString("Alice Johnson");
        Bom bom = createBom(component);

        BomJsonGenerator generator = BomGeneratorFactory.createJson(Version.VERSION_12, bom);
        String json = generator.toJsonString();

        assertThat(json).contains("\"author\" : \"Alice Johnson\"");
        assertThat(json).doesNotContain("\"authors\"");
    }

    @Test
    @DisplayName("v1.2 JSON: Roundtrip with author string")
    public void testV12_JSON_Roundtrip() throws Exception {
        Component component = createComponentWithAuthorString("Bob Brown");
        Bom bom = createBom(component);

        BomJsonGenerator generator = BomGeneratorFactory.createJson(Version.VERSION_12, bom);
        String json = generator.toJsonString();

        JsonParser parser = new JsonParser();
        Bom parsedBom = parser.parse(json.getBytes());

        assertThat(parsedBom.getComponents()).hasSize(1);
        Component parsedComponent = parsedBom.getComponents().get(0);
        assertThat(parsedComponent.getAuthor()).isEqualTo("Bob Brown");
        assertThat(parsedComponent.getAuthors()).isNull();
    }

    @Test
    @DisplayName("v1.2: Cross-format XML to JSON")
    public void testV12_CrossFormat_XmlToJson() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<bom serialNumber=\"urn:uuid:3e671687-395b-41f5-a30f-a58921a69b79\" version=\"1\" xmlns=\"http://cyclonedx.org/schema/bom/1.2\">\n" +
            "  <components>\n" +
            "    <component type=\"library\">\n" +
            "      <author>Cross Format Author</author>\n" +
            "      <name>test-lib</name>\n" +
            "      <version>1.0.0</version>\n" +
            "    </component>\n" +
            "  </components>\n" +
            "</bom>";

        XmlParser xmlParser = new XmlParser();
        Bom bom = xmlParser.parse(xml.getBytes());

        BomJsonGenerator jsonGenerator = BomGeneratorFactory.createJson(Version.VERSION_12, bom);
        String json = jsonGenerator.toJsonString();

        assertThat(json).contains("\"author\" : \"Cross Format Author\"");
        assertThat(json).doesNotContain("\"authors\"");
    }

    // ============================================================================
    // VERSION 1.5 TESTS - Only 'author' (String) field exists (same as 1.2)
    // ============================================================================

    @Test
    @DisplayName("v1.5 XML: Serialize and validate author string")
    public void testV15_XML_SerializeAuthorString() throws Exception {
        Component component = createComponentWithAuthorString("v1.5 Author");
        Bom bom = createBom(component);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_15, bom);
        String xml = generator.toXmlString();

        assertThat(xml).contains("<author>v1.5 Author</author>");
        assertThat(xml).doesNotContain("<authors>");

        XmlParser parser = new XmlParser();
        List<ParseException> errors = parser.validate(xml.getBytes(), Version.VERSION_15);
        assertThat(errors).isEmpty();
    }

    // ============================================================================
    // VERSION 1.6 TESTS - Both 'author' (deprecated) and 'authors' exist
    // ============================================================================

    @Test
    @DisplayName("v1.6 XML: Serialize component with ONLY author string (deprecated field)")
    public void testV16_XML_SerializeOnlyAuthorString() throws Exception {
        Component component = createComponentWithAuthorString("Deprecated Author");
        Bom bom = createBom(component);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_16, bom);
        String xml = generator.toXmlString();

        // Should contain deprecated author field
        assertThat(xml).contains("<author>Deprecated Author</author>");
        // Should NOT contain authors wrapper (not set)
        assertThat(xml).doesNotContain("<authors>");

        XmlParser parser = new XmlParser();
        List<ParseException> errors = parser.validate(xml.getBytes(), Version.VERSION_16);
        assertThat(errors).isEmpty();
    }

    @Test
    @DisplayName("v1.6 XML: Serialize component with ONLY authors list (new field)")
    public void testV16_XML_SerializeOnlyAuthorsList() throws Exception {
        Component component = createComponentWithAuthorsList(
            Arrays.asList(
                createOrganizationalContact("Author One", "author1@example.com", "+1-555-0001"),
                createOrganizationalContact("Author Two", "author2@example.com", null)
            )
        );
        Bom bom = createBom(component);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_16, bom);
        String xml = generator.toXmlString();

        // Should contain authors wrapper with properly nested author elements
        assertThat(xml).contains("<authors>");
        assertThat(xml).contains("</authors>");
        assertThat(xml).contains("<name>Author One</name>");
        assertThat(xml).contains("<email>author1@example.com</email>");
        assertThat(xml).contains("<phone>+1-555-0001</phone>");
        assertThat(xml).contains("<name>Author Two</name>");
        assertThat(xml).contains("<email>author2@example.com</email>");

        // Should NOT contain the deprecated author string field (not set)
        assertThat(xml).doesNotContainPattern("<author>(?!\\s*<name>)[^<]+</author>");

        // Critical: Verify correct nesting - NO double <authors> tags
        assertThat(xml).doesNotContain("<authors>\n    <authors>");
        assertThat(xml).doesNotContain("<authors><authors>");

        XmlParser parser = new XmlParser();
        List<ParseException> errors = parser.validate(xml.getBytes(), Version.VERSION_16);
        assertThat(errors).isEmpty();
    }

    @Test
    @DisplayName("v1.6 XML: Serialize component with BOTH author and authors (both fields set)")
    public void testV16_XML_SerializeBothFields() throws Exception {
        Component component = new Component();
        component.setType(Component.Type.LIBRARY);
        component.setName("test-lib");
        component.setVersion("1.0.0");

        // Set both deprecated and new fields
        component.setAuthor("Legacy String Author");
        component.setAuthors(Arrays.asList(
            createOrganizationalContact("New Format Author", "new@example.com", null)
        ));

        Bom bom = createBom(component);

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_16, bom);
        String xml = generator.toXmlString();

        // Both fields should appear as separate elements
        assertThat(xml).contains("<author>Legacy String Author</author>");
        assertThat(xml).contains("<authors>");
        assertThat(xml).contains("<name>New Format Author</name>");

        XmlParser parser = new XmlParser();
        List<ParseException> errors = parser.validate(xml.getBytes(), Version.VERSION_16);
        assertThat(errors).isEmpty();
    }

    @Test
    @DisplayName("v1.6 XML: Deserialize ONLY deprecated author string")
    public void testV16_XML_DeserializeOnlyAuthorString() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<bom serialNumber=\"urn:uuid:4f681688-496b-42f5-b30f-b68921b70b80\" version=\"1\" xmlns=\"http://cyclonedx.org/schema/bom/1.6\">\n" +
            "  <components>\n" +
            "    <component type=\"library\">\n" +
            "      <author>Only Deprecated Field</author>\n" +
            "      <name>test-lib</name>\n" +
            "      <version>1.0.0</version>\n" +
            "    </component>\n" +
            "  </components>\n" +
            "</bom>";

        XmlParser parser = new XmlParser();
        Bom bom = parser.parse(xml.getBytes());

        assertThat(bom.getComponents()).hasSize(1);
        Component component = bom.getComponents().get(0);
        assertThat(component.getAuthor()).isEqualTo("Only Deprecated Field");
        assertThat(component.getAuthors()).isNull();
    }

    @Test
    @DisplayName("v1.6 XML: Deserialize ONLY authors list")
    public void testV16_XML_DeserializeOnlyAuthorsList() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<bom serialNumber=\"urn:uuid:4f681688-496b-42f5-b30f-b68921b70b80\" version=\"1\" xmlns=\"http://cyclonedx.org/schema/bom/1.6\">\n" +
            "  <components>\n" +
            "    <component type=\"library\">\n" +
            "      <authors>\n" +
            "        <author>\n" +
            "          <name>First Author</name>\n" +
            "          <email>first@example.com</email>\n" +
            "        </author>\n" +
            "        <author>\n" +
            "          <name>Second Author</name>\n" +
            "          <phone>+1-555-9999</phone>\n" +
            "        </author>\n" +
            "      </authors>\n" +
            "      <name>test-lib</name>\n" +
            "      <version>1.0.0</version>\n" +
            "    </component>\n" +
            "  </components>\n" +
            "</bom>";

        XmlParser parser = new XmlParser();
        Bom bom = parser.parse(xml.getBytes());

        assertThat(bom.getComponents()).hasSize(1);
        Component component = bom.getComponents().get(0);
        assertThat(component.getAuthor()).isNull();
        assertThat(component.getAuthors()).isNotNull();
        assertThat(component.getAuthors()).hasSize(2);
        assertThat(component.getAuthors().get(0).getName()).isEqualTo("First Author");
        assertThat(component.getAuthors().get(0).getEmail()).isEqualTo("first@example.com");
        assertThat(component.getAuthors().get(1).getName()).isEqualTo("Second Author");
        assertThat(component.getAuthors().get(1).getPhone()).isEqualTo("+1-555-9999");
    }

    @Test
    @DisplayName("v1.6 XML: Deserialize BOTH author and authors")
    public void testV16_XML_DeserializeBothFields() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<bom serialNumber=\"urn:uuid:4f681688-496b-42f5-b30f-b68921b70b80\" version=\"1\" xmlns=\"http://cyclonedx.org/schema/bom/1.6\">\n" +
            "  <components>\n" +
            "    <component type=\"library\">\n" +
            "      <authors>\n" +
            "        <author>\n" +
            "          <name>New Format</name>\n" +
            "        </author>\n" +
            "      </authors>\n" +
            "      <author>Old Format</author>\n" +
            "      <name>test-lib</name>\n" +
            "      <version>1.0.0</version>\n" +
            "    </component>\n" +
            "  </components>\n" +
            "</bom>";

        XmlParser parser = new XmlParser();
        Bom bom = parser.parse(xml.getBytes());

        assertThat(bom.getComponents()).hasSize(1);
        Component component = bom.getComponents().get(0);
        assertThat(component.getAuthor()).isEqualTo("Old Format");
        assertThat(component.getAuthors()).isNotNull();
        assertThat(component.getAuthors()).hasSize(1);
        assertThat(component.getAuthors().get(0).getName()).isEqualTo("New Format");
    }

    @Test
    @DisplayName("v1.6 XML: Roundtrip with only authors list")
    public void testV16_XML_RoundtripAuthorsList() throws Exception {
        String originalXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<bom serialNumber=\"urn:uuid:4f681688-496b-42f5-b30f-b68921b70b80\" version=\"1\" xmlns=\"http://cyclonedx.org/schema/bom/1.6\">\n" +
            "  <components>\n" +
            "    <component type=\"library\">\n" +
            "      <authors>\n" +
            "        <author>\n" +
            "          <name>Roundtrip Author</name>\n" +
            "          <email>roundtrip@example.com</email>\n" +
            "        </author>\n" +
            "      </authors>\n" +
            "      <name>test-lib</name>\n" +
            "      <version>1.0.0</version>\n" +
            "    </component>\n" +
            "  </components>\n" +
            "</bom>";

        XmlParser parser = new XmlParser();
        Bom bom = parser.parse(originalXml.getBytes());

        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_16, bom);
        String regeneratedXml = generator.toXmlString();

        assertThat(regeneratedXml).contains("<authors>");
        assertThat(regeneratedXml).contains("<name>Roundtrip Author</name>");
        assertThat(regeneratedXml).contains("<email>roundtrip@example.com</email>");

        List<ParseException> errors = parser.validate(regeneratedXml.getBytes(), Version.VERSION_16);
        assertThat(errors).isEmpty();
    }

    @Test
    @DisplayName("v1.6 JSON: Serialize component with ONLY author string")
    public void testV16_JSON_SerializeOnlyAuthorString() throws Exception {
        Component component = createComponentWithAuthorString("JSON Deprecated Author");
        Bom bom = createBom(component);

        BomJsonGenerator generator = BomGeneratorFactory.createJson(Version.VERSION_16, bom);
        String json = generator.toJsonString();

        assertThat(json).contains("\"author\" : \"JSON Deprecated Author\"");
        assertThat(json).doesNotContain("\"authors\"");
    }

    @Test
    @DisplayName("v1.6 JSON: Serialize component with ONLY authors list")
    public void testV16_JSON_SerializeOnlyAuthorsList() throws Exception {
        Component component = createComponentWithAuthorsList(
            Arrays.asList(
                createOrganizationalContact("JSON Author", "json@example.com", null)
            )
        );
        Bom bom = createBom(component);

        BomJsonGenerator generator = BomGeneratorFactory.createJson(Version.VERSION_16, bom);
        String json = generator.toJsonString();

        assertThat(json).contains("\"authors\"");
        assertThat(json).contains("\"name\" : \"JSON Author\"");
        assertThat(json).contains("\"email\" : \"json@example.com\"");
        assertThat(json).doesNotContain("\"author\" : \"");
    }

    @Test
    @DisplayName("v1.6 JSON: Serialize component with BOTH fields")
    public void testV16_JSON_SerializeBothFields() throws Exception {
        Component component = new Component();
        component.setType(Component.Type.LIBRARY);
        component.setName("test-lib");
        component.setVersion("1.0.0");

        component.setAuthor("JSON Legacy");
        component.setAuthors(Arrays.asList(
            createOrganizationalContact("JSON New", "jsonnew@example.com", null)
        ));

        Bom bom = createBom(component);

        BomJsonGenerator generator = BomGeneratorFactory.createJson(Version.VERSION_16, bom);
        String json = generator.toJsonString();

        assertThat(json).contains("\"author\" : \"JSON Legacy\"");
        assertThat(json).contains("\"authors\"");
        assertThat(json).contains("\"name\" : \"JSON New\"");
    }

    @Test
    @DisplayName("v1.6 JSON: Roundtrip with authors list")
    public void testV16_JSON_RoundtripAuthorsList() throws Exception {
        Component component = createComponentWithAuthorsList(
            Arrays.asList(
                createOrganizationalContact("Roundtrip JSON", "rt@example.com", "+1-555-7777")
            )
        );
        Bom bom = createBom(component);

        BomJsonGenerator generator = BomGeneratorFactory.createJson(Version.VERSION_16, bom);
        String json = generator.toJsonString();

        JsonParser parser = new JsonParser();
        Bom parsedBom = parser.parse(json.getBytes());

        assertThat(parsedBom.getComponents()).hasSize(1);
        Component parsedComponent = parsedBom.getComponents().get(0);
        assertThat(parsedComponent.getAuthors()).isNotNull();
        assertThat(parsedComponent.getAuthors()).hasSize(1);
        assertThat(parsedComponent.getAuthors().get(0).getName()).isEqualTo("Roundtrip JSON");
        assertThat(parsedComponent.getAuthors().get(0).getEmail()).isEqualTo("rt@example.com");
        assertThat(parsedComponent.getAuthors().get(0).getPhone()).isEqualTo("+1-555-7777");
    }

    @Test
    @DisplayName("v1.6: Cross-format XML to JSON with authors list")
    public void testV16_CrossFormat_XmlToJson_AuthorsList() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<bom serialNumber=\"urn:uuid:4f681688-496b-42f5-b30f-b68921b70b80\" version=\"1\" xmlns=\"http://cyclonedx.org/schema/bom/1.6\">\n" +
            "  <components>\n" +
            "    <component type=\"library\">\n" +
            "      <authors>\n" +
            "        <author>\n" +
            "          <name>Cross Author</name>\n" +
            "        </author>\n" +
            "      </authors>\n" +
            "      <name>test-lib</name>\n" +
            "      <version>1.0.0</version>\n" +
            "    </component>\n" +
            "  </components>\n" +
            "</bom>";

        XmlParser xmlParser = new XmlParser();
        Bom bom = xmlParser.parse(xml.getBytes());

        BomJsonGenerator jsonGenerator = BomGeneratorFactory.createJson(Version.VERSION_16, bom);
        String json = jsonGenerator.toJsonString();

        assertThat(json).contains("\"authors\"");
        assertThat(json).contains("\"name\" : \"Cross Author\"");
    }

    @Test
    @DisplayName("v1.6: Cross-format JSON to XML with authors list")
    public void testV16_CrossFormat_JsonToXml_AuthorsList() throws Exception {
        Component component = createComponentWithAuthorsList(
            Arrays.asList(
                createOrganizationalContact("JSON to XML", "j2x@example.com", null)
            )
        );
        Bom bom = createBom(component);

        BomJsonGenerator jsonGenerator = BomGeneratorFactory.createJson(Version.VERSION_16, bom);
        String json = jsonGenerator.toJsonString();

        JsonParser jsonParser = new JsonParser();
        Bom parsedBom = jsonParser.parse(json.getBytes());

        BomXmlGenerator xmlGenerator = BomGeneratorFactory.createXml(Version.VERSION_16, parsedBom);
        String xml = xmlGenerator.toXmlString();

        assertThat(xml).contains("<authors>");
        assertThat(xml).contains("<name>JSON to XML</name>");

        XmlParser xmlParser = new XmlParser();
        List<ParseException> errors = xmlParser.validate(xml.getBytes(), Version.VERSION_16);
        assertThat(errors).isEmpty();
    }

    // ============================================================================
    // MIGRATION SCENARIOS - Cross-version compatibility
    // ============================================================================

    @Test
    @DisplayName("Migration: v1.2 XML with author → v1.6 XML (deprecated field should work)")
    public void testMigration_V12ToV16_AuthorString() throws Exception {
        String xmlV12 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<bom serialNumber=\"urn:uuid:3e671687-395b-41f5-a30f-a58921a69b79\" version=\"1\" xmlns=\"http://cyclonedx.org/schema/bom/1.2\">\n" +
            "  <components>\n" +
            "    <component type=\"library\">\n" +
            "      <author>Migrated Author</author>\n" +
            "      <name>test-lib</name>\n" +
            "      <version>1.0.0</version>\n" +
            "    </component>\n" +
            "  </components>\n" +
            "</bom>";

        XmlParser parser = new XmlParser();
        Bom bom = parser.parse(xmlV12.getBytes());

        // Serialize as v1.6 - deprecated field should still appear
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_16, bom);
        String xmlV16 = generator.toXmlString();

        assertThat(xmlV16).contains("<author>Migrated Author</author>");

        List<ParseException> errors = parser.validate(xmlV16.getBytes(), Version.VERSION_16);
        assertThat(errors).isEmpty();
    }

    @Test
    @DisplayName("Migration: v1.6 XML with authors → v1.5 XML (should handle gracefully)")
    public void testMigration_V16ToV15_AuthorsList() throws Exception {
        String xmlV16 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<bom serialNumber=\"urn:uuid:3e671687-395b-41f5-a30f-a58921a69b79\" version=\"1\" xmlns=\"http://cyclonedx.org/schema/bom/1.6\">\n" +
            "  <components>\n" +
            "    <component type=\"library\">\n" +
            "      <authors>\n" +
            "        <author>\n" +
            "          <name>New Format Author</name>\n" +
            "        </author>\n" +
            "      </authors>\n" +
            "      <name>test-lib</name>\n" +
            "      <version>1.0.0</version>\n" +
            "    </component>\n" +
            "  </components>\n" +
            "</bom>";

        XmlParser parser = new XmlParser();
        Bom bom = parser.parse(xmlV16.getBytes());

        // Serialize as v1.5 - authors list should not appear (not in spec)
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_15, bom);
        String xmlV15 = generator.toXmlString();

        assertThat(xmlV15).doesNotContain("<authors>");

        List<ParseException> errors = parser.validate(xmlV15.getBytes(), Version.VERSION_15);
        assertThat(errors).isEmpty();
    }

    // ============================================================================
    // Helper Methods
    // ============================================================================

    private Component createComponentWithAuthorString(String author) {
        Component component = new Component();
        component.setType(Component.Type.LIBRARY);
        component.setName("test-lib");
        component.setVersion("1.0.0");
        component.setAuthor(author);
        return component;
    }

    private Component createComponentWithAuthorsList(List<OrganizationalContact> authors) {
        Component component = new Component();
        component.setType(Component.Type.LIBRARY);
        component.setName("test-lib");
        component.setVersion("1.0.0");
        component.setAuthors(authors);
        return component;
    }

    private OrganizationalContact createOrganizationalContact(String name, String email, String phone) {
        OrganizationalContact contact = new OrganizationalContact();
        contact.setName(name);
        if (email != null) {
            contact.setEmail(email);
        }
        if (phone != null) {
            contact.setPhone(phone);
        }
        return contact;
    }

    private Bom createBom(Component component) {
        Bom bom = new Bom();
        // Use a valid UUID format
        bom.setSerialNumber("urn:uuid:3e671687-395b-41f5-a30f-a58921a69b79");
        bom.setVersion(1);
        bom.addComponent(component);
        return bom;
    }
}