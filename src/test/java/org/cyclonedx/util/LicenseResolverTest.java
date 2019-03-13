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
package org.cyclonedx.util;

import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.junit.Assert;
import org.junit.Test;

public class LicenseResolverTest {

    @Test
    public void parseLicenseByUrlTest() throws Exception {
        License l1 = LicenseResolver.parseLicenseByUrl("http://www.opensource.org/licenses/GPL-3.0");
        License l2 = LicenseResolver.parseLicenseByUrl("http://www.gnu.org/licenses/gpl-3.0-standalone.html");
        License l3 = LicenseResolver.parseLicenseByUrl("http://www.opensource.org/licenses/MIT");
        License l4 = LicenseResolver.parseLicenseByUrl("http://www.opensource.org/licenses/Apache-2.0");
        License l5 = LicenseResolver.parseLicenseByUrl("http://www.apache.org/licenses/LICENSE-2.0");
        Assert.assertEquals("GPL-3.0-only", l1.getId());
        Assert.assertEquals("GPL-3.0-only", l2.getId());
        Assert.assertEquals("MIT", l3.getId());
        Assert.assertEquals("Apache-2.0", l4.getId());
        Assert.assertEquals("Apache-2.0", l5.getId());
    }

    @Test
    public void resolveTestSingleLicense() {
        LicenseChoice c1 = LicenseResolver.resolve("GPL-3.0+");
        Assert.assertEquals(1, c1.getLicenses().size());
        Assert.assertEquals("GPL-3.0", c1.getLicenses().get(0).getId());
        Assert.assertEquals("GNU General Public License v3.0 only", c1.getLicenses().get(0).getName());
        Assert.assertEquals("http://www.gnu.org/licenses/gpl-3.0-standalone.html", c1.getLicenses().get(0).getUrl());
        Assert.assertTrue(c1.getLicenses().get(0).getLicenseText().getText().endsWith("="));
        Assert.assertEquals("plain/text", c1.getLicenses().get(0).getLicenseText().getContentType());
        Assert.assertEquals("base64", c1.getLicenses().get(0).getLicenseText().getEncoding());
    }

    @Test
    public void resolveTestExpressionOrConjunction() {
        LicenseChoice c1 = LicenseResolver.resolve("(LGPL-2.1 OR BSD-3-Clause AND MIT)");
        Assert.assertNull(c1.getLicenses());
        Assert.assertEquals("(LGPL-2.1 OR BSD-3-Clause AND MIT)", c1.getExpression());
    }

    @Test
    public void resolveTestExpressionAndConjunction() {
        LicenseChoice c1 = LicenseResolver.resolve("(MIT AND (LGPL-2.1+ AND BSD-3-Clause))");
        Assert.assertNull(c1.getLicenses());
        Assert.assertEquals("(MIT AND (LGPL-2.1+ AND BSD-3-Clause))", c1.getExpression());
    }

    @Test
    public void fuzzyMatchingTest() {
        LicenseChoice c1 = LicenseResolver.resolve("The Apache Software License, Version 2.0");
        Assert.assertEquals("Apache-2.0", c1.getLicenses().get(0).getId());
        //TODO build this up
    }
}
