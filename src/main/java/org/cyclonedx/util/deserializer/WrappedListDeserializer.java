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
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializes a list from a JSON array, or from XML where a container element wraps the items,
 * e.g. {@code <commits><commit/></commits>}, where repeated items arrive as repeated fields.
 * Items deserialized to {@code null} are dropped.
 */
public class WrappedListDeserializer<T> extends JsonDeserializer<List<T>> {

    private final String elementName;
    private final Class<T> elementType;

    protected WrappedListDeserializer(String elementName, Class<T> elementType) {
        this.elementName = elementName;
        this.elementType = elementType;
    }

    @Override
    public List<T> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (!(parser.getCodec() instanceof XmlMapper) && parser.currentToken() != JsonToken.START_ARRAY) {
            return context.reportInputMismatch(this, "Expected an array of %s", elementName);
        }

        final List<T> items = new ArrayList<>();

        if (parser.currentToken() == JsonToken.START_ARRAY) {
            DeserializerUtils.readItemsInto(parser, context, elementType, items);
        } else if (parser.currentToken() == JsonToken.START_OBJECT) {
            for (JsonToken token = parser.nextToken(); token == JsonToken.FIELD_NAME; token = parser.nextToken()) {
                final String fieldName = parser.currentName();
                parser.nextToken();

                if (elementName.equals(fieldName)) {
                    DeserializerUtils.readItemsInto(parser, context, elementType, items);
                } else {
                    parser.skipChildren();
                }
            }
        } else {
            parser.skipChildren();
        }

        return items;
    }
}
