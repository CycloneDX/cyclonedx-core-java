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
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.license.Expression;
import org.cyclonedx.model.license.ExpressionDetailed;

/**
 * Deserializer for LicenseChoice that handles both CycloneDX 1.6 (array-level choice)
 * and 1.7+ (item-level choice) formats.
 */
public class LicenseChoiceDeserializer extends JsonDeserializer<LicenseChoice>
{
  @Override
  public boolean isCachable()
  {
    return true;
  }

  @Override
  public LicenseChoice deserialize(
      final JsonParser p, final DeserializationContext ctxt) throws IOException
  {
    final LicenseChoice licenseChoice = new LicenseChoice();

    if (p.getCodec() instanceof XmlMapper) {
      readXml(p, ctxt, licenseChoice);
    } else if (p.currentToken() == JsonToken.START_ARRAY) {
      while (p.nextToken() != JsonToken.END_ARRAY) {
        readJsonItem(p, ctxt, licenseChoice);
      }
    } else {
      readJsonItem(p, ctxt, licenseChoice);
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
      p.nextToken();

      switch (fieldName) {
        case "license":
          readItems(p, ctxt, License.class).forEach(licenseChoice::addLicense);
          break;
        case "expression":
          readItems(p, ctxt, Expression.class).forEach(licenseChoice::addExpression);
          break;
        case "expression-detailed":
          readItems(p, ctxt, ExpressionDetailed.class).forEach(licenseChoice::addExpressionDetailed);
          break;
        default:
          p.skipChildren();
      }
    }
  }

  // JSON format for 1.7+: object with "license", "expression", or "expression-detailed" property.
  // JSON format for 1.6-: license or expression object directly in the array.
  private void readJsonItem(JsonParser p, DeserializationContext ctxt, LicenseChoice licenseChoice)
      throws IOException
  {
    final TokenBuffer buffer = ctxt.bufferAsCopyOfValue(p);
    final Set<String> fieldNames = DeserializerUtils.fieldNames(buffer);

    if (fieldNames.contains("expression-detailed")) {
      try (final JsonParser valueParser = DeserializerUtils.fieldValue(buffer, "expression-detailed")) {
        licenseChoice.addExpressionDetailed(ctxt.readValue(valueParser, ExpressionDetailed.class));
      }
    } else if (fieldNames.contains("expressionDetails") || (fieldNames.contains("expression")
        && (fieldNames.contains("licensing") || fieldNames.contains("properties")))) {
      // These fields only exist on ExpressionDetailed, not on simple Expression.
      try (final JsonParser itemParser = buffer.asParserOnFirstToken()) {
        licenseChoice.addExpressionDetailed(ctxt.readValue(itemParser, ExpressionDetailed.class));
      }
    } else if (fieldNames.contains("license")) {
      try (final JsonParser valueParser = DeserializerUtils.fieldValue(buffer, "license")) {
        readItems(valueParser, ctxt, License.class).forEach(licenseChoice::addLicense);
      }
    } else if (fieldNames.contains("expression")) {
      try (final JsonParser itemParser = buffer.asParserOnFirstToken()) {
        licenseChoice.addExpression(ctxt.readValue(itemParser, Expression.class));
      }
    } else {
      try (final JsonParser itemParser = buffer.asParserOnFirstToken()) {
        licenseChoice.addLicense(ctxt.readValue(itemParser, License.class));
      }
    }
  }

  private static <T> List<T> readItems(JsonParser p, DeserializationContext ctxt, Class<T> type) throws IOException {
    final List<T> items = new ArrayList<>();
    DeserializerUtils.readItemsInto(p, ctxt, type, items);
    return items;
  }
}
