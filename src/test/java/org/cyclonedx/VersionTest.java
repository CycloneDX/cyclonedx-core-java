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
package org.cyclonedx;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class VersionTest {

    /**
     * Test the getVersionString method - this is to ensure the method is future-proof.
     * If a new enum value is added this test will fail unless fromVersionString is updated.
     */
    @Test
    void testFromVersionString() {
        for (Version version : Version.values()) {
            String versionString = version.getVersionString();
            Version result = Version.fromVersionString(versionString);
            assertEquals(version, result, () -> "Version should match for " + versionString);
        }
    }

    @Test
    void testFromVersionStringInvalid() {
        Version result = Version.fromVersionString("invalid-version");
        assertNull(result);
    }
    @Test
    void testFromVersionStringNull() {
        Version result = Version.fromVersionString(null);
        assertNull(result);
    }
}
