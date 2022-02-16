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

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"diff", "resolves"})
public class Patch {

  public enum Type {
    @JsonProperty("backport")
    BACKPORT("backport"),
    @JsonProperty("unofficial")
    UNOFFICIAL("unofficial"),
    @JsonProperty("monkey")
    MONKEY("monkey"),
    @JsonProperty("cherry-pick")
    CHERRY_PICK("cherry-pick");

    private final String name;

    public String getTypeName() {
      return this.name;
    }

    Type(final String name) {
      this.name = name;
    }
  }

  @JacksonXmlProperty(isAttribute = true)
  private Type type;
  private Diff diff;
  private List<Issue> resolves;

  public Diff getDiff() {
    return diff;
  }

  public void setDiff(final Diff diff) {
    this.diff = diff;
  }

  @JacksonXmlElementWrapper(localName = "resolves")
  @JacksonXmlProperty(localName = "issue")
  public List<Issue> getResolves() {
    return resolves;
  }

  public void setResolves(final List<Issue> resolves) {
    this.resolves = resolves;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }
}
