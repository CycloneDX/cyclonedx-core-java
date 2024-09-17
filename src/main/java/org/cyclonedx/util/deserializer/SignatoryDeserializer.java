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
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Signature;
import org.cyclonedx.model.attestation.affirmation.Signatory;

public class SignatoryDeserializer
    extends JsonDeserializer<Signatory>
{
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public Signatory deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException
  {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    return parseSignatory(node);
  }

  private Signatory parseSignatory(JsonNode node) throws IOException {
    Signatory signatory = new Signatory();

    if (node.has("name")) {
      signatory.setName(node.get("name").asText());
    }

    if (node.has("role")) {
      signatory.setRole(node.get("role").asText());
    }

    JsonNode signatureNode = node.get("signature");
    if (signatureNode != null) {
      Signature signature = mapper.convertValue(node.get("signature"), Signature.class);
      signatory.setSignature(signature);
    }
    else {
      parseOrganizationAndReference(node, signatory);
    }

    return signatory;
  }

  private void parseOrganizationAndReference(JsonNode node, Signatory signatory) {
    JsonNode organizationNode = node.get("organization");
    JsonNode externalReferenceNode = node.get("externalReference");

    if (organizationNode != null && externalReferenceNode != null) {
      OrganizationalEntity organization = mapper.convertValue(organizationNode, OrganizationalEntity.class);
      ExternalReference externalReference = mapper.convertValue(externalReferenceNode, ExternalReference.class);
      signatory.setExternalReferenceAndOrganization(externalReference, organization);
    }
  }
}
