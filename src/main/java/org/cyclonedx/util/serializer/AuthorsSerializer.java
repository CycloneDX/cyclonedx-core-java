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
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.model.OrganizationalContact;

import java.io.IOException;
import java.util.List;

/**
 * Custom serializer for the Component.authors field to handle XML element naming.
 * This serializer ensures that:
 * - JSON: serializes as "authors": [...]
 * - XML: serializes as authors-author
 * This is necessary because the deprecated "author" field also uses author element,
 * creating a naming conflict that standard Jackson annotations cannot resolve.
 * Version filtering is handled by AuthorsBeanSerializerModifier, which removes this
 * property entirely for versions prior to 1.6.
 */
public class AuthorsSerializer extends StdSerializer<List<OrganizationalContact>> {

    @SuppressWarnings("unchecked")
    public AuthorsSerializer() {
        super((Class<List<OrganizationalContact>>) (Class<?>) List.class);
    }

    @Override
    public void serialize(List<OrganizationalContact> authors, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {

        if (authors == null || authors.isEmpty()) {
            return;
        }

        // Check if we're serializing to XML
        if (gen instanceof ToXmlGenerator) {
            ToXmlGenerator xmlGen = (ToXmlGenerator) gen;

            // For XML: The property name "authors" creates the wrapper <authors>
            // We need to write the array structure, and each item gets its own <author> tag
            xmlGen.writeStartArray();
            for (OrganizationalContact contact : authors) {
                // Set the element name for this array item to "author"
                xmlGen.setNextName(new javax.xml.namespace.QName("author"));
                serializers.defaultSerializeValue(contact, xmlGen);
            }
            xmlGen.writeEndArray();
        } else {
            // For JSON, write as a standard array
            gen.writeStartArray();
            for (OrganizationalContact contact : authors) {
                serializers.defaultSerializeValue(contact, gen);
            }
            gen.writeEndArray();
        }
    }
}
