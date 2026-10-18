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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.formulation.common.ResourceReferenceChoice;

public class ResourceReferenceChoiceDeserializer extends JsonDeserializer<ResourceReferenceChoice>
{
  @Override
  public ResourceReferenceChoice deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    final ResourceReferenceChoice resourceReferenceChoice = new ResourceReferenceChoice();
    if (jsonParser.currentToken() != JsonToken.START_OBJECT) {
      jsonParser.skipChildren();
      return resourceReferenceChoice;
    }

    String ref = null;
    ExternalReference externalReference = null;

    for (JsonToken token = jsonParser.nextToken(); token == JsonToken.FIELD_NAME; token = jsonParser.nextToken()) {
      final String fieldName = jsonParser.currentName();
      jsonParser.nextToken();

      if ("ref".equals(fieldName) && jsonParser.currentToken().isScalarValue()) {
        ref = jsonParser.getValueAsString();
      } else if ("externalReference".equals(fieldName)) {
        externalReference = deserializationContext.readValue(jsonParser, ExternalReference.class);
      } else {
        jsonParser.skipChildren();
      }
    }

    if (ref != null) {
      resourceReferenceChoice.setRef(ref);
    } else {
      resourceReferenceChoice.setExternalReference(externalReference);
    }

    return resourceReferenceChoice;
  }
}
