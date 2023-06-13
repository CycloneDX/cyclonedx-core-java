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
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;

public class LicenseDeserializer extends JsonDeserializer<LicenseChoice>
{
  private final ObjectMapper mapper;

  public LicenseDeserializer() {
    this.mapper = new ObjectMapper();
  }

  @Override
  public LicenseChoice deserialize(
    final JsonParser p, final DeserializationContext ctxt) throws IOException
  {
    if (p instanceof FromXmlParser) {
      return p.readValueAs(LicenseChoice.class);
    }
    ArrayNode nodes = p.readValueAsTree();

    TypeFactory factory = TypeFactory.defaultInstance();
    MapType type = factory.constructMapType(HashMap.class, String.class, License.class);
    LicenseChoice licenseChoice = new LicenseChoice();
    for (JsonNode node : nodes) {
      HashMap<String, License> map;
      try
      {
        map = this.mapper.readValue(node.toString(), type);
        if(map.get("license") != null)
        {
          licenseChoice.addLicense(map.get("license"));
        }
      }
      catch(JsonProcessingException e)
      {
        // Check for expressions
        licenseChoice = this.mapper.readValue(node.toString(), LicenseChoice.class);
      }
    }

    return licenseChoice;
  }
}
