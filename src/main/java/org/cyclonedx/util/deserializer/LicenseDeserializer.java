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
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;

public class LicenseDeserializer extends JsonDeserializer<LicenseChoice>
{
  @Override
  public LicenseChoice deserialize(
      final JsonParser p, final DeserializationContext ctxt) throws IOException
  {
    JsonNode rootNode = p.getCodec().readTree(p);
    if (!rootNode.isEmpty()) {
      ArrayNode nodes = (rootNode.isArray() ? (ArrayNode) rootNode : new ArrayNode(null).add(rootNode));
      LicenseChoice licenseChoice = new LicenseChoice();

      for (JsonNode node : nodes) {
        if (node.has("license")) {
          processLicenseNode(p, node.get("license"), licenseChoice);
        }
        else if (node.has("expression")) {
          licenseChoice.setExpression(node.get("expression").asText());
          return licenseChoice;
        }
      }
      return licenseChoice;
    }
    return null;
  }

  private void processLicenseNode(JsonParser p, JsonNode licenseNode, LicenseChoice licenseChoice) throws IOException {
    ArrayNode licenseNodes = (licenseNode.isArray() ? (ArrayNode) licenseNode : new ArrayNode(null).add(licenseNode));

    for (JsonNode license : licenseNodes) {
      License licenseObj = p.getCodec().treeToValue(license, License.class);
      licenseChoice.addLicense(licenseObj);
    }
  }
}
