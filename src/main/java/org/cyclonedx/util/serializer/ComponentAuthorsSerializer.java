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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.model.OrganizationalContact;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.List;

public class ComponentAuthorsSerializer extends StdSerializer<List<OrganizationalContact>> {


    public ComponentAuthorsSerializer() {
        super(List.class, false);
    }


    @Override
    public void serialize(List<OrganizationalContact> authors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (jsonGenerator instanceof ToXmlGenerator) { // Handle XML
            ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
            xmlGenerator.writeStartArray();

            for (OrganizationalContact author : authors) {
                xmlGenerator.setNextName(new QName("author"));
                xmlGenerator.writeObject(author);
            }

            xmlGenerator.writeEndArray();
        } else { // Handle JSON, as default.
            JsonSerializer<Object> defaultSerializer =
                    serializerProvider.findValueSerializer(List.class, null);
            defaultSerializer.serialize(authors, jsonGenerator, serializerProvider);
        }
    }

}
