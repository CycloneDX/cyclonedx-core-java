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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.Tool;
import org.cyclonedx.model.metadata.ToolInformation;

import java.io.IOException;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class ToolsChoice {

  @SuppressWarnings("deprecation")
  @JacksonXmlElementWrapper(useWrapping = false)
  public List<Tool> tool;

  @JacksonXmlElementWrapper(localName = "components")
  @JacksonXmlProperty(localName = "component")
  public List<Component> components;

  @JacksonXmlElementWrapper(localName = "services")
  @JacksonXmlProperty(localName = "service")
  public List<Service> services;

  @SuppressWarnings("deprecation")
  static ToolsChoice read(JsonParser parser, DeserializationContext ctxt) throws IOException {
    if (parser.currentToken() == JsonToken.START_ARRAY) {
      final ToolsChoice tools = new ToolsChoice();
      tools.tool = DeserializerUtils.readList(parser, ctxt, Tool.class);
      return tools;
    }
    return ctxt.readValue(parser, ToolsChoice.class);
  }

  @SuppressWarnings("deprecation")
  List<Tool> legacyTools() {
    return isToolInformation() ? null : tool;
  }

  ToolInformation toolInformation() {
    if (!isToolInformation()) {
      return null;
    }
    final ToolInformation toolInformation = new ToolInformation();
    toolInformation.setComponents(components);
    toolInformation.setServices(services);
    return toolInformation;
  }

  private boolean isToolInformation() {
    return components != null || services != null;
  }
}
