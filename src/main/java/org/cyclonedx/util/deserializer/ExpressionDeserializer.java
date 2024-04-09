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
import org.cyclonedx.model.license.Expression;

public class ExpressionDeserializer
    extends JsonDeserializer<Expression>
{
  @Override
  public Expression deserialize(
      final JsonParser p, final DeserializationContext ctxt) throws IOException
  {
    JsonNode node = p.getCodec().readTree(p);

    boolean hasParent = node.has("expression") && node.get("expression").isObject();

    if (hasParent) {
      node = node.get("expression");
    }

    if (node.isTextual()) {
      return new Expression(node.asText());
    }
    else {

      Expression expression = new Expression();

      if (node.get("bom-ref") != null) {
        expression.setBomRef(node.get("bom-ref").asText());
      }

      if (node.get("acknowledgement") != null) {
        expression.setAcknowledgement(node.get("acknowledgement").asText());
      }

      JsonNode textNode = node.get("expression");
      if (textNode != null) {
        expression.setId(textNode.textValue());
      }
      else if (node.has("")) {
        expression.setId(node.get("").asText());
      }

      return expression;
    }
  }
}
