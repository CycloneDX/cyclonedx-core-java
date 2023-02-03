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
package org.cyclonedx.util;

import java.io.DataInput;
import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.OrganizationalInstance;

public class OrganizationalInstanceDeserializer extends JsonDeserializer<OrganizationalInstance>
{
  @Override
  public OrganizationalInstance deserialize(
      final JsonParser jsonParser,
      final DeserializationContext deserializationContext)
      throws IOException, JacksonException
  {
    ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
    ObjectNode root = (ObjectNode) mapper.readTree(jsonParser);

    Class<? extends OrganizationalInstance> instanceClass;

    // TODO: Method that determines which entity it is
    if (true) {
      instanceClass = OrganizationalEntity.class;
    } else {
      instanceClass = OrganizationalContact.class;
    }
    if (instanceClass == null) {
      return null;
    }

    return mapper.readValue((DataInput) root, instanceClass);
  }
}
