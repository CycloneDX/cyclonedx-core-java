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
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.cyclonedx.model.LifecycleChoice;
import org.cyclonedx.model.LifecycleChoice.Phase;
import org.cyclonedx.model.Lifecycles;

public class LifecycleDeserializer
    extends JsonDeserializer<Lifecycles>
{
  @Override
  public Lifecycles deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException
  {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    List<LifecycleChoice> choices = new ArrayList<>();

    if (jsonParser instanceof FromXmlParser) {
      JsonNode lifecycleNode = node.get("lifecycle");

      if (lifecycleNode != null) {
        // If it's an array of lifecycle
        if (lifecycleNode.isArray()) {
          for (JsonNode choiceNode : lifecycleNode) {
            LifecycleChoice choice = createLifecycleChoice(choiceNode);
            if (choice != null) {
              choices.add(choice);
            }
          }
        }
        // If it's a single lifecycle
        else {
          LifecycleChoice choice = createLifecycleChoice(lifecycleNode);
          if (choice != null) {
            choices.add(choice);
          }
        }
      }
    }
    else {
      if (node != null && node.isArray()) {
        for (JsonNode choiceNode : node) {
          LifecycleChoice choice = createLifecycleChoice(choiceNode);
          if (choice != null) {
            choices.add(choice);
          }
        }
      }
    }

    Lifecycles lifecycles = new Lifecycles();
    lifecycles.setLifecycleChoice(choices);
    return lifecycles;
  }

  private LifecycleChoice createLifecycleChoice(JsonNode choiceNode) {
    LifecycleChoice choice = new LifecycleChoice();
    JsonNode phaseNode = choiceNode.get("phase");
    if (phaseNode != null) {
      Phase phase = Phase.fromString(phaseNode.asText());
      choice.setPhase(phase);
      return choice;
    }

    JsonNode nameNode = choiceNode.get("name");
    JsonNode descriptionNode = choiceNode.get("description");
    if (nameNode != null && descriptionNode != null) {
      choice.setName(nameNode.asText());
      choice.setDescription(descriptionNode.asText());
      return choice;
    }

    return null;
  }
}
