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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.formulation.common.ResourceReferenceChoice;

import java.io.IOException;

public class ResourceReferenceChoiceDeserializer extends JsonDeserializer<ResourceReferenceChoice>
{
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public ResourceReferenceChoice deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    ResourceReferenceChoice resourceReferenceChoice = new ResourceReferenceChoice();

    if (node.has("ref")) {
      resourceReferenceChoice.setRef(node.get("ref").asText());
    } else if (node.has("externalReference")) {
      JsonNode externalReferenceNode = node.get("externalReference");
      ExternalReference externalReference = objectMapper.treeToValue(externalReferenceNode, ExternalReference.class);
      resourceReferenceChoice.setExternalReference(externalReference);
    }

    return resourceReferenceChoice;
  }
}
