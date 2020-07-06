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
package org.cyclonedx.generators.json;

import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Metadata;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * BomGenerator creates a CycloneDX bill-of-material document from a set of
 * {@link Component}s. Proper usage assumes {@link #generate()} is called after
 * construction and optionally followed by {@link #toJsonString()}.
 * @since 3.0.0
 */
public class BomJsonGenerator12 extends AbstractBomJsonGenerator implements BomJsonGenerator {

    private final Bom bom;

    /**
     * Constructs a new BomGenerator object.
     * @param bom the BOM to generate
     */
    public BomJsonGenerator12(final Bom bom) {
        this.bom = bom;
    }

    /**
     * Returns the version of the CycloneDX schema used by this instance
     * @return a CycloneDxSchemaVersion enum
     */
    public Version getSchemaVersion() {
        return CycloneDxSchema.Version.VERSION_12;
    }

    /**
     * Creates a CycloneDX BOM from a set of Components.
     * @return an XML Document representing a CycloneDX BoM
     * @since 3.0.0
     */
    public JSONObject generate() {
        doc.put("bomFormat", "CycloneDX");
        doc.put("specVersion", Version.VERSION_12.getVersionString());
        if (bom.getSerialNumber() != null) {
            doc.put("serialNumber", bom.getSerialNumber());
        }
        doc.put("version", bom.getVersion());
        createMetadataNode(doc, bom.getMetadata());
        createComponentsNode(doc, bom.getComponents());
        createExternalReferencesNode(doc, bom.getExternalReferences());
        return doc;
    }
}
