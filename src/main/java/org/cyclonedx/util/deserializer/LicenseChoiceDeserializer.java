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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.LicenseItem;
import org.cyclonedx.model.license.Expression;
import org.cyclonedx.model.license.ExpressionDetailed;

/**
 * Deserializer for LicenseChoice that handles both CycloneDX 1.6 (array-level choice)
 * and 1.7+ (item-level choice) formats.
 */
public class LicenseChoiceDeserializer extends JsonDeserializer<LicenseChoice>
{

  final ExpressionDeserializer expressionDeserializer = new ExpressionDeserializer();

  @Override
  public LicenseChoice deserialize(
      final JsonParser p, final DeserializationContext ctxt) throws IOException
  {
    final LicenseChoice licenseChoice = new LicenseChoice();

    if (p.getCodec() instanceof XmlMapper) {
      readXml(p, ctxt, licenseChoice);
    } else {
      // For JSON, the root node is an array of individual license items
      JsonNode rootNode = p.getCodec().readTree(p);
      ArrayNode nodes = DeserializerUtils.getArrayNode(rootNode, null);
      for (JsonNode node : nodes) {
        processJsonNode(p, node, licenseChoice, ctxt);
      }
    }

    return licenseChoice.getItems() == null || licenseChoice.getItems().isEmpty() ? null : licenseChoice;
  }

  private void readXml(JsonParser p, DeserializationContext ctxt, LicenseChoice licenseChoice) throws IOException {
    if (p.currentToken() != JsonToken.START_OBJECT) {
      p.skipChildren();
      return;
    }

    for (JsonToken token = p.nextToken(); token == JsonToken.FIELD_NAME; token = p.nextToken()) {
      final String fieldName = p.currentName();
      // Repeated elements arrive grouped into an array when the XML was read into a tree upstream.
      if (p.nextToken() == JsonToken.START_ARRAY) {
        while (p.nextToken() != JsonToken.END_ARRAY) {
          readXmlItem(fieldName, p, ctxt, licenseChoice);
        }
      } else {
        readXmlItem(fieldName, p, ctxt, licenseChoice);
      }
    }
  }

  private void readXmlItem(String fieldName, JsonParser p, DeserializationContext ctxt, LicenseChoice licenseChoice)
      throws IOException
  {
    final JsonNode node = p.getCodec().readTree(p);
    switch (fieldName) {
      case "license":
        processLicenseNode(p, node, licenseChoice);
        break;
      case "expression":
        processExpression(p, node, licenseChoice, ctxt);
        break;
      case "expression-detailed":
        processExpressionDetailed(p, node, licenseChoice);
        break;
      default:
        break;
    }
  }

  private void processJsonNode(JsonParser p, JsonNode node, LicenseChoice licenseChoice, DeserializationContext ctxt)
      throws IOException {
    // JSON format for 1.7+: object with "license", "expression", or "expression-detailed" property
    // JSON format for 1.6-: license/expression object directly in array
    if (node.has("expression-detailed")) {
      processExpressionDetailed(p, node.get("expression-detailed"), licenseChoice);
    }
    else if (node.has("expressionDetails") ||
        (node.has("expression") && (node.has("licensing") || node.has("properties")))) {
      // ExpressionDetailed in JSON format: has expressionDetails, or expression with licensing/properties.
      // These fields only exist on ExpressionDetailed, not on simple Expression.
      processExpressionDetailed(p, node, licenseChoice);
    }
    else if (node.has("license")) {
      // 1.7+ format: {"license": {...}}
      processLicenseNode(p, node.get("license"), licenseChoice);
    }
    else if (node.has("expression")) {
      // 1.6- format: expression object directly in array (e.g., {"expression": "MIT", "acknowledgement": "declared"})
      // The node itself IS the expression object
      processExpression(p, node, licenseChoice, ctxt);
    }
    else {
      // 1.6- format: license object directly in array
      License license = p.getCodec().treeToValue(node, License.class);
      licenseChoice.addLicense(license);
    }
  }

  private void processLicenseNode(JsonParser p, JsonNode licenseNode, LicenseChoice licenseChoice)
      throws IOException {
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
    licenseChoice.addExpression(expression);
  }

  private void processExpressionDetailed(
      final JsonParser p,
      JsonNode node,
      LicenseChoice licenseChoice) throws IOException
  {
    ExpressionDetailed expressionDetailed = p.getCodec().treeToValue(node, ExpressionDetailed.class);
    licenseChoice.addExpressionDetailed(expressionDetailed);
  }
}
