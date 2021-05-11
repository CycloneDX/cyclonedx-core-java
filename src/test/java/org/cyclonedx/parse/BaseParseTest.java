/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cyclonedx.parse;

import org.cyclonedx.BomGeneratorFactory;
import org.cyclonedx.BomParserFactory;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.cyclonedx.model.Bom;
import org.cyclonedx.parsers.Parser;
import org.junit.jupiter.api.Assertions;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseParseTest {

    static List<CycloneDxSchema.Version> VERSIONS = new ArrayList<>();
    static {
        VERSIONS.add(CycloneDxSchema.Version.VERSION_10);
        VERSIONS.add(CycloneDxSchema.Version.VERSION_11);
        VERSIONS.add(CycloneDxSchema.Version.VERSION_12);
        VERSIONS.add(CycloneDxSchema.Version.VERSION_13);
    }

    List<File> getAllResources() throws Exception {
        final List<File> files = new ArrayList<>();
        for (CycloneDxSchema.Version version: VERSIONS) {
            files.addAll(getResources(version.getVersionString() + "/"));
        }
        return files;
    }

    List<File> getResources(final String resourceDirectory) throws Exception {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final URL url = loader.getResource(resourceDirectory);
        final String path = url.getPath();
        return Arrays.asList(new File(path).listFiles());
    }

    Bom parseBom(File file) throws ParseException {
        final Parser parser = BomParserFactory.createParser(file);
        return parser.parse(file);
    }

    void generateBomXml(final String testName, final Bom bom) throws ParserConfigurationException, ParseException, GeneratorException, IOException {
        for (CycloneDxSchema.Version version : VERSIONS) {
            System.out.println("Generating CycloneDX " + version.getVersionString() + " XML for " + testName);
            BomXmlGenerator generator = BomGeneratorFactory.createXml(version, bom);
            Document doc = generator.generate();
            Assertions.assertNotNull(doc);
        }
    }

    void generateBomJson(final String testName, final Bom bom) {
        for (CycloneDxSchema.Version version : VERSIONS) {
            System.out.println("Generating CycloneDX " + version.getVersionString() + " JSON for " + testName);
            BomJsonGenerator generator = BomGeneratorFactory.createJson(version, bom);
            Assertions.assertNotNull(generator.toJsonString());
        }
    }

}
