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
package org.cyclonedx.generators;

import org.cyclonedx.Format;
import org.cyclonedx.Version;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.cyclonedx.model.Bom;
import org.cyclonedx.generators.json.BomJsonGenerator;

public class BomGeneratorFactory {
    public static AbstractBomGenerator create(Version version, Bom bom, Format format) {
        AbstractBomGenerator generator;

        switch (format) {
            case XML: generator = createXml(version, bom); break;
            case JSON: generator = createJson(version, bom); break;
            default: throw new IllegalArgumentException("Unsupported format " + format);
        }

        return generator;
    }

    public static BomXmlGenerator createXml(Version version, Bom bom) {
        return new BomXmlGenerator(bom, version);
    }

    public static BomJsonGenerator createJson(Version version, Bom bom) {
        return new BomJsonGenerator(bom, version);
    }
}
