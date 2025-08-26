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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.cyclonedx.model.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Custom deserializer for List&lt;Component&gt; that handles XML parsing issues where
 * single nested components might be represented as objects instead of arrays.
 * 
 * This addresses GitHub issue #663 where nested components in metadata fail to parse
 * due to XML-to-JSON mapping inconsistencies.
 */
public class ComponentListDeserializer extends JsonDeserializer<List<Component>> {

    @Override
    public List<Component> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonToken currentToken = parser.getCurrentToken();
        
        if (currentToken == JsonToken.START_ARRAY) {
            // Handle normal array case
            return Arrays.asList(parser.readValueAs(Component[].class));
        } else if (currentToken == JsonToken.START_OBJECT) {
            // Handle single object case (common in XML parsing)
            ObjectMapper mapper = getMapper(parser);
            ObjectNode node = parser.readValueAs(ObjectNode.class);
            
            if (node.has("component")) {
                JsonNode componentNode = node.get("component");
                return deserializeComponentNode(componentNode, parser, mapper);
            } else {
                // If the object doesn't have a "component" field, treat the whole object as a single component
                Component component = mapper.convertValue(node, Component.class);
                return Collections.singletonList(component);
            }
        } else if (currentToken == JsonToken.VALUE_NULL) {
            return null;
        } else {
            // Try to deserialize as a single component
            ObjectMapper mapper = getMapper(parser);
            Component component = parser.readValueAs(Component.class);
            return Collections.singletonList(component);
        }
    }
    
    /**
     * Deserializes a component node that might be either a single component or an array of components
     */
    private List<Component> deserializeComponentNode(JsonNode componentNode, JsonParser originalParser, ObjectMapper mapper) throws IOException {
        try (JsonParser componentParser = componentNode.traverse(originalParser.getCodec())) {
            componentParser.nextToken(); // Advance to the first token
            
            if (componentNode.isArray()) {
                return Arrays.asList(componentParser.readValueAs(Component[].class));
            } else {
                Component component = componentParser.readValueAs(Component.class);
                return Collections.singletonList(component);
            }
        }
    }
    
    /**
     * Gets the ObjectMapper from the JsonParser codec or creates a new one
     */
    private ObjectMapper getMapper(JsonParser parser) {
        if (parser.getCodec() instanceof ObjectMapper) {
            return (ObjectMapper) parser.getCodec();
        } else {
            return new ObjectMapper();
        }
    }
}