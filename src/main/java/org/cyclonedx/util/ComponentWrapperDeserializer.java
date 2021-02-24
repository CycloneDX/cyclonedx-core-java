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
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.cyclonedx.util;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.cyclonedx.model.Ancestors;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.ComponentWrapper;
import org.cyclonedx.model.Descendants;
import org.cyclonedx.model.Variants;

public class ComponentWrapperDeserializer extends JsonDeserializer<ComponentWrapper>
{
  @Override
  public ComponentWrapper deserialize(
      final JsonParser parser, final DeserializationContext context)
      throws IOException, JsonProcessingException
  {
    final String location = parser.getCurrentName();
    if (parser instanceof FromXmlParser) {
      switch (location) {
        case "ancestors":
          return parser.readValueAs(Ancestors.class);
        case "descendants":
          return parser.readValueAs(Descendants.class);
        case "variants":
          return parser.readValueAs(Variants.class);
        default:
          return null;
      }
    }

    ComponentWrapper wrapper = null;

    switch (location) {
      case "ancestors":
        wrapper = new Ancestors();
        break;
      case "descendants":
        wrapper = new Descendants();
        break;
      case "variants":
        wrapper = new Variants();
        break;
      default:
        return null;
    }

    Component[] components = parser.readValueAs(Component[].class);
    if (null != wrapper) {
      wrapper.setComponents(Arrays.asList(components));
    }
    
    return wrapper;
  }
}
