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

import org.cyclonedx.exception.ParseException;
import org.cyclonedx.parsers.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonSchemaClassLoaderResolutionTest {

    @Test // https://github.com/CycloneDX/cyclonedx-core-java/issues/849
    void shouldResolveSchemaResourcesEvenWhenContextClassLoaderCannot() throws Exception {
        final byte[] bom;
        try (final InputStream in = getClass().getResourceAsStream("/1.6/valid-license-id-1.6.json")) {
            assertThat(in).isNotNull();
            bom = in.readAllBytes();
        }

        final ClassLoader original = Thread.currentThread().getContextClassLoader();

        try (final var hostile = new URLClassLoader(new URL[0], null)) {
            Thread.currentThread().setContextClassLoader(hostile);
            List<ParseException> errors = new JsonParser().validate(bom, Version.VERSION_16);
            assertThat(errors).isEmpty();
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

}
