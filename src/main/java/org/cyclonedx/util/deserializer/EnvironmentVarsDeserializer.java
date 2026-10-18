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
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.formulation.common.EnvironmentVars;

public class EnvironmentVarsDeserializer extends StdDeserializer<EnvironmentVars> {

  public EnvironmentVarsDeserializer() {
    this(null);
  }

  public EnvironmentVarsDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public boolean isCachable() {
    return true;
  }

  @Override
  public EnvironmentVars deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
    final List<Object> choices = new ArrayList<>();

    if (parser.currentToken() == JsonToken.START_ARRAY) {
      while (parser.nextToken() != JsonToken.END_ARRAY) {
        if (parser.currentToken() == JsonToken.VALUE_STRING) {
          choices.add(parser.getText());
        } else if (parser.currentToken() == JsonToken.START_OBJECT) {
          choices.add(ctxt.readValue(parser, Property.class));
        } else {
          parser.skipChildren();
        }
      }
    } else if (parser.currentToken() == JsonToken.START_OBJECT) {
      for (JsonToken token = parser.nextToken(); token == JsonToken.FIELD_NAME; token = parser.nextToken()) {
        final String fieldName = parser.currentName();
        parser.nextToken();

        if ("environmentVar".equals(fieldName)) {
          choices.add(ctxt.readValue(parser, Property.class));
        } else if ("value".equals(fieldName) && parser.currentToken().isScalarValue()) {
          choices.add(parser.getValueAsString());
        } else {
          parser.skipChildren();
        }
      }
    }

    final EnvironmentVars environmentVars = new EnvironmentVars();
    environmentVars.setChoices(choices);
    return environmentVars;
  }
}
