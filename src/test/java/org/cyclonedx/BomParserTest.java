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
import org.cyclonedx.model.Component;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.util.List;

public class BomParserTest {

    @Test
    public void testValid10Bom() {
        final File file = new File(this.getClass().getResource("/bom-1.0.xml").getFile());
        final BomParser parser = new BomParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_10);
        Assert.assertTrue(valid);
    }

    @Test
    public void testValid11Bom() {
        final File file = new File(this.getClass().getResource("/bom-1.1.xml").getFile());
        final BomParser parser = new BomParser();
        final boolean valid = parser.isValid(file, CycloneDxSchema.Version.VERSION_11);
        Assert.assertTrue(valid);
    }

    @Test
    public void testParsedObjects10Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.0.xml"));
        final BomParser parser = new BomParser();
        final Bom bom = parser.parse(bomBytes);
        Assert.assertEquals(1, bom.getComponents().size());
        Assert.assertEquals(1, bom.getVersion());
        final List<Component> components = bom.getComponents();
        Assert.assertEquals(1, components.size());
        Component c1 = components.get(0);
        Assert.assertEquals("org.example", c1.getGroup());
        Assert.assertEquals("1.0.0", c1.getVersion());
        Assert.assertEquals(Component.Classification.APPLICATION.getValue(), c1.getType());
        Assert.assertEquals("2342c2eaf1feb9a80195dbaddf2ebaa3", c1.getHashes().get(0).getValue());
        Assert.assertEquals("68b78babe00a053f9e35ec6a2d9080f5b90122b0", c1.getHashes().get(1).getValue());
        Assert.assertEquals("708f1f53b41f11f02d12a11b1a38d2905d47b099afc71a0f1124ef8582ec7313", c1.getHashes().get(2).getValue());
        Assert.assertEquals("387b7ae16b9cae45f830671541539bf544202faae5aac544a93b7b0a04f5f846fa2f4e81ef3f1677e13aed7496408a441f5657ab6d54423e56bf6f38da124aef", c1.getHashes().get(3).getValue());
        Assert.assertEquals("cpe:/a:example:myapplication:1.0.0", c1.getCpe());
        Assert.assertEquals("pkg:maven/com.example/myapplication@1.0.0?packaging=war", c1.getPurl());
        Assert.assertEquals("An example application", c1.getDescription());
        Assert.assertEquals("Copyright Example Inc. All rights reserved.", c1.getCopyright());
        Assert.assertEquals("Apache-2.0", c1.getLicenses().get(0).getId());
        Assert.assertEquals(2, c1.getComponents().size());
    }

    @Test
    public void testParsedObjects11Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.1.xml"));
        final BomParser parser = new BomParser();
        final Bom bom = parser.parse(bomBytes);
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
        Assert.assertEquals(Component.Classification.APPLICATION.getValue(), c1.getType());
        Assert.assertEquals("required", c1.getScope());
        Assert.assertEquals("3942447fac867ae5cdb3229b658f4d48", c1.getHashes().get(0).getValue());
        Assert.assertEquals("e6b1000b94e835ffd37f4c6dcbdad43f4b48a02a", c1.getHashes().get(1).getValue());
        Assert.assertEquals("f498a8ff2dd007e29c2074f5e4b01a9a01775c3ff3aeaf6906ea503bc5791b7b", c1.getHashes().get(2).getValue());
        Assert.assertEquals("e8f33e424f3f4ed6db76a482fde1a5298970e442c531729119e37991884bdffab4f9426b7ee11fccd074eeda0634d71697d6f88a460dce0ac8d627a29f7d1282", c1.getHashes().get(3).getValue());
        Assert.assertEquals("pkg:maven/com.acme/tomcat-catalina@9.0.14?packaging=jar", c1.getPurl());
        Assert.assertEquals("Apache-2.0", c1.getLicenses().get(0).getId());
        Assert.assertTrue(c1.getLicenses().get(0).getLicenseText().getText().startsWith("CiAgICA"));
        Assert.assertEquals("text/plain", c1.getLicenses().get(0).getLicenseText().getContentType());
        Assert.assertEquals("base64", c1.getLicenses().get(0).getLicenseText().getEncoding());
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
        //Assert.assertEquals("EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0", c2.getLicenseExpression());
    }
}
