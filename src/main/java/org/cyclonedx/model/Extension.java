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

import java.util.List;

import org.cyclonedx.model.vulnerability.Vulnerability1_0;

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

  private String namespaceUri;
  private String prefix;

  private List<ExtensibleType> extensions;

  public Extension(final ExtensionType extensionType, final List<ExtensibleType> extensions) {
    if(extensionType == ExtensionType.VULNERABILITIES){
      this.namespaceUri = Vulnerability1_0.NAMESPACE_URI;
      this.prefix = Vulnerability1_0.PREFIX;
    }

    this.extensions = extensions;
  }

  public List<ExtensibleType> getExtensions() {
    return extensions;
  }

  public void setExtensions(final List<ExtensibleType> extensions) {
    this.extensions = extensions;
  }

  public String getNamespaceUri() {
    return namespaceUri;
  }

  public String getPrefix() {
    return prefix;
  }

}
