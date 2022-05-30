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

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BomLinkTest {

    @Test
    public void testValidBomLinks() {
        assertTrue(BomLink.isBomLink("urn:cdx:dbd9e325-a02e-4bd1-9a21-5123edd1b27c/1"));
        assertTrue(BomLink.isBomLink("urn:cdx:dbd9e325-a02e-4bd1-9a21-5123edd1b27c/999999999"));
        assertTrue(BomLink.isBomLink("urn:cdx:dbd9e325-a02e-4bd1-9a21-5123edd1b27c/1#pkg:maven/org.apache.logging.log4j/log4j-core@2.17.2"));
        assertTrue(BomLink.isBomLink("urn:cdx:dbd9e325-a02e-4bd1-9a21-5123edd1b27c/1#foo"));
    }

    @Test
    public void testInvalidBomLinks() {
        assertFalse(BomLink.isBomLink("urn:cdx:dbd9e325-a02e-4bd1-9a21-5123edd1b27c/"));
        assertFalse(BomLink.isBomLink("urn:cdx:dbd9e325-a02e-4bd1-9a21-5123edd1b27c"));
        assertFalse(BomLink.isBomLink("urn:cdx:foo/1"));
        assertFalse(BomLink.isBomLink("urn:foo:dbd9e325-a02e-4bd1-9a21-5123edd1b27/1"));
        assertFalse(BomLink.isBomLink("foo:cdx:dbd9e325-a02e-4bd1-9a21-5123edd1b27/1"));
    }

    @Test
    public void testParserWithRef() throws Exception {
        BomLink bl = new BomLink("urn:cdx:dbd9e325-a02e-4bd1-9a21-5123edd1b27c/1#pkg:maven/org.apache.logging.log4j/log4j-core@2.17.2");
        assertEquals(UUID.fromString("dbd9e325-a02e-4bd1-9a21-5123edd1b27c"), bl.getSerialNumber());
        assertEquals(1, bl.getVersion());
        assertEquals("pkg:maven/org.apache.logging.log4j/log4j-core@2.17.2", bl.getBomRef());
    }

    @Test
    public void testParserWithoutRef() throws Exception {
        BomLink bl = new BomLink("urn:cdx:dbd9e325-a02e-4bd1-9a21-5123edd1b27c/1");
        assertEquals(UUID.fromString("dbd9e325-a02e-4bd1-9a21-5123edd1b27c"), bl.getSerialNumber());
        assertEquals(1, bl.getVersion());
        assertNull(bl.getBomRef());
    }

}
