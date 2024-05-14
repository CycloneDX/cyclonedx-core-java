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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.cyclonedx.model.BomReference;
import org.cyclonedx.model.component.evidence.Identity;
import org.cyclonedx.model.component.evidence.Identity.Field;
import org.cyclonedx.model.component.evidence.Method;

public class IdentityDeserializer
    extends JsonDeserializer<List<Identity>> {


    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public List<Identity> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        List<Identity> identities = new ArrayList<>();

        if(node.has("identity")) {
            node = node.get("identity");
        }

        if (node.isArray()) {
            // If the node is an array, deserialize each element individually
            for (JsonNode identityNode : node) {
                Identity singleIdentity = deserializeSingleIdentity(identityNode);
                identities.add(singleIdentity);
            }
        } else {
            // If the node is a single object, deserialize it as a single Identity
            identities.add(deserializeSingleIdentity(node));
        }
        return identities;
    }

    private Identity deserializeSingleIdentity(JsonNode node) {
        Identity identity = new Identity();

        if (node.has("field")) {
            Field field  = mapper.convertValue(node.get("field"), Field.class);
            identity.setField(field);
        }

        if (node.has("confidence")) {
            Double confidence = node.get("confidence").asDouble();
            identity.setConfidence(confidence);
        }

        if (node.has("concludedValue")) {
            String concludedValue = node.get("concludedValue").asText();
            identity.setConcludedValue(concludedValue);
        }

        if (node.has("methods")) {
            JsonNode methodsNode = node.get("methods");

            if(methodsNode.has("method")) {
                methodsNode = methodsNode.get("method");
            }

            ArrayNode nodes = (methodsNode.isArray() ? (ArrayNode) methodsNode : new ArrayNode(null).add(methodsNode));

            List<Method> methods = new ArrayList<>();
            for (JsonNode resolvesNode : nodes) {
                Method method = mapper.convertValue(resolvesNode, Method.class);
                methods.add(method);
            }
            identity.setMethods(methods);
        }

        if (node.has("tools")) {
            JsonNode toolsNode = node.get("tools");

            if(toolsNode.has("tool")) {
                toolsNode = toolsNode.get("tool");
            }

            ArrayNode nodes = (toolsNode.isArray() ? (ArrayNode) toolsNode : new ArrayNode(null).add(toolsNode));

            List<BomReference> tools = new ArrayList<>();
            for (JsonNode resolvesNode : nodes) {
                BomReference tool = mapper.convertValue(resolvesNode, BomReference.class);
                tools.add(tool);
            }

            identity.setTools(tools);
        }
        return identity;
    }
}