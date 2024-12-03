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

class FormatTest {

    @Test
    void testFromExtension() {
        for (Format format : Format.values()) {
            String extension = format.getExtension();
            Format result = Format.fromExtension(extension);
            assertEquals(format, result, () -> "Format should match for extension " + extension);
        }
    }

    @Test
    void testFromExtensionInvalid() {
        Format result = Format.fromExtension("invalid-extension");
        assertNull(result);
    }

    @Test
    void testFromExtensionNull() {
        Format result = Format.fromExtension(null);
        assertNull(result);
    }
}
