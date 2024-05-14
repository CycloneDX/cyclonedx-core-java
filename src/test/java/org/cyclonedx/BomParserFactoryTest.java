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
import org.cyclonedx.parsers.BomParserFactory;
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.Parser;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BomParserFactoryTest {

    @Test
    public void testXMLFactory() throws Exception {
        Parser parser = BomParserFactory.createParser(
            new File(Objects.requireNonNull(BomParserFactory.class.getResource("/bom-1.2.xml")).getFile()));
        assertInstanceOf(XmlParser.class, parser);
    }

    @Test
    public void testJSONFactory() throws Exception {
        Parser parser = BomParserFactory.createParser(new File(
            Objects.requireNonNull(BomParserFactory.class.getResource("/bom-1.2.json")).getFile()));
        assertInstanceOf(JsonParser.class, parser);
    }

    @Test()
    public void testFactoryThrowsParseExceptionWithEmptyData() {
        byte[] emptyData = new byte[]{};
        assertThrows(ParseException.class, () ->
            BomParserFactory.createParser(emptyData)
        );
    }
}
