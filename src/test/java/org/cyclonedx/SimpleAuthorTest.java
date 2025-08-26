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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Simple test to debug author field serialization
 */
public class SimpleAuthorTest {

    @Test
    public void testBasicAuthorSerialization() throws Exception {
        // Create a very simple component with just the author field
        Component component = new Component();
        component.setType(Component.Type.LIBRARY);
        component.setName("test");
        component.setVersion("1.0");
        component.setAuthor("Test Author");
        
        // Also set the new authors field for v1.6 testing
        OrganizationalContact newAuthor = new OrganizationalContact();
        newAuthor.setName("New Style Author");
        newAuthor.setEmail("author@example.com");
        component.setAuthors(Arrays.asList(newAuthor));
        
        System.out.println("Author field value: " + component.getAuthor());
        System.out.println("Authors field value: " + component.getAuthors().get(0).getName());
        
        Bom bom = new Bom();
        bom.setSerialNumber("urn:uuid:12345678-1234-5678-9abc-123456789abc");
        bom.setVersion(1);
        bom.addComponent(component);
        
        // Try different versions
        XmlParser xmlParser = new XmlParser();
        for (Version version : new Version[]{Version.VERSION_12, Version.VERSION_13, Version.VERSION_14, Version.VERSION_15, Version.VERSION_16}) {
            BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
            String xml = generator.toXmlString();
            System.out.println("=== " + version.getVersionString() + " ===");
            System.out.println(xml);
            System.out.println();
            
            // Validate the generated XML against the schema
            List<ParseException> validationErrors = xmlParser.validate(xml.getBytes(), version);
            assertTrue(validationErrors.isEmpty(), 
                String.format("Validation failed for version %s: %s", version.getVersionString(), 
                    validationErrors.toString()));
            
            System.out.println("✅ Validation passed for " + version.getVersionString());
            System.out.println();
        }
    }
}