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

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "title",
    "featuredImage",
    "socialImage",
    "description",
    "timestamp",
    "aliases",
    "tags",
    "resolves",
    "properties"
})
/*
 * @since 1.4
 * @version 1.0
 */
public class ReleaseNotes
{
  public ReleaseNotes() {}

  @JacksonXmlProperty(isAttribute = true)
  private String type;
  private String title;
  private String featuredImage;
  private String socialImage;
  private String description;
  private Date timestamp;
  private List<String> aliases;
  private List<String> tags;
  private List<Resolves> resolves;

  private List<Property> properties;

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getFeaturedImage() {
    return featuredImage;
  }

  public void setFeaturedImage(final String featuredImage) {
    this.featuredImage = featuredImage;
  }

  public String getSocialImage() {
    return socialImage;
  }

  public void setSocialImage(final String socialImage) {
    this.socialImage = socialImage;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(final Date timestamp) {
    this.timestamp = timestamp;
  }

  public List<String> getAliases() {
    return aliases;
  }

  public void setAliases(final List<String> aliases) {
    this.aliases = aliases;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(final List<String> tags) {
    this.tags = tags;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }

  public List<Resolves> getResolves() {
    return resolves;
  }

  public void setResolves(final List<Resolves> resolves) {
    this.resolves = resolves;
  }

  public static class Resolves {

    public enum Type {
      @JsonProperty("defect")
      DEFECT("defect"),
      @JsonProperty("enhancement")
      ENHANCEMENT("enhancement"),
      @JsonProperty("security")
      SECURITY("security");

      private final String name;

      public String getResolvesName() {
        return this.name;
      }

      Type(String name) {
        this.name = name;
      }

      public static Resolves.Type fromString(String text) {
        for (Resolves.Type t : Resolves.Type.values()) {
          if (t.name.equals(text)) {
            return t;

          }
        }
        return null;
      }
    }

    private Type type;

    private String id;
    private String name;
    private String description;
    private Source source;
    private List<String> references;

    public Type getType() {
      return type;
    }

    public void setType(final Type type) {
      this.type = type;
    }

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

    public List<String> getReferences() {
      return references;
    }

    public void setReferences(final List<String> references) {
      this.references = references;
    }
  }
}
