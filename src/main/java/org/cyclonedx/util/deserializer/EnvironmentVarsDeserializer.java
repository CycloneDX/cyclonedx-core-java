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
import com.fasterxml.jackson.databind.JsonNode;
import org.cyclonedx.model.Property;

import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.cyclonedx.model.formulation.common.EnvironmentVars;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentVarsDeserializer extends StdDeserializer<EnvironmentVars> {

  public EnvironmentVarsDeserializer() {
    this(null);
  }

  public EnvironmentVarsDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public EnvironmentVars deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException{
    JsonNode node = jp.getCodec().readTree(jp);
    EnvironmentVars environmentVars = new EnvironmentVars();
    List<Object> choices = new ArrayList<>();

      if(node.isObject()) {
        if (node.has("value")) {
          if(node.has("name")){
            Property environmentVar = createProperty(node);
            choices.add(environmentVar);
          } else {
            choices.add(node.get("value").asText());
          }
        }
        else if (node.has("environmentVar")) {
          JsonNode envVarNode = node.get("environmentVar");
          Property environmentVar = createProperty(envVarNode);
          choices.add(environmentVar);
        }
      } else {
        choices.add(node.asText());
      }

    environmentVars.setChoices(choices);
    return environmentVars;
  }

  private Property createProperty(JsonNode envVarNode){
    Property prop = new Property();

    if (envVarNode.has("name")) {
      String name = envVarNode.get("name").asText();
      prop.setName(name);
    }
    if (envVarNode.has("")) {
      String value = envVarNode.get("").asText();
      prop.setValue(value);
    } else  if (envVarNode.has("value")) {
      String value = envVarNode.get("value").asText();
      prop.setValue(value);
    }
    return prop;
  }
}
