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

import org.cyclonedx.BomParserFactory;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.model.Bom;
import org.cyclonedx.parsers.Parser;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseParseTest {

    List<File> getAllResources() throws Exception {
        final List<File> files = new ArrayList<>();
        files.addAll(getResources("1.0/"));
        files.addAll(getResources("1.1/"));
        files.addAll(getResources("1.2/"));
        files.addAll(getResources("1.3/"));
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
}
