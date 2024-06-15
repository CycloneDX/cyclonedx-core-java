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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.cyclonedx.model.formulation.common.AbstractType;
import org.cyclonedx.model.formulation.common.EnvVariableChoice;
import org.cyclonedx.model.formulation.common.ResourceReferenceChoice;

public abstract class AbstractDataTypeDeserializer<T extends AbstractType>
    extends JsonDeserializer<T> {

  protected final ObjectMapper objectMapper = new ObjectMapper();

  protected void setEnvironmentVars(final JsonNode node, AbstractType data) throws JsonProcessingException {
    JsonNode nodes = node.get("environmentVars");
    List<EnvVariableChoice> environmentVars = new ArrayList<>();

    if (nodes != null) {
      ArrayNode environmentVarsNode = DeserializerUtils.getArrayNode(nodes, objectMapper);
      for (JsonNode envVarNode : environmentVarsNode) {
        EnvVariableChoice envVar = objectMapper.treeToValue(envVarNode, EnvVariableChoice.class);
        environmentVars.add(envVar);
      }
    }

    data.setEnvironmentVars(environmentVars);
  }

  protected void setReference(JsonNode node, String fieldName, AbstractType type) throws JsonProcessingException {
    if (node.has(fieldName)) {
      JsonNode fieldNode = node.get(fieldName);
      ResourceReferenceChoice reference = objectMapper.treeToValue(fieldNode, ResourceReferenceChoice.class);

      if ("source".equals(fieldName)) {
        type.setSource(reference);
      } else if ("target".equals(fieldName)) {
        type.setTarget(reference);
      }
    }
  }

  protected void setSourceAndTarget(JsonNode node, AbstractType type) throws JsonProcessingException {
    setReference(node, "source", type);
    setReference(node, "target", type);
  }
}
