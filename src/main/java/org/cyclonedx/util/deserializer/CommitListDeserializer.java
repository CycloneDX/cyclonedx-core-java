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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.cyclonedx.model.Commit;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommitListDeserializer
    extends JsonDeserializer<List<Commit>>
{
  @Override
  public List<Commit> deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException
  {
    if (parser instanceof FromXmlParser) {
      return Arrays.asList(parser.readValueAs(Commit[].class));
    }

    JsonToken token = parser.currentToken();
    if (token == JsonToken.START_ARRAY) {
      return Arrays.asList(parser.readValueAs(Commit[].class));
    } else if (token == JsonToken.START_OBJECT) {
      // XML-via-JsonNode path (e.g. ToolInformationDeserializer): wrapper element
      // <commits><commit>...</commit></commits> becomes {"commit": {...}} or {"commit": [{...},...]}
      ObjectNode node = parser.readValueAs(ObjectNode.class);
      if (node.has("commit")) {
        JsonNode commitNode = node.get("commit");
        try (JsonParser cp = commitNode.traverse(parser.getCodec())) {
          cp.nextToken();
          if (commitNode.isArray()) {
            return Arrays.asList(cp.readValueAs(Commit[].class));
          } else {
            return Collections.singletonList(cp.readValueAs(Commit.class));
          }
        }
      }
    }
    return Collections.emptyList();
  }
}