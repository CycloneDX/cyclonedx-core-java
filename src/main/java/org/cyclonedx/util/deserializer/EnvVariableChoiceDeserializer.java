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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.formulation.common.EnvVariableChoice;

public class EnvVariableChoiceDeserializer
    extends JsonDeserializer<EnvVariableChoice>
{
  @Override
  public EnvVariableChoice deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    EnvVariableChoice envReferenceChoice = new EnvVariableChoice();

    if (node.has("value")) {
      String value = node.get("value").asText();
      envReferenceChoice.setValue(value);
    } else if (node.has("environmentVar")) {
      JsonNode envVarNode = node.get("environmentVar");
      Property prop = new Property();

      if (envVarNode.has("name")) {
        String name = envVarNode.get("name").asText();
        prop.setName(name);
      }
      if (envVarNode.has("")) {
        String value = envVarNode.get("").asText();
        prop.setValue(value);
      }

      envReferenceChoice.setEnvironmentVar(prop);
    }

    return envReferenceChoice;
  }
}
