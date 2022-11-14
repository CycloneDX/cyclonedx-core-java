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
package org.cyclonedx.util;

import org.cyclonedx.model.LicenseChoice;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LicenseResolverTest {

    @Test
    public void parseLicenseByUrlTest() {
        LicenseChoice l1 = LicenseResolver.resolve("https://www.opensource.org/licenses/GPL-3.0");
        LicenseChoice l2 = LicenseResolver.resolve("https://www.gnu.org/licenses/gpl-3.0-standalone.html");
        LicenseChoice l3 = LicenseResolver.resolve("https://www.opensource.org/licenses/MIT");
        LicenseChoice l4 = LicenseResolver.resolve("https://www.opensource.org/licenses/Apache-2.0");
        LicenseChoice l5 = LicenseResolver.resolve("https://www.apache.org/licenses/LICENSE-2.0");
        assertEquals("GPL-3.0-only", l1.getLicenses().get(0).getId());
        assertEquals("GPL-3.0-only", l2.getLicenses().get(0).getId());
        assertEquals("MIT", l3.getLicenses().get(0).getId());
        assertEquals("Apache-2.0", l4.getLicenses().get(0).getId());
        assertEquals("Apache-2.0", l5.getLicenses().get(0).getId());
    }

    @Test
    public void resolveTestSingleLicense() {
        LicenseChoice c1 = LicenseResolver.resolve("GPL-3.0-only");
        assertEquals(1, c1.getLicenses().size());
        assertEquals("GPL-3.0-only", c1.getLicenses().get(0).getId());
        assertEquals("https://www.gnu.org/licenses/gpl-3.0-standalone.html", c1.getLicenses().get(0).getUrl());
        assertNotNull(c1.getLicenses().get(0).getAttachmentText().getText());
        assertEquals("plain/text", c1.getLicenses().get(0).getAttachmentText().getContentType());
        assertEquals("base64", c1.getLicenses().get(0).getAttachmentText().getEncoding());

        LicenseResolver.LicenseTextSettings textSettings = new LicenseResolver.LicenseTextSettings( true, LicenseResolver.LicenseEncoding.NONE);
        LicenseChoice c2 = LicenseResolver.resolve("GPL-3.0-only", textSettings);
        assertEquals(1, c2.getLicenses().size());
        assertEquals("GPL-3.0-only", c2.getLicenses().get(0).getId());
        assertEquals("https://www.gnu.org/licenses/gpl-3.0-standalone.html", c2.getLicenses().get(0).getUrl());
        assertNotNull(c2.getLicenses().get(0).getAttachmentText().getText());
        assertEquals("plain/text", c2.getLicenses().get(0).getAttachmentText().getContentType());
        assertNull(c2.getLicenses().get(0).getAttachmentText().getEncoding());

        textSettings = new LicenseResolver.LicenseTextSettings( true, LicenseResolver.LicenseEncoding.BASE64);
        LicenseChoice c3 = LicenseResolver.resolve("GPL-3.0-only", textSettings);
        assertEquals(1, c3.getLicenses().size());
        assertEquals("GPL-3.0-only", c3.getLicenses().get(0).getId());
        assertEquals("https://www.gnu.org/licenses/gpl-3.0-standalone.html", c3.getLicenses().get(0).getUrl());
        assertNotNull(c3.getLicenses().get(0).getAttachmentText().getText());
        assertEquals("plain/text", c3.getLicenses().get(0).getAttachmentText().getContentType());
        assertEquals("base64", c3.getLicenses().get(0).getAttachmentText().getEncoding());
    }

    @Test
    public void fuzzyMatchingTest() {
        LicenseChoice c1 = LicenseResolver.resolve("The Apache Software License, Version 2.0");
        assertEquals("Apache-2.0", c1.getLicenses().get(0).getId());
        LicenseChoice c2 = LicenseResolver.resolve("Apache License (v2.0)");
        assertEquals("Apache-2.0", c2.getLicenses().get(0).getId());
        LicenseChoice c3 = LicenseResolver.resolve("Apache Public License 2.0");
        assertEquals("Apache-2.0", c3.getLicenses().get(0).getId());
        LicenseChoice c4 = LicenseResolver.resolve("Modified BSD License");
        assertEquals("BSD-3-Clause", c4.getLicenses().get(0).getId());
    }
}
