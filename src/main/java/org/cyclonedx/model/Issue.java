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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"id", "name", "description", "source", "reference"})
public class Issue {

  public enum Type {
    @JsonProperty("enhancement")
    ENHANCEMENT("enhancement"),
    @JsonProperty("security")
    SECURITY("security"),
    @JsonProperty("defect")
    DEFECT("defect");

    private final String name;

    public String getTypeName() {
      return this.name;
    }

    Type(String name) {
      this.name = name;
    }
  }

  private String id;
  private String name;
  private String description;
  private Source source;
  private List<String> references;
  @JacksonXmlProperty(isAttribute = true)
  private Type type;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public Source getSource() {
    return source;
  }

  public void setSource(final Source source) {
    this.source = source;
  }

  @JacksonXmlElementWrapper(localName = "references")
  @JacksonXmlProperty(localName = "url")
  public List<String> getReferences() {
    return references;
  }

  public void addReference(String reference) {
    if (references == null) {
      references = new ArrayList<>();
    }
    references.add(reference);
  }

  public void addReference(URI reference) {
    if (references == null) {
      references = new ArrayList<>();
    }
    references.add(reference.toString());
  }

  public void setReferences(List<String> references) {
    this.references = references;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }
}
