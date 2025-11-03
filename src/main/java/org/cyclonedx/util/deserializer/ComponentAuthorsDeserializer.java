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
package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.cyclonedx.model.OrganizationalContact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComponentAuthorsDeserializer extends JsonDeserializer<List<OrganizationalContact>> {

    @Override
    public List<OrganizationalContact> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<OrganizationalContact> contacts = new ArrayList<>();
        if (p instanceof FromXmlParser) { // Handle XML
            while (p.nextToken() != JsonToken.END_OBJECT && p.currentToken() != JsonToken.END_OBJECT) {
                if (p.currentToken() == JsonToken.FIELD_NAME) {
                    String fieldName = p.currentName();
                    if ("author".equals(fieldName) || "authors".equals(fieldName)) {
                        // Handles both <author> and <authors> as the item tag
                        p.nextToken();
                        contacts.add(p.readValueAs(OrganizationalContact.class));
                    } else {
                        ctxt.reportInputMismatch(
                                List.class,
                                "Unexpected field '%s' in %s",
                                fieldName,
                                getClass().getSimpleName()
                        );
                    }
                }
            }
        } else { // Handle JSON
            if (p.isExpectedStartArrayToken()) {
                while (p.nextToken() != JsonToken.END_ARRAY) {
                    // Handles case where author is a JSON object
                    contacts.add(p.readValueAs(OrganizationalContact.class));
                }
            }
        }
        return contacts;
    }
}