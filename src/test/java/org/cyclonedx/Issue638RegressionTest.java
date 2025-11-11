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
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.generators.BomGeneratorFactory;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression test for GitHub issue #638: XML serialization of components with authors results in invalid CycloneDX SBOM
 * 
 * This test verifies that components with authors serialize correctly to XML format,
 * producing valid CycloneDX SBOM structure instead of nested &lt;authors&gt; tags.
 */
public class Issue638RegressionTest {

    @Test
    @DisplayName("Should serialize component authors correctly in XML format")
    public void testComponentAuthorsXmlSerialization() throws Exception {
        // Arrange - Create a component with authors similar to the issue example
        Component component = new Component();
        component.setType(Component.Type.LIBRARY);
        component.setBomRef("Maven:me.xdrop:fuzzywuzzy:1.4.0");
        component.setName("fuzzywuzzy");
        component.setVersion("1.4.0");
        
        // Create authors
        OrganizationalContact author1 = new OrganizationalContact();
        author1.setName("Panayiotis P");
        
        OrganizationalContact author2 = new OrganizationalContact();
        author2.setName("Jane Doe");
        author2.setEmail("jane@example.com");
        
        component.setAuthors(Arrays.asList(author1, author2));
        
        // Create BOM
        Bom bom = new Bom();
        bom.setSerialNumber("urn:uuid:12345678-1234-1234-1234-123456789abc");
        bom.setVersion(1);
        bom.addComponent(component);
        
        // Act - Generate XML
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_16, bom);
        String xmlOutput = generator.toXmlString();
        
        // Assert - Validate against schema first
        XmlParser xmlParser = new XmlParser();
        List<ParseException> validationErrors = xmlParser.validate(xmlOutput.getBytes(), Version.VERSION_16);
        assertTrue(validationErrors.isEmpty(), "Generated XML should validate against schema: " + validationErrors);
        
        // Verify correct XML structure
        assertThat(xmlOutput).isNotNull();
        
        // Should contain proper authors structure: <authors><author>...</author></authors>
        assertThat(xmlOutput).contains("<authors>");
        assertThat(xmlOutput).contains("</authors>");
        assertThat(xmlOutput).contains("<author>");
        assertThat(xmlOutput).contains("</author>");
        assertThat(xmlOutput).contains("<name>Panayiotis P</name>");
        assertThat(xmlOutput).contains("<name>Jane Doe</name>");
        assertThat(xmlOutput).contains("<email>jane@example.com</email>");
        
        // Should NOT contain nested authors tags (the bug)
        assertThat(xmlOutput).doesNotContain("<authors><authors>");
        assertThat(xmlOutput).doesNotContain("</authors></authors>");
        
        // Verify the structure matches expected format from issue description
        assertThat(xmlOutput).containsPattern("(?s)<authors>\\s*<author>\\s*<name>Panayiotis P</name>\\s*</author>");
        assertThat(xmlOutput).containsPattern("(?s)<authors>.*<author>\\s*<name>Jane Doe</name>\\s*<email>jane@example.com</email>\\s*</author>");
    }

    @Test
    @DisplayName("Should handle component with single author correctly")
    public void testComponentSingleAuthorXmlSerialization() throws Exception {
        // Arrange
        Component component = new Component();
        component.setType(Component.Type.LIBRARY);
        component.setBomRef("single-author-component");
        component.setName("example-lib");
        component.setVersion("1.0.0");
        
        OrganizationalContact author = new OrganizationalContact();
        author.setName("John Smith");
        author.setEmail("john@example.com");
        author.setPhone("+1-555-1234");
        
        component.setAuthors(Arrays.asList(author));
        
        Bom bom = new Bom();
        bom.setSerialNumber("urn:uuid:12345678-1234-5678-9abc-123456789def");
        bom.setVersion(1);
        bom.addComponent(component);
        
        // Act
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_16, bom);
        String xmlOutput = generator.toXmlString();
        
        // Assert - Validate against schema first
        XmlParser xmlParser = new XmlParser();
        List<ParseException> validationErrors = xmlParser.validate(xmlOutput.getBytes(), Version.VERSION_16);
        assertTrue(validationErrors.isEmpty(), "Generated XML should validate against schema: " + validationErrors);
        
        // Verify structure
        assertThat(xmlOutput).contains("<authors>");
        assertThat(xmlOutput).contains("</authors>");
        assertThat(xmlOutput).contains("<author>");
        assertThat(xmlOutput).contains("</author>");
        assertThat(xmlOutput).contains("<name>John Smith</name>");
        assertThat(xmlOutput).contains("<email>john@example.com</email>");
        assertThat(xmlOutput).contains("<phone>+1-555-1234</phone>");
        
        // Should NOT contain the bug (nested authors)
        assertThat(xmlOutput).doesNotContain("<authors><authors>");
    }

    @Test
    @DisplayName("Should handle component with no authors correctly")
    public void testComponentNoAuthorsXmlSerialization() throws Exception {
        // Arrange
        Component component = new Component();
        component.setType(Component.Type.LIBRARY);
        component.setBomRef("no-authors-component");
        component.setName("no-author-lib");
        component.setVersion("1.0.0");
        // No authors set
        
        Bom bom = new Bom();
        bom.setSerialNumber("urn:uuid:12345678-1234-5678-9abc-123456789fed");
        bom.setVersion(1);
        bom.addComponent(component);
        
        // Act
        BomXmlGenerator generator = BomGeneratorFactory.createXml(Version.VERSION_16, bom);
        String xmlOutput = generator.toXmlString();
        
        // Assert - Validate against schema first
        XmlParser xmlParser = new XmlParser();
        List<ParseException> validationErrors = xmlParser.validate(xmlOutput.getBytes(), Version.VERSION_16);
        assertTrue(validationErrors.isEmpty(), "Generated XML should validate against schema: " + validationErrors);
        
        // Verify no authors section
        assertThat(xmlOutput).doesNotContain("<authors>");
        assertThat(xmlOutput).doesNotContain("</authors>");
        assertThat(xmlOutput).doesNotContain("<author>");
        assertThat(xmlOutput).doesNotContain("</author>");
    }
}