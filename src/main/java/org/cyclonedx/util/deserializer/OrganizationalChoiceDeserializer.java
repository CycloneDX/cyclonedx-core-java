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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.cyclonedx.model.OrganizationalChoice;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;

public class OrganizationalChoiceDeserializer
    extends JsonDeserializer<OrganizationalChoice>
{
  @Override
  public OrganizationalChoice deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException
  {
    JsonNode node = jp.getCodec().readTree(jp);

    OrganizationalChoice organizationalChoice = new OrganizationalChoice();

    if(node.has("individual")) {
      OrganizationalContact individual = jp.getCodec().treeToValue(node.get("individual"), OrganizationalContact.class);
      organizationalChoice.setIndividual(individual);
    }
    else if(node.has("organization")) {
      JsonNode organizationNode = node.get("organization");
      OrganizationalEntity organization = new OrganizationalEntity();
      organization.setName(organizationNode.get("name").asText());

      if (organizationNode.has("contact")) {
        JsonNode contactsNode = organizationNode.get("contact");
        if (contactsNode instanceof ArrayNode) {
          for (JsonNode contactNode : contactsNode) {
            OrganizationalContact contact = jp.getCodec().treeToValue(contactNode, OrganizationalContact.class);
            organization.addContact(contact);
          }
        } else if (contactsNode instanceof ObjectNode) {
          OrganizationalContact contact = jp.getCodec().treeToValue(contactsNode, OrganizationalContact.class);
          organization.addContact(contact);
        }
      }
      organizationalChoice.setOrganization(organization);
    }

    return organizationalChoice;
  }
}
