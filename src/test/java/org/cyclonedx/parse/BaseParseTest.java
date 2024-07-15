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
package org.cyclonedx.parse;

import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.Format;
import org.cyclonedx.generators.BomGeneratorFactory;
import org.cyclonedx.parsers.BomParserFactory;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.Version;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.cyclonedx.model.Bom;
import org.cyclonedx.parsers.Parser;
import org.junit.jupiter.api.Assertions;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class BaseParseTest {

    List<File> getAllResources() {
        final List<File> files = new ArrayList<>();
        for (Version version: CycloneDxSchema.ALL_VERSIONS) {
            files.addAll(getResources(version.getVersionString() + "/"));
        }
        return files;
    }

    List<File> getResources(final String resourceDirectory) {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final URL url = loader.getResource(resourceDirectory);
        final String path = Objects.requireNonNull(url).getPath();
        return Arrays.asList(Objects.requireNonNull(new File(path).listFiles()));
    }

    Bom parseBom(File file) throws ParseException {
        final Parser parser = BomParserFactory.createParser(file);
        return parser.parse(file);
    }

    void generateBomXml(final String testName, final Bom bom) throws ParserConfigurationException {
        List<Version> xmlVersions = Arrays.stream(Version.values())
                .filter(v -> v.getFormats().contains(Format.XML))
                .collect(Collectors.toList());

        for (Version version : xmlVersions) {
            System.out.println("Generating CycloneDX " + version.getVersionString() + " XML for " + testName);
            BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
            Document doc = generator.generate();
            Assertions.assertNotNull(doc);
        }
    }

    void generateBomJson(final String testName, final Bom bom) throws GeneratorException {
        List<Version> jsonVersions = Arrays.stream(Version.values())
                .filter(v -> v.getFormats().contains(Format.JSON))
                .collect(Collectors.toList());

        for (Version version : jsonVersions) {
            System.out.println("Generating CycloneDX " + version.getVersionString() + " JSON for " + testName);
            BomJsonGenerator generator = BomGeneratorFactory.createJson(version, bom);
            Assertions.assertNotNull(generator.toJsonString());
        }
    }

}
