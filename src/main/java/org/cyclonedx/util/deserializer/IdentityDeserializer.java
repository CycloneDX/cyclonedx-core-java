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
    extends JsonDeserializer<List<Identity>>
{
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public List<Identity> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException
  {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    return parseIdentities(node);
  }

  private List<Identity> parseIdentities(JsonNode node) {
    List<Identity> identities = new ArrayList<>();

    if (node.has("identity")) {
      node = node.get("identity");
    }

    if (node.isArray()) {
      for (JsonNode identityNode : node) {
        identities.add(parseSingleIdentity(identityNode));
      }
    }
    else {
      identities.add(parseSingleIdentity(node));
    }

    return identities;
  }

  private Identity parseSingleIdentity(JsonNode node) {
    Identity identity = new Identity();

    if (node.has("field")) {
      Field field = mapper.convertValue(node.get("field"), Field.class);
      identity.setField(field);
    }

    if (node.has("confidence")) {
      identity.setConfidence(node.get("confidence").asDouble());
    }

    if (node.has("concludedValue")) {
      identity.setConcludedValue(node.get("concludedValue").asText());
    }

    if (node.has("methods")) {
      identity.setMethods(parseMethods(node.get("methods")));
    }

    if (node.has("tools")) {
      identity.setTools(parseTools(node.get("tools")));
    }

    return identity;
  }

  private List<Method> parseMethods(JsonNode methodsNode) {
    if (methodsNode.has("method")) {
      methodsNode = methodsNode.get("method");
    }

    List<Method> methods = new ArrayList<>();
    ArrayNode nodes =  DeserializerUtils.getArrayNode(methodsNode, mapper);
    for (JsonNode methodNode : nodes) {
      methods.add(mapper.convertValue(methodNode, Method.class));
    }
    return methods;
  }

    private List<BomReference> parseTools(JsonNode toolsNode) {
        if (toolsNode.has("tool")) {
            toolsNode = toolsNode.get("tool");
        }
        ArrayNode nodes = DeserializerUtils.getArrayNode(toolsNode, mapper);
        List<BomReference> tools = new ArrayList<>();
        for (JsonNode toolNode : nodes) {
            tools.add(mapper.convertValue(toolNode, BomReference.class));
        }
        return tools;
    }
}