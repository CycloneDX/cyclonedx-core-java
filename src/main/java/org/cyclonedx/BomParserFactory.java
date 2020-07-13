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
package org.cyclonedx;

import org.apache.commons.io.IOUtils;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.Parser;
import org.cyclonedx.parsers.XmlParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BomParserFactory {

    private BomParserFactory() {}

    public static Parser createParser(final File file) throws ParseException {
        try {
            final byte[] bytes = IOUtils.toByteArray(new FileInputStream(file), 1);
            if (bytes[0] == 123) {
                return new JsonParser();
            } else if (bytes[0] == 60) {
                return new XmlParser();
            } else {
                throw new ParseException("The specified BOM is not in a supported format. Supported formats are XML and JSON");
            }
        } catch (IOException e) {
            throw new ParseException("An error occurred creating parser from file", e);
        }
    }
}
