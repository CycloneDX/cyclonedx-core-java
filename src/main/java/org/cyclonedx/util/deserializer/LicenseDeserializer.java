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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.license.Expression;

public class LicenseDeserializer extends JsonDeserializer<LicenseChoice>
{

  final ExpressionDeserializer expressionDeserializer = new ExpressionDeserializer();

  @Override
  public LicenseChoice deserialize(
      final JsonParser p, final DeserializationContext ctxt) throws IOException
  {
    ObjectMapper codec = (ObjectMapper) p.getCodec();
    boolean isXml = codec instanceof XmlMapper;
    JsonNode rootNode = p.getCodec().readTree(p);

    if (!rootNode.isEmpty()) {
      ArrayNode nodes = DeserializerUtils.getArrayNode(rootNode, null);
      LicenseChoice licenseChoice = new LicenseChoice();

      for (JsonNode node : nodes) {
        if(isXml) {
          if (node.has("license")) {
            processLicenseNode(p, node.get("license"), licenseChoice);
          }
          else {
            processExpression(p, node, licenseChoice, ctxt);
          }
        } else {
          if (node.has("expression")) {
            processExpression(p, node, licenseChoice, ctxt);
          }
          else {
            processLicenseNode(p, node, licenseChoice);
          }
        }
      }
      return licenseChoice;
    }
    return null;
  }

  private void processLicenseNode(JsonParser p, JsonNode licenseNode, LicenseChoice licenseChoice) throws IOException {
    ArrayNode licenseNodes = DeserializerUtils.getArrayNode(licenseNode, null);

    for (JsonNode license : licenseNodes) {
      License licenseObj = p.getCodec().treeToValue(license, License.class);
      licenseChoice.addLicense(licenseObj);
    }
  }

  private void processExpression(
      final JsonParser p,
      JsonNode node,
      LicenseChoice licenseChoice,
      DeserializationContext ctxt) throws IOException
  {
    JsonParser expressionParser = node.traverse(p.getCodec());
    expressionParser.nextToken();
    Expression expression = expressionDeserializer.deserialize(expressionParser, ctxt);
    licenseChoice.setExpression(expression);
  }
}
