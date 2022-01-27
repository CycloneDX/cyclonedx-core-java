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
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Pedigree;
import org.cyclonedx.model.ReleaseNotes;
import org.cyclonedx.model.ReleaseNotes.Notes;
import org.cyclonedx.model.ReleaseNotes.Resolves;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.ServiceData;
import org.cyclonedx.model.vulnerability.Vulnerability;
import org.cyclonedx.model.vulnerability.Vulnerability.Analysis.Justification;
import org.cyclonedx.model.vulnerability.Vulnerability.Analysis.State;
import org.cyclonedx.model.vulnerability.Vulnerability.Rating.Method;
import org.cyclonedx.model.vulnerability.Vulnerability.Rating.Severity;
import org.cyclonedx.model.vulnerability.Vulnerability.Version.Status;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("deprecation")
public class XmlParserTest {

    @Test
    public void testValid10Bom() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.0.xml").getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_10);
        assertTrue(valid);
    }

    @Test
    public void testValid11Bom() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.1.xml").getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_11);
        assertTrue(valid);
    }

    @Test
    public void testValid11BomWithDependencyGraph10() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.1-dependency-graph-1.0.xml").getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_11);
        assertTrue(valid);
    }

    @Test
    public void testValid11BomWithVulnerability10() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.1-vulnerability-1.0.xml").getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_11);
        assertTrue(valid);
    }

    @Test
    public void testValid12Bom() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.2.xml").getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_12);
        assertTrue(valid);
    }

    @Test
    public void testValid12BomWithPedigree() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.2-pedigree.xml").getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_12);
        assertTrue(valid);

        final Bom bom = parser.parse(file);
        testPedigree(bom.getComponents().get(0).getPedigree());
    }

    private void testPedigree(final Pedigree pedigree) {
        assertEquals("sample-library-ancestor", pedigree.getAncestors().getComponents().get(0).getName());
        assertEquals("sample-library-descendant", pedigree.getDescendants().getComponents().get(0).getName());
        assertEquals("sample-library-variant", pedigree.getVariants().getComponents().get(0).getName());
    }

    @Test
    public void testValid12BomWithPedigreeWithPatches() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.2-pedigree-example.xml").getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_12);
        assertTrue(valid);

        final Bom bom = parser.parse(file);
        testPedigreeFromExample(bom.getComponents().get(0).getPedigree());
    }

    private void testPedigreeFromExample(final Pedigree pedigree) {
        assertEquals(2, pedigree.getPatches().size());
        assertEquals(2, pedigree.getPatches().get(1).getResolves().size());
    }

    @Test
    public void testParsedObjects10Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.0.xml"));
        final XmlParser parser = new XmlParser();
        final Bom bom = parser.parse(bomBytes);
        assertEquals(1, bom.getComponents().size());
        assertEquals("1.0", bom.getSpecVersion());
        assertEquals(1, bom.getVersion());
        final List<Component> components = bom.getComponents();
        assertEquals(1, components.size());
        Component c1 = components.get(0);
        assertEquals("org.example", c1.getGroup());
        assertEquals("1.0.0", c1.getVersion());
        assertEquals(Component.Type.APPLICATION, c1.getType());
        assertEquals("2342c2eaf1feb9a80195dbaddf2ebaa3", c1.getHashes().get(0).getValue());
        assertEquals("68b78babe00a053f9e35ec6a2d9080f5b90122b0", c1.getHashes().get(1).getValue());
        assertEquals("708f1f53b41f11f02d12a11b1a38d2905d47b099afc71a0f1124ef8582ec7313", c1.getHashes().get(2).getValue());
        assertEquals("387b7ae16b9cae45f830671541539bf544202faae5aac544a93b7b0a04f5f846fa2f4e81ef3f1677e13aed7496408a441f5657ab6d54423e56bf6f38da124aef", c1.getHashes().get(3).getValue());
        assertEquals("cpe:/a:example:myapplication:1.0.0", c1.getCpe());
        assertEquals("pkg:maven/com.example/myapplication@1.0.0?packaging=war", c1.getPurl());
        assertEquals("An example application", c1.getDescription());
        assertEquals("Copyright Example Inc. All rights reserved.", c1.getCopyright());
        assertEquals("Apache-2.0", c1.getLicenseChoice().getLicenses().get(0).getId());
        assertEquals(2, c1.getComponents().size());
    }

    @Test
    public void testParsedObjects11Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.1.xml"));
        final XmlParser parser = new XmlParser();
        final Bom bom = parser.parse(bomBytes);
        assertEquals(3, bom.getComponents().size());
        assertEquals("1.1", bom.getSpecVersion());
        assertEquals(1, bom.getVersion());
        final List<Component> components = bom.getComponents();
        assertEquals(3, components.size());
        Component c1 = components.get(0);
        Component c2 = components.get(1);
        assertEquals("Acme Inc", c1.getPublisher());
        assertEquals("com.acme", c1.getGroup());
        assertEquals("tomcat-catalina", c1.getName());
        assertEquals("9.0.14", c1.getVersion());
        assertEquals("Modified version of Apache Catalina", c1.getDescription());
        assertEquals(Component.Type.APPLICATION, c1.getType());
        assertEquals("required", c1.getScope().getScopeName());
        assertEquals("3942447fac867ae5cdb3229b658f4d48", c1.getHashes().get(0).getValue());
        assertEquals("e6b1000b94e835ffd37f4c6dcbdad43f4b48a02a", c1.getHashes().get(1).getValue());
        assertEquals("f498a8ff2dd007e29c2074f5e4b01a9a01775c3ff3aeaf6906ea503bc5791b7b", c1.getHashes().get(2).getValue());
        assertEquals("e8f33e424f3f4ed6db76a482fde1a5298970e442c531729119e37991884bdffab4f9426b7ee11fccd074eeda0634d71697d6f88a460dce0ac8d627a29f7d1282", c1.getHashes().get(3).getValue());
        assertEquals("pkg:maven/com.acme/tomcat-catalina@9.0.14?packaging=jar", c1.getPurl());
        assertEquals("Apache-2.0", c1.getLicenseChoice().getLicenses().get(0).getId());
        assertTrue(c1.getLicenseChoice().getLicenses().get(0).getAttachmentText().getText().startsWith("CiAgICA"));
        assertEquals("text/plain", c1.getLicenseChoice().getLicenses().get(0).getAttachmentText().getContentType());
        assertEquals("base64", c1.getLicenseChoice().getLicenses().get(0).getAttachmentText().getEncoding());
        assertNotNull(c1.getPedigree());

        assertEquals(1, c1.getPedigree().getAncestors().getComponents().size());
        assertNull(c1.getPedigree().getDescendants());
        assertNull(c1.getPedigree().getVariants());
        assertEquals(1, c1.getPedigree().getCommits().size());
        assertEquals("7638417db6d59f3c431d3e1f261cc637155684cd", c1.getPedigree().getCommits().get(0).getUid());
        assertEquals("https://location/to/7638417db6d59f3c431d3e1f261cc637155684cd", c1.getPedigree().getCommits().get(0).getUrl());
        assertEquals("John Doe", c1.getPedigree().getCommits().get(0).getAuthor().getName());
        assertEquals("john.doe@example.com", c1.getPedigree().getCommits().get(0).getAuthor().getEmail());
        assertNotNull(c1.getPedigree().getCommits().get(0).getAuthor().getTimestamp());
        assertEquals("Jane Doe", c1.getPedigree().getCommits().get(0).getCommitter().getName());
        assertEquals("jane.doe@example.com", c1.getPedigree().getCommits().get(0).getCommitter().getEmail());
        assertNotNull(c1.getPedigree().getCommits().get(0).getCommitter().getTimestamp());
        assertEquals("Initial commit", c1.getPedigree().getCommits().get(0).getMessage());
        assertEquals("Commentary here", c1.getPedigree().getNotes());
        assertEquals("EPL-2.0 OR GPL-2.0-with-classpath-exception", c2.getLicenseChoice().getExpression());
    }

    @Test
    public void testParsedObjects11BomWithDependencyGraph10() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.1-dependency-graph-1.0.xml"));
        final XmlParser parser = new XmlParser();
        final Bom bom = parser.parse(bomBytes);
        assertEquals(3, bom.getComponents().size());
        assertEquals("1.1", bom.getSpecVersion());
        assertEquals(1, bom.getVersion());
        final List<Component> components = bom.getComponents();
        assertEquals(3, components.size());
        Component c1 = components.get(0);
        //Component c2 = components.get(1);
        assertEquals("org.example.acme", c1.getGroup());
        assertEquals("web-framework", c1.getName());
        assertEquals("1.0.0", c1.getVersion());
        assertEquals(Component.Type.FRAMEWORK, c1.getType());
        assertEquals("pkg:maven/org.example.acme/web-framework@1.0.0", c1.getPurl());
        assertEquals(3, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        Dependency d2 = bom.getDependencies().get(1);
        Dependency d3 = bom.getDependencies().get(2);
        assertNotNull(d1);
        assertEquals("pkg:maven/org.example.acme/web-framework@1.0.0", d1.getRef());
        assertEquals(2, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        assertEquals("pkg:maven/org.example.acme/common-util@3.0.0", d11.getRef());
        assertNull(d11.getDependencies());
        assertEquals(1, d2.getDependencies().size());
        assertNull(d3.getDependencies());
    }

    @Test
    public void testParsedObjects12Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.2.xml"));
        final XmlParser parser = new XmlParser();
        final Bom bom = parser.parse(bomBytes);
        assertEquals(3, bom.getComponents().size());
        assertEquals("1.2", bom.getSpecVersion());
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
        assertEquals(Component.Type.APPLICATION, c1.getType());
        assertEquals("pkg:maven/com.acme/tomcat-catalina@9.0.14?packaging=jar", c1.getPurl());
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
        assertEquals("acme-app", d1.getRef());
        assertEquals(2, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        assertEquals("pkg:maven/org.acme/web-framework@1.0.0", d11.getRef());
        assertNull(d11.getDependencies());
    }

    @Test
    public void testParsedObjects13Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.3.xml"));
        final XmlParser parser = new XmlParser();
        final Bom bom = parser.parse(bomBytes);
        assertEquals(3, bom.getComponents().size());
        assertEquals("1.3", bom.getSpecVersion());
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
        assertEquals(Component.Type.APPLICATION, c1.getType());
        assertEquals("pkg:maven/com.acme/tomcat-catalina@9.0.14?packaging=jar", c1.getPurl());
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
        assertEquals("acme-app", d1.getRef());
        assertEquals(2, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        assertEquals("pkg:maven/org.acme/web-framework@1.0.0", d11.getRef());
        assertNull(d11.getDependencies());
    }

    @Test
    public void testParsedObjects14Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.4.xml"));
        final XmlParser parser = new XmlParser();
        final Bom bom = parser.parse(bomBytes);

        assertEquals("1.4", bom.getSpecVersion());
        assertEquals(1, bom.getVersion());

        assertMetadata(bom.getMetadata());
        assertComponent(bom);
        assertServices(bom);
        assertVulnerabilities(bom);

        // Dependencies
        assertEquals(1, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("pkg:maven/com.acme/jackson-databind@2.9.4", d1.getRef());
    }

    private void assertVulnerabilities(final Bom bom) {
        final List<Vulnerability> vulnerabilities = bom.getVulnerabilities();
        assertEquals(1, vulnerabilities.size());
        Vulnerability vuln = vulnerabilities.get(0);

        assertEquals("6eee14da-8f42-4cc4-bb65-203235f02415", vuln.getBomRef());
        assertEquals("SONATYPE-2021123", vuln.getId());
        assertEquals("Description", vuln.getDescription());
        assertEquals("Detail", vuln.getDetail());
        assertEquals("Upgrade", vuln.getRecommendation());
        assertEquals(184, vuln.getCwes().get(0));
        assertNotNull(vuln.getCreated());
        assertNotNull(vuln.getPublished());
        assertNotNull(vuln.getUpdated());

        //Source
        assertEquals("Sonatype", vuln.getSource().getName());
        assertEquals("https://www.vuln.com", vuln.getSource().getUrl());

        //References
        assertEquals(1, vuln.getReferences().size());
        assertEquals("CVE-2018-7489", vuln.getReferences().get(0).getId());
        assertEquals("NVD", vuln.getReferences().get(0).getSource().getName());
        assertEquals("https://nvd.nist.gov/vuln/detail/CVE-2019-9997",
            vuln.getReferences().get(0).getSource().getUrl());

        //Ratings
        assertEquals(1, vuln.getRatings().size());
        assertEquals("NVD", vuln.getRatings().get(0).getSource().getName());
        assertEquals(
            "https://nvd.nist.gov/vuln-metrics/cvss/v3-calculator?vector=AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H&version=3.0",
            vuln.getRatings().get(0).getSource().getUrl());
        assertEquals(9.8, vuln.getRatings().get(0).getScore());
        assertEquals(Severity.CRITICAL, vuln.getRatings().get(0).getSeverity());
        assertEquals(Method.CVSSV3, vuln.getRatings().get(0).getMethod());
        assertEquals("AN/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H", vuln.getRatings().get(0).getVector());
        assertEquals("An optional reason for rating the vulnerability as it was",
            vuln.getRatings().get(0).getJustification());

        //Advisories
        assertEquals(1, vuln.getAdvisories().size());
        assertEquals("GitHub Commit", vuln.getAdvisories().get(0).getTitle());
        assertEquals("https://github.com/FasterXML/jackson-databind/commit/6799f8f10cc78e9af6d443ed6982d00a13f2e7d2",
            vuln.getAdvisories().get(0).getUrl());

        //Credits
        assertEquals(1, vuln.getCredits().getIndividuals().size());
        assertEquals(1, vuln.getCredits().getOrganizations().size());

        assertEquals("Jane Doe", vuln.getCredits().getIndividuals().get(0).getName());
        assertEquals("jane.doe@example.com", vuln.getCredits().getIndividuals().get(0).getEmail());

        assertEquals("Acme, Inc.", vuln.getCredits().getOrganizations().get(0).getName());
        assertEquals("https://example.com", vuln.getCredits().getOrganizations().get(0).getUrls().get(0));

        //Tools
        assertEquals(1, vuln.getTools().size());
        assertEquals("Sonatype CLI", vuln.getTools().get(0).getName());
        assertEquals("Sonatype", vuln.getTools().get(0).getVendor());
        assertEquals("1.131", vuln.getTools().get(0).getVersion());
        assertEquals(1, vuln.getTools().get(0).getHashes().size());

        //Analysis
        assertEquals(State.NOT_AFFECTED, vuln.getAnalysis().getState());
        assertEquals(Justification.CODE_NOT_REACHABLE, vuln.getAnalysis().getJustification());
        assertEquals("An optional explanation of why the application is not affected by the vulnerable component.",
            vuln.getAnalysis().getDetail());
        assertEquals("update", vuln.getAnalysis().getResponses().get(0).getResponseName());

        //Affects
        assertEquals(1, vuln.getAffects().size());
        assertEquals("pkg:maven/com.acme/jackson-databind@2.9.9", vuln.getAffects().get(0).getRef());

        assertEquals("vers:semver/<2.6.7.5", vuln.getAffects().get(0).getVersions().get(0).getRange());
        assertEquals(Status.AFFECTED, vuln.getAffects().get(0).getVersions().get(0).getStatus());

        assertEquals("2.9.9", vuln.getAffects().get(0).getVersions().get(1).getVersion());
        assertEquals(Status.AFFECTED, vuln.getAffects().get(0).getVersions().get(1).getStatus());
    }

    private void assertServices(final Bom bom) {
        //Services
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
        assertEquals(4, data.size());
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
    }

    private void assertComponent(final Bom bom) {
        final List<Component> components = bom.getComponents();
        assertEquals(1, components.size());
        Component component = components.get(0);
        assertEquals("com.acme", component.getGroup());
        assertEquals("jackson-databind", component.getName());
        assertEquals("2.9.4", component.getVersion());
        assertEquals(Component.Type.LIBRARY, component.getType());
        assertEquals("pkg:maven/com.acme/jackson-databind@2.9.4", component.getPurl());
        assertEquals(12, component.getHashes().size());
        assertNotNull(component.getLicenseChoice().getExpression());
        assertEquals("Copyright Example Inc. All rights reserved.", component.getCopyright());
        assertEquals("Acme Application", component.getSwid().getName());
        assertEquals("9.1.1", component.getSwid().getVersion());
        assertEquals("swidgen-242eb18a-503e-ca37-393b-cf156ef09691_9.1.1", component.getSwid().getTagId());
        assertEquals(1, component.getExternalReferences().size());

        //Evidence
        assertNotNull(component.getEvidence());
        assertEquals("Copyright 2012 Google Inc. All Rights Reserved.",
            component.getEvidence().getCopyright().get(0).getText());
        assertEquals("Apache-2.0", component.getEvidence().getLicenseChoice().getLicenses().get(0).getId());
        assertEquals("http://www.apache.org/licenses/LICENSE-2.0",
            component.getEvidence().getLicenseChoice().getLicenses().get(0).getUrl());
    }

    private void assertMetadata(final Metadata metadata) {
        assertNotNull(metadata);
        assertNotNull(metadata.getTimestamp());

        //Tool
        assertEquals(1, metadata.getTools().size());
        assertEquals("Awesome Vendor", metadata.getTools().get(0).getVendor());
        assertEquals("Awesome Tool", metadata.getTools().get(0).getName());
        assertEquals("9.1.2", metadata.getTools().get(0).getVersion());
        assertEquals(1, metadata.getTools().get(0).getHashes().size());
        assertEquals("SHA-1", metadata.getTools().get(0).getHashes().get(0).getAlgorithm());
        assertEquals("25ed8e31b995bb927966616df2a42b979a2717f0",
            metadata.getTools().get(0).getHashes().get(0).getValue());

        //Author
        assertEquals(1, metadata.getAuthors().size());
        assertEquals("Samantha Wright", metadata.getAuthors().get(0).getName());
        assertEquals("samantha.wright@example.com", metadata.getAuthors().get(0).getEmail());
        assertEquals("800-555-1212", metadata.getAuthors().get(0).getPhone());

        //Component
        Component component = metadata.getComponent();
        assertEquals("Acme Application", component.getName());
        assertEquals("9.1.1", component.getVersion());
        assertEquals(Component.Type.APPLICATION, component.getType());
        assertNotNull(component.getSwid());
        assertEquals("swidgen-242eb18a-503e-ca37-393b-cf156ef09691_9.1.1", component.getSwid().getTagId());
        assertEquals("Acme Application", component.getSwid().getName());
        assertEquals("9.1.1", component.getSwid().getVersion());
        assertEquals(0, component.getSwid().getTagVersion());
        assertFalse(component.getSwid().isPatch());

        //Release Notes
        ReleaseNotes releaseNotes = metadata.getComponent().getReleaseNotes();
        assertEquals("major", releaseNotes.getType());
        assertEquals("My new release", releaseNotes.getTitle());
        assertNotNull(releaseNotes.getDescription());
        assertNotNull(releaseNotes.getSocialImage());
        assertNotNull(releaseNotes.getTimestamp());
        assertEquals(1, releaseNotes.getAliases().size());
        assertEquals(1, releaseNotes.getTags().size());

        //Resolves
        assertEquals(1, releaseNotes.getResolves().size());
        Resolves resolves = releaseNotes.getResolves().get(0);
        assertEquals(Resolves.Type.SECURITY, resolves.getType());
        assertEquals("CVE-2019-9997", resolves.getId());
        assertEquals("A security issue was fixed that did something bad", resolves.getDescription());
        assertEquals("CVE-2019-9997", resolves.getName());

        //Notes
        assertEquals(1, releaseNotes.getNotes().size());
        Notes note = releaseNotes.getNotes().get(0);
        assertEquals("en-US", note.getLocale());
        assertNotNull(note.getText());
        assertEquals("PGgxPk15IG5ldyByZWxlYXNlPGgxPgo8cD5SZWxlYXNlIG5vdGVzIGhlcmU8L3A+", note.getText().getText());
        assertEquals("text/html", note.getText().getContentType());
        assertEquals("base64", note.getText().getEncoding());

        //Manufacture
        assertEquals("Acme, Inc.", metadata.getManufacture().getName());
        assertEquals("https://example.com", metadata.getManufacture().getUrls().get(0));
        assertEquals(1, metadata.getManufacture().getContacts().size());
        assertEquals("Acme Professional Services", metadata.getManufacture().getContacts().get(0).getName());
        assertEquals("professional.services@example.com", metadata.getManufacture().getContacts().get(0).getEmail());
        assertEquals("Acme, Inc.", metadata.getSupplier().getName());

        //Supplier
        assertEquals("https://example.com", metadata.getSupplier().getUrls().get(0));
        assertEquals(1, metadata.getSupplier().getContacts().size());
        assertEquals("Acme Distribution", metadata.getSupplier().getContacts().get(0).getName());
        assertEquals("distribution@example.com", metadata.getSupplier().getContacts().get(0).getEmail());
    }

    @Test
    public void test13PrereleasePropertyBom() throws Exception {
        final File file = new File(this.getClass().getResource("/1.3/valid-properties-1.3-prerelease.xml").getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_13);
        assertFalse(valid);
        final Bom bom1 = parser.parse(file);
        Component c1 = bom1.getComponents().get(0);
        assertEquals(2, c1.getProperties().size());
        assertEquals("Foo", c1.getProperties().get(0).getName());
        assertNull(c1.getProperties().get(0).getValue());

        System.setProperty("cyclonedx.prerelease.13.properties", "true");
        final Bom bom2 = parser.parse(file);
        Component c2 = bom2.getComponents().get(0);
        assertEquals(2, c2.getProperties().size());
        assertEquals("Foo", c2.getProperties().get(0).getName());
        assertEquals("Bar", c2.getProperties().get(0).getValue());
    }
}
