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

import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.util.CollectionTypeSerializer;
import org.cyclonedx.util.LicenseChoiceSerializer;
import org.json.JSONObject;

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
        Bom modifiedBom = null;
        try {
            modifiedBom = injectBomFormat(bom);
        }
        catch (GeneratorException e) {
        }
        if (modifiedBom != null) {
            this.bom = modifiedBom;
        } else {
            this.bom = bom;
        }
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
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule licenseModule = new SimpleModule();
        SimpleModule depModule = new SimpleModule();

        licenseModule.addSerializer(new LicenseChoiceSerializer());

        mapper.registerModule(licenseModule);

        try {
            if (bom.getDependencies() != null && !bom.getDependencies().isEmpty()) {
                depModule.setSerializers(new CollectionTypeSerializer(false));
                mapper.registerModule(depModule);
            }
            final String json = mapper.writeValueAsString(this.bom);

            doc = new JSONObject(json);

            return doc;
        }
        catch (JsonProcessingException e) {
            return null;
        }
    }

    private Bom injectBomFormat(Bom bom) throws GeneratorException {
        try {
            Field field;
            field = Bom.class.getDeclaredField("bomFormat");
            field.setAccessible(true);
            field.set(bom, "CycloneDX");
            field = Bom.class.getDeclaredField("specVersion");
            field.setAccessible(true);
            field.set(bom, getSchemaVersion().getVersionString());
            return bom;
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            throw new GeneratorException(e);
        }
    }
}
