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
import org.cyclonedx.CycloneDxSchema.Version;
import org.cyclonedx.model.Annotation;
import org.cyclonedx.model.Annotator;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Composition;
import org.cyclonedx.model.Composition.Aggregate;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.Licensing;
import org.cyclonedx.model.LifecycleChoice;
import org.cyclonedx.model.LifecycleChoice.Phase;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.OrganizationalChoice;
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

public class JsonParserTest {

    @Test
    public void testValid12Bom() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.2.json").getFile());
        final JsonParser parser = new JsonParser();
        Bom bom = parser.parse(file);
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_12));
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
        assertEquals(2, bom.getDependencies().size());
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
        assertEquals(2, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("pkg:npm/acme/component@1.0.0", d1.getRef());
        assertEquals(1, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        assertEquals("pkg:npm/acme/common@1.0.0", d11.getRef());
        assertNull(d11.getDependencies());
        Dependency d2 = bom.getDependencies().get(1);
        assertEquals("pkg:npm/acme/component@2.0.0", d2.getRef());
        assertEquals(3, d2.getDependencies().size());
        Dependency d21 = d2.getDependencies().get(0);
        assertEquals("pkg:npm/acme/common@1.1.0", d21.getRef());
        Dependency d22 = d2.getDependencies().get(1);
        assertEquals("pkg:npm/acme/common@2.2.0", d22.getRef());
    }

    @Test
    public void testValidBomLink() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.4-bomlink.json").getFile());
        final JsonParser parser = new JsonParser();
        Bom bom = parser.parse(file);
        assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_14));
        ExternalReference ref = bom.getComponents().get(0).getExternalReferences().get(0);
        assertEquals("bom", ref.getType().getTypeName());
        assertEquals("urn:cdx:f08a6ccd-4dce-4759-bd84-c626675d60a7/1", ref.getUrl());
    }

    @Test
    public void testParsedObjects14Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.4.json"));
        final JsonParser parser = new JsonParser();
        final Bom bom = parser.parse(bomBytes);

        assertEquals("1.4", bom.getSpecVersion());
        assertEquals(1, bom.getVersion());

        assertMetadata(bom.getMetadata(), Version.VERSION_14);
        assertComponent(bom, Version.VERSION_14);
        assertServices(bom);
        assertCompositions(bom, Version.VERSION_14);
        assertVulnerabilities(bom, Version.VERSION_14);

        // Dependencies
        assertEquals(2, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("pkg:npm/acme/component@1.0.0", d1.getRef());

        //Assert Bom Properties
        assertNull(bom.getProperties());

        //Assert Annotations
        assertAnnotations(bom, Version.VERSION_14);
    }

    @Test
    public void testParsedObjects15Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.5.json"));
        final JsonParser parser = new JsonParser();
        final Bom bom = parser.parse(bomBytes);

        assertEquals("1.5", bom.getSpecVersion());
        assertEquals(1, bom.getVersion());

        assertMetadata(bom.getMetadata(), Version.VERSION_15);
        assertComponent(bom, Version.VERSION_15);
        assertServices(bom);
        assertCompositions(bom, Version.VERSION_15);
        assertVulnerabilities(bom, Version.VERSION_15);

        // Dependencies
        assertEquals(2, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("pkg:npm/acme/component@1.0.0", d1.getRef());

        //Assert Bom Properties
        assertEquals(bom.getProperties().size(), 1);

        //Assert Annotations
        assertAnnotations(bom, Version.VERSION_15);
    }


    private void assertAnnotations(final Bom bom, final Version version) {

        if(version== Version.VERSION_15) {
            List<Annotation> annotations = bom.getAnnotations();

            assertEquals(annotations.size(), 1);

            Annotation annotation = annotations.get(0);
            assertNotNull(annotation.getBomRef());
            assertNotNull(annotation.getText());
            assertNotNull(annotation.getTimestamp());

            assertEquals(annotation.getSubjects().size(), 1);
            assertAnnotator(annotation.getAnnotator());
        } else {
            assertNull(bom.getAnnotations());
        }
    }

    private void assertAnnotator(final Annotator annotator) {
        assertNotNull(annotator);
        assertNull(annotator.getIndividual());
        assertNull(annotator.getComponent());
        assertNull(annotator.getService());

        assertNotNull(annotator.getOrganization());
        assertEquals(annotator.getOrganization().getName(), "Acme, Inc.");
        assertEquals(annotator.getOrganization().getContacts().size(), 1);
        assertEquals(annotator.getOrganization().getUrls().size(), 1);
    }

    private void assertCompositions(final Bom bom, final Version version) {
        final List<Composition> compositions = bom.getCompositions();
        assertEquals(2, compositions.size());
        Composition composition = compositions.get(0);

        assertEquals(composition.getAggregate(), Aggregate.COMPLETE);
        assertNotNull(composition.getAssemblies());
        assertNotNull(composition.getDependencies());

        //Assert Vulnerability Rejected
        if (version == Version.VERSION_15) {
            assertNotNull(composition.getVulnerabilities());
            assertEquals("6eee14da-8f42-4cc4-bb65-203235f02415", composition.getBomRef());
        }
        else {
            assertNull(composition.getVulnerabilities());
            assertNull(composition.getBomRef());
        }
    }

    private void assertVulnerabilities(final Bom bom, final Version version) {
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

        //Assert Vulnerability Rejected
        if (version != Version.VERSION_14) {
            assertNotNull(vuln.getRejected());
        }
        else {
            assertNull(vuln.getRejected());
        }

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

        //Vulnerability Analysis Timestamp 1.5
        if (version != Version.VERSION_14) {
            assertNotNull(vuln.getAnalysis().getFirstIssued());
            assertNotNull(vuln.getAnalysis().getLastUpdated());
        } else {
            assertNull(vuln.getAnalysis().getFirstIssued());
            assertNull(vuln.getAnalysis().getLastUpdated());
        }

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

    private void assertComponent(final Bom bom, final Version version) {
        final List<Component> components = bom.getComponents();
        assertEquals(1, components.size());
        Component component = components.get(0);
        assertEquals("com.acme", component.getGroup());
        assertEquals("jackson-databind", component.getName());
        assertEquals("2.9.4", component.getVersion());
        assertEquals(Component.Type.LIBRARY, component.getType());
        assertEquals("pkg:maven/com.acme/jackson-databind@2.9.4", component.getPurl());
        assertEquals(12, component.getHashes().size());

        //Licenses
        assertLicense(component.getLicenseChoice(), version);

        assertEquals("Copyright Example Inc. All rights reserved.", component.getCopyright());
        assertEquals("Acme Application", component.getSwid().getName());
        assertEquals("9.1.1", component.getSwid().getVersion());
        assertEquals("swidgen-242eb18a-503e-ca37-393b-cf156ef09691_9.1.1", component.getSwid().getTagId());

        if (version == Version.VERSION_14) {
            assertEquals(1, component.getExternalReferences().size());
        }
        else {
            assertEquals(2, component.getExternalReferences().size());
            //Security Contact
            assertSecurityContact(component.getExternalReferences().get(1));
        }

        //Evidence
        assertNotNull(component.getEvidence());
        assertEquals("Copyright 2012 Google Inc. All Rights Reserved.",
            component.getEvidence().getCopyright().get(0).getText());
        assertEquals("Apache-2.0", component.getEvidence().getLicenseChoice().getLicenses().get(0).getId());
        assertEquals("http://www.apache.org/licenses/LICENSE-2.0",
            component.getEvidence().getLicenseChoice().getLicenses().get(0).getUrl());
    }

    private void assertSecurityContact(ExternalReference externalReference) {
        assertEquals(externalReference.getType(), ExternalReference.Type.SECURITY_CONTACT);
        assertEquals(externalReference.getComment(), "test");
        assertEquals(externalReference.getUrl(), "http://example.org/docs");
    }

    private void assertLicense(LicenseChoice licenseChoice, final Version version) {
        assertNotNull(licenseChoice);
        assertNull(licenseChoice.getExpression());

        assertEquals(1, licenseChoice.getLicenses().size());
        License license = licenseChoice.getLicenses().get(0);
        assertNotNull(license);


        if (version == Version.VERSION_14) {
            assertNull(license.getProperties());
            assertNull(license.getBomRef());
        }
        else {
            //Dev Licensing
            assertLicensing(license.getLicensing());
            assertEquals(license.getProperties().size(), 1);
            assertNotNull(license.getBomRef());
        }
    }

    private void assertLicensing(final Licensing licensing) {
        assertNotNull(licensing);

        assertEquals(2, licensing.getAltIds().size());
        assertNotNull(licensing.getPurchaseOrder());
        assertLicensor(licensing.getLicensor());
        assertLicensee(licensing.getLicensee());
        assertPurchaser(licensing.getPurchaser());
        assertNotNull(licensing.getExpiration());
        assertNotNull(licensing.getLastRenewal());
        assertEquals(licensing.getLicenseTypes().size(), 1);
    }

    private void assertLicensor(OrganizationalChoice licensor) {
        assertNull(licensor.getIndividual());
        assertNotNull(licensor.getOrganization());
        assertEquals(licensor.getOrganization().getName(), "Acme Inc");
        assertEquals(licensor.getOrganization().getContacts().size(), 2);
        assertNull(licensor.getOrganization().getUrls());
    }

    private void assertLicensee(OrganizationalChoice licensee) {
        assertNull(licensee.getIndividual());
        assertNotNull(licensee.getOrganization());
        assertEquals(licensee.getOrganization().getName(), "Example Co.");
        assertNull(licensee.getOrganization().getContacts());
        assertNull(licensee.getOrganization().getUrls());
    }

    private void assertPurchaser(OrganizationalChoice purchaser) {
        assertNull(purchaser.getOrganization());
        assertNotNull(purchaser.getIndividual());
        assertEquals(purchaser.getIndividual().getName(), "Samantha Wright");
        assertEquals(purchaser.getIndividual().getEmail(), "samantha.wright@gmail.com");
        assertEquals(purchaser.getIndividual().getPhone(), "800-555-1212");
    }

    private void assertMetadata(final Metadata metadata, final Version version) {
        assertNotNull(metadata);
        assertNotNull(metadata.getTimestamp());

        //Lifecycles
        if(version == Version.VERSION_15) {
            assertNotNull(metadata.getLifecycles());
            assertEquals(2, metadata.getLifecycles().getLifecycleChoice().size());
            LifecycleChoice firstChoice = metadata.getLifecycles().getLifecycleChoice().get(0);
            assertEquals(Phase.BUILD.getPhaseName(), firstChoice.getPhase().getPhaseName());
            assertNull(firstChoice.getName());
            assertNull(firstChoice.getDescription());

            LifecycleChoice secondChoice = metadata.getLifecycles().getLifecycleChoice().get(1);
            assertEquals("platform-integration-testing", secondChoice.getName());
            assertNotNull(secondChoice.getDescription());
            assertNull(secondChoice.getPhase());
        } else {
            assertNull(metadata.getLifecycles());
        }

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
}
