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
package org.cyclonedx.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Helper class for Jackson serializing/deserializing lists that have same localname, but different wrapper name.
 * Currently used by Ancestors, Descendants and Variants
 * Workaround for: <a href="https://github.com/FasterXML/jackson-dataformat-xml/issues/192">...</a>
 * @since 4.0.0
 */
public abstract class ComponentWrapper {
  protected List<Component> components;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "component")
  public List<Component> getComponents() {
    return components;
  }

  public void setComponents(final List<Component> components) {
    this.components = components;
  }

  public void addComponent(final Component component) {
    if (this.components == null) {
      this.components = new ArrayList<>();
    }
    this.components.add(component);
  }
}
