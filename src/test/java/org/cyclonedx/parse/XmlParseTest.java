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

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class XmlParseTest extends BaseParseTest {

    @TestFactory
    public Collection<DynamicTest> dynamicTestsWithCollection() throws Exception {
        final List<File> files = getAllResources();
        final List<DynamicTest> dynamicTests = new ArrayList<>();
        for (final File file: files) {
            if (file.getName().endsWith(".xml")) {
                if (file.getName().startsWith("valid")) {
                    dynamicTests.add(DynamicTest.dynamicTest(file.getName(), () -> assertNotNull(parseBom(file))));
                } else if (file.getName().startsWith("invalid")) {

                }
            }
        }
        return dynamicTests;
    }

}
