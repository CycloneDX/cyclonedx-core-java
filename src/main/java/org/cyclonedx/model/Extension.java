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
package org.cyclonedx.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.cyclonedx.util.ExtensionSerializer;

@JsonSerialize(using = ExtensionSerializer.class)
public class Extension
{
  public enum ExtensionType {
    VULNERABILITIES("vulnerabilities");

    private final String type;

    public String getTypeName() {
      return this.type;
    }

    ExtensionType(String type) {
      this.type = type;
    }
  }

  private ExtensionType extensionType;
  private String namespaceURI;
  private String prefix;

  public ExtensionType getExtensionType() {
    return extensionType;
  }

  public void setExtensionType(final ExtensionType extensionType) {
    this.extensionType = extensionType;
  }

  public void setNamespaceURI(final String namespaceURI) {
    this.namespaceURI = namespaceURI;
  }

  public void setPrefix(final String prefix) {
    this.prefix = prefix;
  }

  private List<ExtensibleType> extensions;

  public Extension() {
  }

  public Extension(final ExtensionType type, final List<ExtensibleType> extensions) {
    this.extensionType = type;
    this.extensions = extensions;
  }

  public List<ExtensibleType> getExtensions() {
    return extensions;
  }

  public void setExtensions(final List<ExtensibleType> extensions) {
    this.extensions = extensions;
  }

  public void addExtension(ExtensibleType extensibleType) {
    if (this.extensions == null) {
      this.extensions = new ArrayList<>();
    }
    this.extensions.add(extensibleType);
  }

  public String getPrefix() {
    return prefix;
  }

  public String getNamespaceURI() {
    return namespaceURI;
  }
}
