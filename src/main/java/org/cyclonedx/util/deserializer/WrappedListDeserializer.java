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
 * e.g. {@code <commits><commit/></commits>}. Repeated XML items may arrive as repeated fields or,
 * if the XML was read into a tree upstream, as an array. Items deserialized to {@code null} are dropped.
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
        // NB: Checks the codec rather than the parser, so XML that was read into a tree upstream counts as XML too.
        if (!(parser.getCodec() instanceof XmlMapper) && parser.currentToken() != JsonToken.START_ARRAY) {
            return context.reportInputMismatch(this, "Expected an array of %s", elementName);
        }

        final List<T> items = new ArrayList<>();

        if (parser.currentToken() == JsonToken.START_ARRAY) {
            readArrayItems(parser, context, items);
        } else if (parser.currentToken() == JsonToken.START_OBJECT) {
            for (JsonToken token = parser.nextToken(); token == JsonToken.FIELD_NAME; token = parser.nextToken()) {
                final String fieldName = parser.currentName();
                final JsonToken valueToken = parser.nextToken();

                if (!elementName.equals(fieldName)) {
                    parser.skipChildren();
                } else if (valueToken == JsonToken.START_ARRAY) {
                    readArrayItems(parser, context, items);
                } else {
                    addIfNotNull(items, context.readValue(parser, elementType));
                }
            }
        } else {
            parser.skipChildren();
        }

        return items;
    }

    private void readArrayItems(JsonParser parser, DeserializationContext context, List<T> items) throws IOException {
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            addIfNotNull(items, context.readValue(parser, elementType));
        }
    }

    private static <T> void addIfNotNull(List<T> items, T item) {
        if (item != null) {
            items.add(item);
        }
    }
}
