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
        System.out.println(bom.getSerialNumber());
    }

    //@Test //TODO
    public void testParsedObjects12Bom() throws Exception {
        final byte[] bomBytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/bom-1.1-dependency-graph-1.0.xml"));
        final XmlParser parser = new XmlParser();
        final Bom bom = parser.parse(bomBytes);
        Assert.assertEquals("1.1", bom.getSpecVersion());
        Assert.assertEquals(3, bom.getComponents().size());
        Assert.assertEquals(1, bom.getVersion());
        final List<Component> components = bom.getComponents();
        Assert.assertEquals(3, components.size());
        Component c1 = components.get(0);
        Component c2 = components.get(1);
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
}
