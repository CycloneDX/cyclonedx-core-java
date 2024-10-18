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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.cyclonedx.model.Tool;
import org.cyclonedx.model.metadata.ToolInformation;
import org.cyclonedx.util.deserializer.ToolInformationDeserializer;
import org.cyclonedx.util.deserializer.ToolsDeserializer;

import java.io.IOException;
import java.util.List;

public class ToolsJsonParser {
    private final ToolInformationDeserializer toolInformationDeserializer = new ToolInformationDeserializer();
    private final ToolsDeserializer toolsDeserializer = new ToolsDeserializer();

    private ToolInformation toolInformation;
    private List<Tool> tools;

    public ToolsJsonParser(JsonNode node, JsonParser parser, DeserializationContext context) throws IOException {
      parse(node, parser, context);
    }

    private void parse(JsonNode node, JsonParser parser, DeserializationContext context) throws IOException {
      if (node.has("tools")) {
        JsonNode toolsNode = node.get("tools");
        JsonParser toolsParser = toolsNode.traverse(parser.getCodec());
        toolsParser.nextToken();
        if (toolsNode.has("components") || toolsNode.has("services")) {
          toolInformation = toolInformationDeserializer.deserialize(toolsParser, context);
        } else {
          tools = toolsDeserializer.deserialize(toolsParser, context);
        }
      }
    }

    public List<Tool> getTools() {
      return tools;
    }

    public ToolInformation getToolInformation() {
      return toolInformation;
    }
}
