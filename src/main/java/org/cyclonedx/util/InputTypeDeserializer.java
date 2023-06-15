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
package org.cyclonedx.util;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cyclonedx.model.AttachmentText;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.formulation.common.InputType;
import org.cyclonedx.model.formulation.common.InputType.Parameter;
import org.cyclonedx.model.formulation.common.ResourceReferenceChoice;

public class InputTypeDeserializer extends JsonDeserializer<InputType> {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public InputType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    InputType inputType = new InputType();

    if (node.has("source")) {
      JsonNode sourceNode = node.get("source");
      ResourceReferenceChoice source = objectMapper.treeToValue(sourceNode, ResourceReferenceChoice.class);
      inputType.setSource(source);
    } else if (node.has("target")) {
      JsonNode targetNode = node.get("target");
      ResourceReferenceChoice target = objectMapper.treeToValue(targetNode, ResourceReferenceChoice.class);
      inputType.setTarget(target);
    } else if (node.has("resource")) {
      JsonNode resourceNode = node.get("resource");
      ResourceReferenceChoice resource = objectMapper.treeToValue(resourceNode, ResourceReferenceChoice.class);
      inputType.setResource(resource);
    } else if (node.has("parameters")) {
      JsonNode parametersNode = node.get("parameters");
      List<Parameter> parameters = objectMapper.convertValue(parametersNode, new TypeReference<List<Parameter>>() {});
      inputType.setParameters(parameters);
    } else if (node.has("environmentVars")) {
      JsonNode environmentVarsNode = node.get("environmentVars");
      List<Property> environmentVars = objectMapper.convertValue(environmentVarsNode, new TypeReference<List<Property>>() {});
      inputType.setEnvironmentVars(environmentVars);
    } else if (node.has("data")) {
      JsonNode dataNode = node.get("data");
      AttachmentText data = objectMapper.treeToValue(dataNode, AttachmentText.class);
      inputType.setData(data);
    }

    return inputType;
  }
}
