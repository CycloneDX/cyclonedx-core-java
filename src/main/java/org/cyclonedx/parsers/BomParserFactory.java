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
package org.cyclonedx.parsers;

import org.apache.commons.io.IOUtils;
import org.cyclonedx.exception.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

public class BomParserFactory {

    private BomParserFactory() {}

    public static Parser createParser(final File file) throws ParseException {
        try (final InputStream fis = Files.newInputStream(file.toPath())) {
            final byte[] prefix = new byte[4]; // potential 3-byte UTF-8 byte-order mark + 1 content byte
            final int actualPrefixLength = IOUtils.read(fis, prefix);
            return createParser(Arrays.copyOf(prefix, actualPrefixLength));
        } catch (IOException e) {
            throw new ParseException("An error occurred creating parser from file", e);
        }
    }

    public static Parser createParser(final byte[] bytes) throws ParseException {
        final int offset = hasUtf8ByteOrderMark(bytes) ? 3 : 0;
        if (bytes.length - offset < 1) {
            throw new ParseException("Cannot create parser from empty byte array.");
        }

        if (bytes[offset] == (byte) '{') {
            return new JsonParser();
        } else if (bytes[offset] == (byte) '<') {
            return new XmlParser();
        } else {
            throw new ParseException("The specified BOM is not in a supported format. Supported formats are XML and JSON");
        }
    }

    public static boolean looksLikeCycloneDX(final byte[] bytes) {
        final int offset = hasUtf8ByteOrderMark(bytes) ? 3 : 0;
        final String bomString = new String(bytes, offset, bytes.length - offset, StandardCharsets.UTF_8);
        if (bomString.startsWith("<?xml") && bomString.contains("<bom") &&
            bomString.contains("http://cyclonedx.org/schema/bom")) {
            return true;
        }
        else {
            return bomString.startsWith("{") && bomString.contains("bomFormat") && bomString.contains("CycloneDX");
        }
    }

    private static boolean hasUtf8ByteOrderMark(byte[] bytes) {
        return bytes.length >= 3
                && bytes[0] == (byte) 0xEF
                && bytes[1] == (byte) 0xBB
                && bytes[2] == (byte) 0xBF;
    }

}
