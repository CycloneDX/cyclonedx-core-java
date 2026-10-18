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
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cyclonedx.model.AttachmentText;
import org.cyclonedx.model.formulation.common.AbstractType;
import org.cyclonedx.model.formulation.common.ResourceReferenceChoice;

public abstract class AbstractDataTypeDeserializer<T extends AbstractType>
    extends JsonDeserializer<T> {

  private final EnvironmentVarsDeserializer environmentVarsDeserializer = new EnvironmentVarsDeserializer();
  private final PropertiesDeserializer propertiesDeserializer = new PropertiesDeserializer();

  protected abstract T createType();

  protected abstract boolean readField(String fieldName, T type, JsonParser parser, DeserializationContext ctxt)
      throws IOException;

  @Override
  public boolean isCachable() {
    return true;
  }

  @Override
  public T deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
    final T type = createType();

    JsonToken token = parser.currentToken();
    if (token == JsonToken.START_OBJECT) {
      token = parser.nextToken();
    }

    for (; token == JsonToken.FIELD_NAME; token = parser.nextToken()) {
      final String fieldName = parser.currentName();
      parser.nextToken();

      switch (fieldName) {
        case "source":
          type.setSource(ctxt.readValue(parser, ResourceReferenceChoice.class));
          break;
        case "target":
          type.setTarget(ctxt.readValue(parser, ResourceReferenceChoice.class));
          break;
        case "resource":
          type.setResource(ctxt.readValue(parser, ResourceReferenceChoice.class));
          break;
        case "environmentVars":
          type.setEnvironmentVars(environmentVarsDeserializer.deserialize(parser, ctxt));
          break;
        case "data":
          type.setData(ctxt.readValue(parser, AttachmentText.class));
          break;
        case "properties":
          type.setProperties(propertiesDeserializer.deserialize(parser, ctxt));
          break;
        default:
          if (!readField(fieldName, type, parser, ctxt)) {
            parser.skipChildren();
          }
      }
    }

    return type;
  }
}
