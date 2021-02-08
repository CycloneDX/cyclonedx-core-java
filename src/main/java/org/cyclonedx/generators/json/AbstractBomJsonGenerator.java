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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.model.Bom;
import org.cyclonedx.util.CollectionTypeSerializer;
import org.cyclonedx.util.LicenseChoiceSerializer;
import org.json.JSONObject;

abstract class AbstractBomJsonGenerator extends CycloneDxSchema implements BomJsonGenerator {

    private final ObjectMapper mapper;

    AbstractBomJsonGenerator() {
        this.mapper = new ObjectMapper();

        setupObjectMapper(this.mapper);
    }

    private void setupObjectMapper(final ObjectMapper mapper) {
        SimpleModule licenseModule = new SimpleModule();
        SimpleModule depModule = new SimpleModule();

        licenseModule.addSerializer(new LicenseChoiceSerializer());

        mapper.registerModule(licenseModule);

        depModule.setSerializers(new CollectionTypeSerializer(false));
        mapper.registerModule(depModule);
    }

    JSONObject doc = OrderedJSONObjectFactory.create();

    String toJson(final Bom bom, final boolean prettyPrint) throws GeneratorException {
        try {
            if (prettyPrint) {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bom);
            }
            return mapper.writeValueAsString(bom);
        }
        catch (JsonProcessingException e) {
            throw new GeneratorException(e);
        }
    }
}
