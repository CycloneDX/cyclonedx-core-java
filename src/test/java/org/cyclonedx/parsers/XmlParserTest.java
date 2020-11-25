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
package org.cyclonedx.parsers;

import org.apache.commons.io.IOUtils;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.ExtensibleExtension;
import org.cyclonedx.model.vulnerability.Vulnerability1_0;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class XmlParserTest {

    @Test
    public void testValid10Bom() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.0.xml").getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_10);
        Assert.assertTrue(valid);
    }

    @Test
    public void testValid11Bom() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.1.xml").getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_11);
        Assert.assertTrue(valid);
    }

    @Test
    public void testValid11BomWithDependencyGraph10() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.1-dependency-graph-1.0.xml").getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_11);
        Assert.assertTrue(valid);
    }

    @Test
    public void testValid11BomWithVulnerability10() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.1-vulnerability-1.0.xml").getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_11);
        Assert.assertTrue(valid);
    }

    @Test
    public void testValid12BomWithVulnerability10() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.2-vulnerability-1.0.xml").getFile());
        final XmlParser parser = new XmlParser();
        Bom bom = parser.parse(file);
        bom.getComponents().get(0).getExtensions().get(0);
        //Assert.assertTrue(bom.getComponents().get(0).getExtensions().get(0) instanceof Vulnerability1_0);
    }

    @Test
    public void testValid12BomWithVulnerability1_1() throws Exception {
        Bom bom=new Bom();
        Component component=new Component();

        Vulnerability1_0  vuln = new Vulnerability1_0();
        vuln.setRef("ref");
        vuln.setId("id");
        vuln.setDescription("desc");

        List<ExtensibleExtension> vulns =new ArrayList<>();
        vulns.add(vuln);

        component.addExtension("vulnerability", vulns);
        bom.addComponent(component);


        final XmlParser parser = new XmlParser();
        parser.parse(bom);
        System.out.println("HOLA");
    }

    @Test
    public void testValid12Bom() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.2.xml").getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_12);
        Assert.assertTrue(valid);
    }

    @Test
    public void testParsedObjects10Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.0.xml"));
        final XmlParser parser = new XmlParser();
        final Bom bom = parser.parse(bomBytes);
        Assert.assertEquals("1.0", bom.getSpecVersion());
        Assert.assertEquals(1, bom.getComponents().size());
        Assert.assertEquals(1, bom.getVersion());
        final List<Component> components = bom.getComponents();
        Assert.assertEquals(1, components.size());
        Component c1 = components.get(0);
        Assert.assertEquals("org.example", c1.getGroup());
        Assert.assertEquals("1.0.0", c1.getVersion());
        Assert.assertEquals(Component.Type.APPLICATION, c1.getType());
        Assert.assertEquals("2342c2eaf1feb9a80195dbaddf2ebaa3", c1.getHashes().get(0).getValue());
        Assert.assertEquals("68b78babe00a053f9e35ec6a2d9080f5b90122b0", c1.getHashes().get(1).getValue());
        Assert.assertEquals("708f1f53b41f11f02d12a11b1a38d2905d47b099afc71a0f1124ef8582ec7313", c1.getHashes().get(2).getValue());
        Assert.assertEquals("387b7ae16b9cae45f830671541539bf544202faae5aac544a93b7b0a04f5f846fa2f4e81ef3f1677e13aed7496408a441f5657ab6d54423e56bf6f38da124aef", c1.getHashes().get(3).getValue());
        Assert.assertEquals("cpe:/a:example:myapplication:1.0.0", c1.getCpe());
        Assert.assertEquals("pkg:maven/com.example/myapplication@1.0.0?packaging=war", c1.getPurl());
        Assert.assertEquals("An example application", c1.getDescription());
        Assert.assertEquals("Copyright Example Inc. All rights reserved.", c1.getCopyright());
        Assert.assertEquals("Apache-2.0", c1.getLicenseChoice().getLicenses().get(0).getId());
        Assert.assertEquals(2, c1.getComponents().size());
    }

    @Test
    public void testParsedObjects11Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.1.xml"));
        final XmlParser parser = new XmlParser();
        final Bom bom = parser.parse(bomBytes);
        Assert.assertEquals("1.1", bom.getSpecVersion());
        Assert.assertEquals(3, bom.getComponents().size());
        Assert.assertEquals(1, bom.getVersion());
        final List<Component> components = bom.getComponents();
        Assert.assertEquals(3, components.size());
        Component c1 = components.get(0);
        Component c2 = components.get(1);
        Assert.assertEquals("Acme Inc", c1.getPublisher());
        Assert.assertEquals("com.acme", c1.getGroup());
        Assert.assertEquals("tomcat-catalina", c1.getName());
        Assert.assertEquals("9.0.14", c1.getVersion());
        Assert.assertEquals("Modified version of Apache Catalina", c1.getDescription());
        Assert.assertEquals(Component.Type.APPLICATION, c1.getType());
        Assert.assertEquals("required", c1.getScope().getScopeName());
        Assert.assertEquals("3942447fac867ae5cdb3229b658f4d48", c1.getHashes().get(0).getValue());
        Assert.assertEquals("e6b1000b94e835ffd37f4c6dcbdad43f4b48a02a", c1.getHashes().get(1).getValue());
        Assert.assertEquals("f498a8ff2dd007e29c2074f5e4b01a9a01775c3ff3aeaf6906ea503bc5791b7b", c1.getHashes().get(2).getValue());
        Assert.assertEquals("e8f33e424f3f4ed6db76a482fde1a5298970e442c531729119e37991884bdffab4f9426b7ee11fccd074eeda0634d71697d6f88a460dce0ac8d627a29f7d1282", c1.getHashes().get(3).getValue());
        Assert.assertEquals("pkg:maven/com.acme/tomcat-catalina@9.0.14?packaging=jar", c1.getPurl());
        Assert.assertEquals("Apache-2.0", c1.getLicenseChoice().getLicenses().get(0).getId());
        Assert.assertTrue(c1.getLicenseChoice().getLicenses().get(0).getAttachmentText().getText().startsWith("CiAgICA"));
        Assert.assertEquals("text/plain", c1.getLicenseChoice().getLicenses().get(0).getAttachmentText().getContentType());
        Assert.assertEquals("base64", c1.getLicenseChoice().getLicenses().get(0).getAttachmentText().getEncoding());
        Assert.assertNotNull(c1.getPedigree());
        Assert.assertEquals(1, c1.getPedigree().getAncestors().size());
        Assert.assertNull(c1.getPedigree().getDescendants());
        Assert.assertNull(c1.getPedigree().getVariants());
        Assert.assertEquals(1, c1.getPedigree().getCommits().size());
        Assert.assertEquals("7638417db6d59f3c431d3e1f261cc637155684cd", c1.getPedigree().getCommits().get(0).getUid());
        Assert.assertEquals("https://location/to/7638417db6d59f3c431d3e1f261cc637155684cd", c1.getPedigree().getCommits().get(0).getUrl());
        Assert.assertEquals("John Doe", c1.getPedigree().getCommits().get(0).getAuthor().getName());
        Assert.assertEquals("john.doe@example.com", c1.getPedigree().getCommits().get(0).getAuthor().getEmail());
        Assert.assertNotNull(c1.getPedigree().getCommits().get(0).getAuthor().getTimestamp());
        Assert.assertEquals("Jane Doe", c1.getPedigree().getCommits().get(0).getCommitter().getName());
        Assert.assertEquals("jane.doe@example.com", c1.getPedigree().getCommits().get(0).getCommitter().getEmail());
        Assert.assertNotNull(c1.getPedigree().getCommits().get(0).getCommitter().getTimestamp());
        Assert.assertEquals("Initial commit", c1.getPedigree().getCommits().get(0).getMessage());
        Assert.assertEquals("Commentary here", c1.getPedigree().getNotes());
        Assert.assertEquals("EPL-2.0 OR GPL-2.0-with-classpath-exception", c2.getLicenseChoice().getExpression());
    }

    @Test
    public void testParsedObjects11BomWithDependencyGraph10() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.1-dependency-graph-1.0.xml"));
        final XmlParser parser = new XmlParser();
        final Bom bom = parser.parse(bomBytes);
        Assert.assertEquals("1.1", bom.getSpecVersion());
        Assert.assertEquals(3, bom.getComponents().size());
        Assert.assertEquals(1, bom.getVersion());
        final List<Component> components = bom.getComponents();
        Assert.assertEquals(3, components.size());
        Component c1 = components.get(0);
        //Component c2 = components.get(1);
        Assert.assertEquals("org.example.acme", c1.getGroup());
        Assert.assertEquals("web-framework", c1.getName());
        Assert.assertEquals("1.0.0", c1.getVersion());
        Assert.assertEquals(Component.Type.FRAMEWORK, c1.getType());
        Assert.assertEquals("pkg:maven/org.example.acme/web-framework@1.0.0", c1.getPurl());
        Assert.assertEquals(3, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        Dependency d2 = bom.getDependencies().get(1);
        Dependency d3 = bom.getDependencies().get(2);
        Assert.assertNotNull(d1);
        Assert.assertEquals("pkg:maven/org.example.acme/web-framework@1.0.0", d1.getRef());
        Assert.assertEquals(2, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        Assert.assertEquals("pkg:maven/org.example.acme/common-util@3.0.0", d11.getRef());
        Assert.assertNull(d11.getDependencies());
        Assert.assertEquals(1, d2.getDependencies().size());
        Assert.assertNull(d3.getDependencies());
    }

    @Test
    public void testParsedObjects12Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.2.xml"));
        final XmlParser parser = new XmlParser();
        final Bom bom = parser.parse(bomBytes);
        Assert.assertEquals("1.2", bom.getSpecVersion());
        Assert.assertEquals(3, bom.getComponents().size());
        Assert.assertEquals(1, bom.getVersion());
        Assert.assertNotNull(bom.getMetadata());
        Assert.assertNotNull(bom.getMetadata().getTimestamp());
        Assert.assertEquals(1, bom.getMetadata().getTools().size());
        Assert.assertEquals("Awesome Vendor", bom.getMetadata().getTools().get(0).getVendor());
        Assert.assertEquals("Awesome Tool", bom.getMetadata().getTools().get(0).getName());
        Assert.assertEquals("9.1.2", bom.getMetadata().getTools().get(0).getVersion());
        Assert.assertEquals(2, bom.getMetadata().getTools().get(0).getHashes().size());
        Assert.assertEquals("SHA-1", bom.getMetadata().getTools().get(0).getHashes().get(0).getAlgorithm());
        Assert.assertEquals("25ed8e31b995bb927966616df2a42b979a2717f0", bom.getMetadata().getTools().get(0).getHashes().get(0).getValue());
        Assert.assertEquals(1, bom.getMetadata().getAuthors().size());
        Assert.assertEquals("Samantha Wright", bom.getMetadata().getAuthors().get(0).getName());
        Assert.assertEquals("samantha.wright@example.com", bom.getMetadata().getAuthors().get(0).getEmail());
        Assert.assertEquals("800-555-1212", bom.getMetadata().getAuthors().get(0).getPhone());
        Assert.assertEquals("Acme Application", bom.getMetadata().getComponent().getName());
        Assert.assertEquals("9.1.1", bom.getMetadata().getComponent().getVersion());
        Assert.assertEquals(Component.Type.APPLICATION, bom.getMetadata().getComponent().getType());
        Assert.assertNotNull(bom.getMetadata().getComponent().getSwid());
        Assert.assertEquals("swidgen-242eb18a-503e-ca37-393b-cf156ef09691_9.1.1", bom.getMetadata().getComponent().getSwid().getTagId());
        Assert.assertEquals("Acme Application", bom.getMetadata().getComponent().getSwid().getName());
        Assert.assertEquals("9.1.1", bom.getMetadata().getComponent().getSwid().getVersion());
        Assert.assertEquals(0, bom.getMetadata().getComponent().getSwid().getTagVersion());
        Assert.assertEquals(false, bom.getMetadata().getComponent().getSwid().isPatch());
        Assert.assertEquals("Acme, Inc.", bom.getMetadata().getManufacture().getName());
        Assert.assertEquals("https://example.com", bom.getMetadata().getManufacture().getUrl());
        Assert.assertEquals(1, bom.getMetadata().getManufacture().getContacts().size());
        Assert.assertEquals("Acme Professional Services", bom.getMetadata().getManufacture().getContacts().get(0).getName());
        Assert.assertEquals("professional.services@example.com", bom.getMetadata().getManufacture().getContacts().get(0).getEmail());
        Assert.assertEquals("Acme, Inc.", bom.getMetadata().getSupplier().getName());
        Assert.assertEquals("https://example.com", bom.getMetadata().getSupplier().getUrl());
        Assert.assertEquals(1, bom.getMetadata().getSupplier().getContacts().size());
        Assert.assertEquals("Acme Distribution", bom.getMetadata().getSupplier().getContacts().get(0).getName());
        Assert.assertEquals("distribution@example.com", bom.getMetadata().getSupplier().getContacts().get(0).getEmail());
        final List<Component> components = bom.getComponents();
        Assert.assertEquals(3, components.size());
        Component c1 = components.get(0);
        Assert.assertEquals("com.acme", c1.getGroup());
        Assert.assertEquals("tomcat-catalina", c1.getName());
        Assert.assertEquals("9.0.14", c1.getVersion());
        Assert.assertEquals(Component.Type.APPLICATION, c1.getType());
        Assert.assertEquals("pkg:maven/com.acme/tomcat-catalina@9.0.14?packaging=jar", c1.getPurl());
        Assert.assertEquals(1, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        Assert.assertNotNull(d1);
        Assert.assertEquals("acme-app", d1.getRef());
        Assert.assertEquals(2, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        Assert.assertEquals("pkg:maven/org.acme/web-framework@1.0.0", d11.getRef());
        Assert.assertNull(d11.getDependencies());
    }
}
