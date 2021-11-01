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
package org.cyclonedx.parsers;

import org.apache.commons.io.IOUtils;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.ServiceData;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonParserTest {

    @Test
    public void testValid12Bom() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.2.json").getFile());
        final JsonParser parser = new JsonParser();
        Bom bom = parser.parse(file);
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_12, true));
        System.out.println(bom.getSerialNumber());
    }

    @Test
    public void testParsedObjects12Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.2.json"));
        final JsonParser parser = new JsonParser();
        final Bom bom = parser.parse(bomBytes);
        assertEquals("1.2", bom.getSpecVersion());
        assertEquals(3, bom.getComponents().size());
        assertEquals(1, bom.getVersion());
        assertNotNull(bom.getMetadata());
        assertNotNull(bom.getMetadata().getTimestamp());
        assertEquals(1, bom.getMetadata().getTools().size());
        assertEquals("Awesome Vendor", bom.getMetadata().getTools().get(0).getVendor());
        assertEquals("Awesome Tool", bom.getMetadata().getTools().get(0).getName());
        assertEquals("9.1.2", bom.getMetadata().getTools().get(0).getVersion());
        assertEquals(2, bom.getMetadata().getTools().get(0).getHashes().size());
        assertEquals("SHA-1", bom.getMetadata().getTools().get(0).getHashes().get(0).getAlgorithm());
        assertEquals("25ed8e31b995bb927966616df2a42b979a2717f0", bom.getMetadata().getTools().get(0).getHashes().get(0).getValue());
        assertEquals(1, bom.getMetadata().getAuthors().size());
        assertEquals("Samantha Wright", bom.getMetadata().getAuthors().get(0).getName());
        assertEquals("samantha.wright@example.com", bom.getMetadata().getAuthors().get(0).getEmail());
        assertEquals("800-555-1212", bom.getMetadata().getAuthors().get(0).getPhone());
        assertEquals("Acme Application", bom.getMetadata().getComponent().getName());
        assertEquals("9.1.1", bom.getMetadata().getComponent().getVersion());
        assertEquals(Component.Type.APPLICATION, bom.getMetadata().getComponent().getType());
        assertNotNull(bom.getMetadata().getComponent().getSwid());
        assertEquals("swidgen-242eb18a-503e-ca37-393b-cf156ef09691_9.1.1", bom.getMetadata().getComponent().getSwid().getTagId());
        assertEquals("Acme Application", bom.getMetadata().getComponent().getSwid().getName());
        assertEquals("9.1.1", bom.getMetadata().getComponent().getSwid().getVersion());
        assertEquals(0, bom.getMetadata().getComponent().getSwid().getTagVersion());
        assertFalse(bom.getMetadata().getComponent().getSwid().isPatch());
        assertEquals("Acme, Inc.", bom.getMetadata().getManufacture().getName());
        assertEquals("https://example.com", bom.getMetadata().getManufacture().getUrls().get(0));
        assertEquals(1, bom.getMetadata().getManufacture().getContacts().size());
        assertEquals("Acme Professional Services", bom.getMetadata().getManufacture().getContacts().get(0).getName());
        assertEquals("professional.services@example.com", bom.getMetadata().getManufacture().getContacts().get(0).getEmail());
        assertEquals("Acme, Inc.", bom.getMetadata().getSupplier().getName());
        assertEquals("https://example.com", bom.getMetadata().getSupplier().getUrls().get(0));
        assertEquals(1, bom.getMetadata().getSupplier().getContacts().size());
        assertEquals("Acme Distribution", bom.getMetadata().getSupplier().getContacts().get(0).getName());
        assertEquals("distribution@example.com", bom.getMetadata().getSupplier().getContacts().get(0).getEmail());

        final List<Component> components = bom.getComponents();
        assertEquals(3, components.size());
        Component c1 = components.get(0);
        assertEquals("com.acme", c1.getGroup());
        assertEquals("tomcat-catalina", c1.getName());
        assertEquals("9.0.14", c1.getVersion());
        assertEquals(Component.Type.LIBRARY, c1.getType());
        assertEquals("pkg:npm/acme/component@1.0.0", c1.getPurl());

        Component c3 = components.get(2);
        assertEquals(Component.Type.LIBRARY, c3.getType());
        assertEquals("pkg:maven/org.glassfish.hk2/osgi-resource-locator@1.0.1?type=jar", c3.getBomRef());
        assertEquals("GlassFish Community", c3.getPublisher());
        assertEquals("org.glassfish.hk2", c3.getGroup());
        assertEquals("osgi-resource-locator", c3.getName());
        assertEquals("1.0.1", c3.getVersion());
        assertEquals( "(CDDL-1.0 OR GPL-2.0-with-classpath-exception)", c3.getLicenseChoice().getExpression());

        testPedigree(c1);

        // Begin Services
        List<Service> services = bom.getServices();
        assertEquals(1, services.size());
        Service s = services.get(0);
        OrganizationalEntity provider = s.getProvider();
        assertNotNull(provider);
        assertEquals("Partner Org", provider.getName());
        List<String> urls = provider.getUrls();
        assertEquals(1, urls.size());
        assertEquals("https://partner.org", urls.get(0));
        List<OrganizationalContact> contacts = provider.getContacts();
        assertEquals(1, contacts.size());
        OrganizationalContact contact = contacts.get(0);
        assertEquals("Support", contact.getName());
        assertEquals("support@partner", contact.getEmail());
        assertEquals("800-555-1212", contact.getPhone());
        assertEquals("org.partner", s.getGroup());
        assertEquals("Stock ticker service", s.getName());
        assertEquals("2020-Q2", s.getVersion());
        assertEquals("Provides real-time stock information", s.getDescription());
        List<String> endpoints = s.getEndpoints();
        assertEquals(2, endpoints.size());
        assertEquals("https://partner.org/api/v1/lookup", endpoints.get(0));
        assertEquals("https://partner.org/api/v1/stock", endpoints.get(1));
        assertNotNull(s.getAuthenticated());
        assertNotNull(s.getxTrustBoundary());
        assertTrue(s.getAuthenticated());
        assertTrue(s.getxTrustBoundary());
        List<ServiceData> data = s.getData();
        assertEquals(3, data.size());
        assertEquals("inbound", data.get(0).getFlow().getFlowName());
        assertEquals("PII", data.get(0).getClassification());
        assertEquals(ServiceData.Flow.OUTBOUND, data.get(1).getFlow());
        assertEquals("PIFI", data.get(1).getClassification());
        assertEquals(ServiceData.Flow.BI_DIRECTIONAL, data.get(2).getFlow());
        assertEquals("pubic", data.get(2).getClassification());
        assertNotNull(s.getLicense());
        assertEquals(1, s.getLicense().getLicenses().size());
        assertEquals("Partner license", s.getLicense().getLicenses().get(0).getName());
        assertEquals(2, s.getExternalReferences().size());
        assertEquals(ExternalReference.Type.WEBSITE, s.getExternalReferences().get(0).getType());
        assertEquals("http://partner.org", s.getExternalReferences().get(0).getUrl());
        assertEquals(ExternalReference.Type.DOCUMENTATION, s.getExternalReferences().get(1).getType());
        assertEquals("http://api.partner.org/swagger", s.getExternalReferences().get(1).getUrl());
        // End Services

        // Begin Dependencies
        assertEquals(1, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("pkg:npm/acme/component@1.0.0", d1.getRef());
        assertEquals(1, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        assertEquals("pkg:npm/acme/common@1.0.0", d11.getRef());
        assertNull(d11.getDependencies());
    }

    private void testPedigree(final Component component) {
        assertNotNull(component.getPedigree());
        assertNotNull(component.getPedigree().getAncestors());
        assertEquals(2, component.getPedigree().getAncestors().getComponents().size());
        assertNotNull(component.getPedigree().getDescendants());
        assertEquals(2, component.getPedigree().getDescendants().getComponents().size());
        assertNotNull(component.getPedigree().getVariants());
        assertEquals(2, component.getPedigree().getVariants().getComponents().size());
    }

    @Test
    public void testParsedObjects13Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.3.json"));
        final JsonParser parser = new JsonParser();
        final Bom bom = parser.parse(bomBytes);
        assertEquals("1.3", bom.getSpecVersion());
        assertEquals(3, bom.getComponents().size());
        assertEquals(1, bom.getVersion());
        assertNotNull(bom.getMetadata());
        assertNotNull(bom.getMetadata().getTimestamp());
        assertEquals(1, bom.getMetadata().getTools().size());
        assertEquals("Awesome Vendor", bom.getMetadata().getTools().get(0).getVendor());
        assertEquals("Awesome Tool", bom.getMetadata().getTools().get(0).getName());
        assertEquals("9.1.2", bom.getMetadata().getTools().get(0).getVersion());
        assertEquals(2, bom.getMetadata().getTools().get(0).getHashes().size());
        assertEquals("SHA-1", bom.getMetadata().getTools().get(0).getHashes().get(0).getAlgorithm());
        assertEquals("25ed8e31b995bb927966616df2a42b979a2717f0", bom.getMetadata().getTools().get(0).getHashes().get(0).getValue());
        assertEquals(1, bom.getMetadata().getAuthors().size());
        assertEquals("Samantha Wright", bom.getMetadata().getAuthors().get(0).getName());
        assertEquals("samantha.wright@example.com", bom.getMetadata().getAuthors().get(0).getEmail());
        assertEquals("800-555-1212", bom.getMetadata().getAuthors().get(0).getPhone());
        assertEquals("Acme Application", bom.getMetadata().getComponent().getName());
        assertEquals("9.1.1", bom.getMetadata().getComponent().getVersion());
        assertEquals(Component.Type.APPLICATION, bom.getMetadata().getComponent().getType());
        assertNotNull(bom.getMetadata().getComponent().getSwid());
        assertEquals("swidgen-242eb18a-503e-ca37-393b-cf156ef09691_9.1.1", bom.getMetadata().getComponent().getSwid().getTagId());
        assertEquals("Acme Application", bom.getMetadata().getComponent().getSwid().getName());
        assertEquals("9.1.1", bom.getMetadata().getComponent().getSwid().getVersion());
        assertEquals(0, bom.getMetadata().getComponent().getSwid().getTagVersion());
        assertFalse(bom.getMetadata().getComponent().getSwid().isPatch());
        assertEquals("Acme, Inc.", bom.getMetadata().getManufacture().getName());
        assertEquals("https://example.com", bom.getMetadata().getManufacture().getUrls().get(0));
        assertEquals(1, bom.getMetadata().getManufacture().getContacts().size());
        assertEquals("Acme Professional Services", bom.getMetadata().getManufacture().getContacts().get(0).getName());
        assertEquals("professional.services@example.com", bom.getMetadata().getManufacture().getContacts().get(0).getEmail());
        assertEquals("Acme, Inc.", bom.getMetadata().getSupplier().getName());
        assertEquals("https://example.com", bom.getMetadata().getSupplier().getUrls().get(0));
        assertEquals(1, bom.getMetadata().getSupplier().getContacts().size());
        assertEquals("Acme Distribution", bom.getMetadata().getSupplier().getContacts().get(0).getName());
        assertEquals("distribution@example.com", bom.getMetadata().getSupplier().getContacts().get(0).getEmail());

        final List<Component> components = bom.getComponents();
        assertEquals(3, components.size());
        Component c1 = components.get(0);
        assertEquals("com.acme", c1.getGroup());
        assertEquals("tomcat-catalina", c1.getName());
        assertEquals("9.0.14", c1.getVersion());
        assertEquals(Component.Type.LIBRARY, c1.getType());
        assertEquals("pkg:npm/acme/component@1.0.0", c1.getPurl());

        Component c3 = components.get(2);
        assertEquals(Component.Type.LIBRARY, c3.getType());
        assertEquals("pkg:maven/org.glassfish.hk2/osgi-resource-locator@1.0.1?type=jar", c3.getBomRef());
        assertEquals("GlassFish Community", c3.getPublisher());
        assertEquals("org.glassfish.hk2", c3.getGroup());
        assertEquals("osgi-resource-locator", c3.getName());
        assertEquals("1.0.1", c3.getVersion());
        assertEquals( "(CDDL-1.0 OR GPL-2.0-with-classpath-exception)", c3.getLicenseChoice().getExpression());

        testPedigree(c1);

        // Begin Services
        List<Service> services = bom.getServices();
        assertEquals(1, services.size());
        Service s = services.get(0);
        OrganizationalEntity provider = s.getProvider();
        assertNotNull(provider);
        assertEquals("Partner Org", provider.getName());
        List<String> urls = provider.getUrls();
        assertEquals(1, urls.size());
        assertEquals("https://partner.org", urls.get(0));
        List<OrganizationalContact> contacts = provider.getContacts();
        assertEquals(1, contacts.size());
        OrganizationalContact contact = contacts.get(0);
        assertEquals("Support", contact.getName());
        assertEquals("support@partner", contact.getEmail());
        assertEquals("800-555-1212", contact.getPhone());
        assertEquals("org.partner", s.getGroup());
        assertEquals("Stock ticker service", s.getName());
        assertEquals("2020-Q2", s.getVersion());
        assertEquals("Provides real-time stock information", s.getDescription());
        List<String> endpoints = s.getEndpoints();
        assertEquals(2, endpoints.size());
        assertEquals("https://partner.org/api/v1/lookup", endpoints.get(0));
        assertEquals("https://partner.org/api/v1/stock", endpoints.get(1));
        assertNotNull(s.getAuthenticated());
        assertNotNull(s.getxTrustBoundary());
        assertTrue(s.getAuthenticated());
        assertTrue(s.getxTrustBoundary());
        List<ServiceData> data = s.getData();
        assertEquals(3, data.size());
        assertEquals("inbound", data.get(0).getFlow().getFlowName());
        assertEquals("PII", data.get(0).getClassification());
        assertEquals(ServiceData.Flow.OUTBOUND, data.get(1).getFlow());
        assertEquals("PIFI", data.get(1).getClassification());
        assertEquals(ServiceData.Flow.BI_DIRECTIONAL, data.get(2).getFlow());
        assertEquals("pubic", data.get(2).getClassification());
        assertNotNull(s.getLicense());
        assertEquals(1, s.getLicense().getLicenses().size());
        assertEquals("Partner license", s.getLicense().getLicenses().get(0).getName());
        assertEquals(2, s.getExternalReferences().size());
        assertEquals(ExternalReference.Type.WEBSITE, s.getExternalReferences().get(0).getType());
        assertEquals("http://partner.org", s.getExternalReferences().get(0).getUrl());
        assertEquals(ExternalReference.Type.DOCUMENTATION, s.getExternalReferences().get(1).getType());
        assertEquals("http://api.partner.org/swagger", s.getExternalReferences().get(1).getUrl());
        // End Services

        // Begin Dependencies
        assertEquals(1, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("pkg:npm/acme/component@1.0.0", d1.getRef());
        assertEquals(1, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        assertEquals("pkg:npm/acme/common@1.0.0", d11.getRef());
        assertNull(d11.getDependencies());
    }

    @Test
    public void testParsedObjects14Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.4.json"));
        final JsonParser parser = new JsonParser();
        final Bom bom = parser.parse(bomBytes);

        assertNotNull(bom);
    }
}
