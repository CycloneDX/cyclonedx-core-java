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
package org.cyclonedx.util.serializer;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cyclonedx.Version;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Property;

public class PropertiesSerializer
    extends JsonSerializer<Object>
{
    private final boolean isXml;

    private final Version version;

    private final Class<?> parentClass;

    public PropertiesSerializer(Class<?> parentClass, boolean isXml, Version version) {
        this.parentClass = parentClass;
        this.version = version;
        this.isXml = isXml;
    }

    @Override
    public void serialize(
        final Object value,
        final JsonGenerator jsonGenerator,
        final SerializerProvider serializerProvider)
        throws IOException
    {
        if (value instanceof List) {
            List<Property> properties = (List<Property>) value;
            if (parentClass == Bom.class) {
                if (!isXml && version.getVersion() >= Version.VERSION_15.getVersion()) {
                    jsonGenerator.writeStartArray();
                    for (Property property : properties) {
                        jsonGenerator.writeStartObject();
                        jsonGenerator.writeObjectField("name", property.getName());
                        jsonGenerator.writeObjectField("value", property.getValue());
                        jsonGenerator.writeEndObject();
                    }
                    jsonGenerator.writeEndArray();
                }
            }
        }
    }

    @Override
    public Class<Object> handledType() {
        return Object.class;
    }
}
