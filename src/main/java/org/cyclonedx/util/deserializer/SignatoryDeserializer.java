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
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Signature;
import org.cyclonedx.model.attestation.affirmation.Signatory;

public class SignatoryDeserializer
    extends JsonDeserializer<Signatory>
{
  @Override
  public boolean isCachable() {
    return true;
  }

  @Override
  public Signatory deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
    final Signatory signatory = new Signatory();
    if (parser.currentToken() != JsonToken.START_OBJECT) {
      parser.skipChildren();
      return signatory;
    }

    Signature signature = null;
    OrganizationalEntity organization = null;
    ExternalReference externalReference = null;

    for (JsonToken token = parser.nextToken(); token == JsonToken.FIELD_NAME; token = parser.nextToken()) {
      final String fieldName = parser.currentName();
      parser.nextToken();

      switch (fieldName) {
        case "name":
          signatory.setName(DeserializerUtils.readText(parser));
          break;
        case "role":
          signatory.setRole(DeserializerUtils.readText(parser));
          break;
        case "signature":
          signature = ctxt.readValue(parser, Signature.class);
          break;
        case "organization":
          organization = ctxt.readValue(parser, OrganizationalEntity.class);
          break;
        case "externalReference":
          externalReference = ctxt.readValue(parser, ExternalReference.class);
          break;
        default:
          parser.skipChildren();
      }
    }

    if (signature != null) {
      signatory.setSignature(signature);
    } else if (organization != null && externalReference != null) {
      signatory.setExternalReferenceAndOrganization(externalReference, organization);
    }

    return signatory;
  }
}
