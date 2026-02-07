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
import org.cyclonedx.model.OrganizationalChoice;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;

/**
 * Deserializer for OrganizationalChoice that handles both:
 * 1. Object format: {"individual": {...}} or {"organization": {...}}
 * 2. String format: "bom-ref" (reference to an organization defined elsewhere)
 */
public class OrganizationalChoiceDeserializer extends JsonDeserializer<OrganizationalChoice>
{
  @Override
  public OrganizationalChoice deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
  {
    if (p.currentToken() == JsonToken.VALUE_STRING) {
      // Simple string format - this is a bom-ref reference
      String bomRef = p.getText();
      OrganizationalEntity org = new OrganizationalEntity();
      org.setBomRef(bomRef);

      OrganizationalChoice choice = new OrganizationalChoice();
      choice.setOrganization(org);
      return choice;
    }

    // Object format - deserialize normally
    JsonNode node = p.getCodec().readTree(p);
    OrganizationalChoice choice = new OrganizationalChoice();

    if (node.has("individual")) {
      OrganizationalContact individual = p.getCodec().treeToValue(node.get("individual"), OrganizationalContact.class);
      choice.setIndividual(individual);
    } else if (node.has("organization")) {
      OrganizationalEntity organization = p.getCodec().treeToValue(node.get("organization"), OrganizationalEntity.class);
      choice.setOrganization(organization);
    }

    return choice;
  }
}
