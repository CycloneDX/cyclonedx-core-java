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
package org.cyclonedx.generators.json;

import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonValue;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.BomReference;
import org.cyclonedx.util.CollectionTypeSerializer;
import org.cyclonedx.util.ComponentWrapperSerializer;
import org.cyclonedx.util.LicenseChoiceSerializer;
import org.cyclonedx.util.TrimStringSerializer;
import org.cyclonedx.util.VersionJsonAnnotationIntrospector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public abstract class AbstractBomJsonGenerator extends CycloneDxSchema implements BomJsonGenerator {

    private final ObjectMapper mapper;

    private final DefaultPrettyPrinter prettyPrinter;

	public AbstractBomJsonGenerator() {
        this.mapper = new ObjectMapper();
        this.prettyPrinter = new DefaultPrettyPrinter();

        setupObjectMapper(this.mapper);
        setupPrettyPrinter(this.prettyPrinter);
    }

	public ObjectMapper getMapper() {
		return mapper;
	}
	
    private void setupPrettyPrinter(final DefaultPrettyPrinter prettyPrinter) {
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
    }
	
    private void setupObjectMapper(final ObjectMapper mapper) {
        mapper.setAnnotationIntrospector(
                new VersionJsonAnnotationIntrospector(
                        String.valueOf(this.getSchemaVersion().getVersion())));

        SimpleModule licenseModule = new SimpleModule();
        SimpleModule depModule = new SimpleModule();
        SimpleModule componentWrapperModule = new SimpleModule();

        SimpleModule stringModule = new SimpleModule();
        stringModule.addSerializer(new TrimStringSerializer());
        mapper.registerModule(stringModule);

        licenseModule.addSerializer(new LicenseChoiceSerializer());

        mapper.registerModule(licenseModule);

        depModule.setSerializers(new CollectionTypeSerializer(false));
        mapper.registerModule(depModule);

        componentWrapperModule.addSerializer(new ComponentWrapperSerializer(mapper));

        mapper.registerModule(componentWrapperModule);
    }

    public class MixInBomReference {
        @JsonValue
        private String ref;

        public String getRef() {
            return ref;
        }

        public void setRef(final String ref) {
            this.ref = ref;
        }
    }

    String toJson(final Bom bom, final boolean prettyPrint) throws GeneratorException {
        try {
            mapper.addMixIn(BomReference.class, MixInBomReference.class);
            if (prettyPrint) {
                return mapper.writer(prettyPrinter).writeValueAsString(bom);
            }
            return mapper.writeValueAsString(bom);
        }
        catch (JsonProcessingException e) {
            throw new GeneratorException(e);
        }
    }

    Bom injectBomFormatAndSpecVersion(Bom bom) throws GeneratorException {
        try {
            Field field;
            field = Bom.class.getDeclaredField("bomFormat");
            field.setAccessible(true);
            field.set(bom, "CycloneDX");
            field = Bom.class.getDeclaredField("specVersion");
            field.setAccessible(true);
            field.set(bom, getSchemaVersion().getVersionString());
            return bom;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new GeneratorException(e);
        }
    }
}
