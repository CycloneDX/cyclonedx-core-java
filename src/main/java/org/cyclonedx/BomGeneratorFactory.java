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

import org.cyclonedx.generators.json.BomJsonGenerator12;
import org.cyclonedx.model.Bom;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator10;
import org.cyclonedx.generators.xml.BomXmlGenerator11;
import org.cyclonedx.generators.xml.BomXmlGenerator12;

public class BomGeneratorFactory {

    private BomGeneratorFactory() {
    }

    @Deprecated
    public static BomXmlGenerator create(CycloneDxSchema.Version version, Bom bom) {
        return createXml(version, bom);
    }

    public static BomXmlGenerator createXml(CycloneDxSchema.Version version, Bom bom) {
        if (CycloneDxSchema.Version.VERSION_10 == version) {
            return new BomXmlGenerator10(bom);
        } else if (CycloneDxSchema.Version.VERSION_11 == version) {
            return new BomXmlGenerator11(bom);
        } else {
            return new BomXmlGenerator12(bom);
        }
    }

    public static BomJsonGenerator createJson(CycloneDxSchema.Version version, Bom bom) {
        return new BomJsonGenerator12(bom);
    }
}
