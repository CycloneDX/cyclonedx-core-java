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

import org.cyclonedx.generators.json.BomJsonGenerator12;
import org.cyclonedx.generators.json.BomJsonGenerator13;
import org.cyclonedx.generators.json.BomJsonGenerator14;
import org.cyclonedx.generators.json.BomJsonGenerator15;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator10;
import org.cyclonedx.generators.xml.BomXmlGenerator11;
import org.cyclonedx.generators.xml.BomXmlGenerator12;
import org.cyclonedx.generators.xml.BomXmlGenerator13;
import org.cyclonedx.generators.xml.BomXmlGenerator14;
import org.cyclonedx.generators.xml.BomXmlGenerator15;
import org.cyclonedx.model.Bom;
import org.cyclonedx.generators.json.BomJsonGenerator;

public class BomGeneratorFactory {

    private BomGeneratorFactory() {
    }

    @Deprecated
    public static BomXmlGenerator create(CycloneDxSchema.Version version, Bom bom) {
        return createXml(version, bom);
    }

    public static BomXmlGenerator createXml(CycloneDxSchema.Version version, Bom bom) {
        switch (version) {
            case VERSION_10:
                return new BomXmlGenerator10(bom);
            case VERSION_11:
                return new BomXmlGenerator11(bom);
            case VERSION_12:
                return new BomXmlGenerator12(bom);
            case VERSION_13:
                return new BomXmlGenerator13(bom);
            case VERSION_14:
                return new BomXmlGenerator14(bom);
            default:
                return new BomXmlGenerator15(bom);
        }
    }

    public static BomJsonGenerator createJson(final CycloneDxSchema.Version version,  Bom bom) {
        switch (version) {
            case VERSION_12:
                return new BomJsonGenerator12(bom);
            case VERSION_13:
                return new BomJsonGenerator13(bom);
            case VERSION_14:
                return new BomJsonGenerator14(bom);
            default:
                return new BomJsonGenerator15(bom);
        }
    }
}
