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
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.cyclonedx.model.OrganizationalChoice;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;

/**
 * Deserializer for OrganizationalChoice that handles:
 * 1. Wrapped format: {"individual": {...}} or {"organization": {...}} (Licensing)
 * 2. String format: "bom-ref" (reference to an organization defined elsewhere)
 * 3. XML choice format: {"ref": "bom-ref"} or {"contact": {...}} (XML asserter)
 * 4. Unwrapped format: direct entity/contact object fields (patent asserter/assignee JSON)
 */
public class OrganizationalChoiceDeserializer extends JsonDeserializer<OrganizationalChoice>
{
  @Override
  public OrganizationalChoice deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
  {
    if (p.currentToken() == JsonToken.VALUE_STRING) {
      final OrganizationalChoice choice = new OrganizationalChoice();
      choice.setOrganization(referenceTo(p.getText()));
      return choice;
    }
    if (p.currentToken() != JsonToken.START_OBJECT) {
      p.skipChildren();
      return new OrganizationalChoice();
    }
    if (p.getCodec() instanceof XmlMapper) {
      return readWrapped(p, ctxt);
    }

    final TokenBuffer buffer = ctxt.bufferAsCopyOfValue(p);
    final Set<String> fieldNames = DeserializerUtils.fieldNames(buffer);

    try (final JsonParser choiceParser = buffer.asParserOnFirstToken()) {
      if (fieldNames.contains("individual") || fieldNames.contains("organization") || fieldNames.contains("ref")) {
        return readWrapped(choiceParser, ctxt);
      }

      final OrganizationalChoice choice = new OrganizationalChoice();
      if (fieldNames.contains("email") || fieldNames.contains("phone")) {
        choice.setIndividual(ctxt.readValue(choiceParser, OrganizationalContact.class));
      } else if (!fieldNames.isEmpty()) {
        choice.setOrganization(ctxt.readValue(choiceParser, OrganizationalEntity.class));
      }

      return choice;
    }
  }

  private static OrganizationalChoice readWrapped(JsonParser p, DeserializationContext ctxt) throws IOException {
    OrganizationalContact individual = null;
    OrganizationalEntity organization = null;
    String ref = null;
    OrganizationalContact contact = null;

    for (JsonToken token = p.nextToken(); token == JsonToken.FIELD_NAME; token = p.nextToken()) {
      final String fieldName = p.currentName();
      p.nextToken();

      switch (fieldName) {
        case "individual":
          individual = ctxt.readValue(p, OrganizationalContact.class);
          break;
        case "organization":
          organization = ctxt.readValue(p, OrganizationalEntity.class);
          break;
        case "ref":
          ref = DeserializerUtils.readText(p);
          break;
        case "contact":
          contact = ctxt.readValue(p, OrganizationalContact.class);
          break;
        default:
          p.skipChildren();
      }
    }

    final OrganizationalChoice choice = new OrganizationalChoice();
    if (individual != null) {
      choice.setIndividual(individual);
    } else if (organization != null) {
      choice.setOrganization(organization);
    } else if (ref != null) {
      choice.setOrganization(referenceTo(ref));
    } else {
      choice.setIndividual(contact);
    }

    return choice;
  }

  private static OrganizationalEntity referenceTo(String bomRef) {
    final OrganizationalEntity organization = new OrganizationalEntity();
    organization.setBomRef(bomRef);
    return organization;
  }
}
