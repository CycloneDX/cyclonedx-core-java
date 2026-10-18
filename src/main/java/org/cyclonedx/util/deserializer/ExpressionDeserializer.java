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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cyclonedx.model.license.Acknowledgement;
import org.cyclonedx.model.license.Expression;

import java.io.IOException;

public class ExpressionDeserializer
    extends JsonDeserializer<Expression>
{
  @Override
  public boolean isCachable() {
    return true;
  }

  @Override
  public Expression deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
    if (p.currentToken().isScalarValue()) {
      return new Expression(readTrimmed(p));
    }

    final Expression expression = new Expression();
    if (p.currentToken() != JsonToken.START_OBJECT) {
      p.skipChildren();
      return expression;
    }

    Expression nested = null;
    String text = null;

    for (JsonToken token = p.nextToken(); token == JsonToken.FIELD_NAME; token = p.nextToken()) {
      final String fieldName = p.currentName();
      final JsonToken valueToken = p.nextToken();

      if ("expression".equals(fieldName) && valueToken == JsonToken.START_OBJECT) {
        nested = deserialize(p, ctxt);
        continue;
      }
      if (!valueToken.isScalarValue()) {
        p.skipChildren();
        continue;
      }

      switch (fieldName) {
        case "bom-ref":
          expression.setBomRef(p.getValueAsString());
          break;
        case "acknowledgement":
          expression.setAcknowledgement(Acknowledgement.fromString(p.getValueAsString()));
          break;
        case "expression":
          expression.setValue(readTrimmed(p));
          break;
        // NB: For XML, the expression is the element's text, which gets translated
        // to a field with empty name.
        case "":
          text = readTrimmed(p);
          break;
        default:
          break;
      }
    }

    if (nested != null) {
      return nested;
    }
    if (expression.getValue() == null) {
      expression.setValue(text);
    }

    return expression;
  }

  private static String readTrimmed(final JsonParser p) throws IOException {
    final String text = DeserializerUtils.readText(p);
    return text == null ? null : text.trim();
  }
}
