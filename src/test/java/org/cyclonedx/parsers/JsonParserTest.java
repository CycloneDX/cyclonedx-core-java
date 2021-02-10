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
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.util.List;

public class JsonParserTest {

    @Test
    public void testValid12Bom() throws Exception {
        final File file = new File(this.getClass().getResource("/bom-1.2.json").getFile());
        final JsonParser parser = new JsonParser();
        Bom bom = parser.parse(file);
        Assert.assertTrue(parser.isValid(file, CycloneDxSchema.Version.VERSION_12, true));
        System.out.println(bom.getSerialNumber());
    }

    @Test
    public void testParsedObjects12Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.2.json"));
        final JsonParser parser = new JsonParser();
        final Bom bom = parser.parse(bomBytes);
        Assert.assertEquals("1.2", bom.getSpecVersion());
        Assert.assertEquals(2, bom.getComponents().size());
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
        Assert.assertEquals("https://example.com", bom.getMetadata().getManufacture().getUrls().get(0));
        Assert.assertEquals(1, bom.getMetadata().getManufacture().getContacts().size());
        Assert.assertEquals("Acme Professional Services", bom.getMetadata().getManufacture().getContacts().get(0).getName());
        Assert.assertEquals("professional.services@example.com", bom.getMetadata().getManufacture().getContacts().get(0).getEmail());
        Assert.assertEquals("Acme, Inc.", bom.getMetadata().getSupplier().getName());
        Assert.assertEquals("https://example.com", bom.getMetadata().getSupplier().getUrls().get(0));
        Assert.assertEquals(1, bom.getMetadata().getSupplier().getContacts().size());
        Assert.assertEquals("Acme Distribution", bom.getMetadata().getSupplier().getContacts().get(0).getName());
        Assert.assertEquals("distribution@example.com", bom.getMetadata().getSupplier().getContacts().get(0).getEmail());
        final List<Component> components = bom.getComponents();
        Assert.assertEquals(2, components.size());
        Component c1 = components.get(0);
        Assert.assertEquals("com.acme", c1.getGroup());
        Assert.assertEquals("tomcat-catalina", c1.getName());
        Assert.assertEquals("9.0.14", c1.getVersion());
        Assert.assertEquals(Component.Type.LIBRARY, c1.getType());
        Assert.assertEquals("pkg:npm/acme/component@1.0.0", c1.getPurl());
        Assert.assertEquals(1, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        Assert.assertNotNull(d1);
        Assert.assertEquals("pkg:npm/acme/component@1.0.0", d1.getRef());
        Assert.assertEquals(1, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        Assert.assertEquals("pkg:npm/acme/common@1.0.0", d11.getRef());
        Assert.assertNull(d11.getDependencies());
    }
}
