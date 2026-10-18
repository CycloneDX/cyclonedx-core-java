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
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializes a list from a JSON array, or from XML where a container element wraps the items,
 * e.g. {@code <commits><commit/></commits>}.
 */
public abstract class WrappedListDeserializer<T> extends JsonDeserializer<List<T>> {

    private final String elementName;
    private final Class<T> elementType;

    protected WrappedListDeserializer(String elementName, Class<T> elementType) {
        this.elementName = elementName;
        this.elementType = elementType;
    }

    @Override
    public List<T> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        final JavaType listType = context.getTypeFactory().constructCollectionType(ArrayList.class, elementType);

        // NB: Do not read XML into a tree, as it loses wrapper handling for nested lists such as Patch.resolves.
        if (parser instanceof FromXmlParser || parser.currentToken() == JsonToken.START_ARRAY) {
            return context.readValue(parser, listType);
        }

        // XML-via-JsonNode path (e.g. ToolInformationDeserializer): wrapper element
        // <commits><commit>...</commit></commits> becomes {"commit": {...}} or {"commit": [{...},...]}
        final JsonNode node = context.readTree(parser);
        if (node.isTextual() && node.textValue().isEmpty()) {
            return new ArrayList<>();
        }

        final JsonNode elements = node.get(elementName);
        if (elements == null) {
            return context.reportInputMismatch(listType, "Expected an array or <%s> elements", elementName);
        }
        if (elements.isArray()) {
            return context.readTreeAsValue(elements, listType);
        }

        final List<T> list = new ArrayList<>();
        list.add(context.readTreeAsValue(elements, elementType));
        return list;
    }
}
