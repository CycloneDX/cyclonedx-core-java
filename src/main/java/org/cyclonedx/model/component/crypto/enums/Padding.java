package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Padding
{
  @JsonProperty("pkcs5")
  PKCS5("pkcs5"),
  @JsonProperty("pkcs7")
  PKCS7("pkcs7"),
  @JsonProperty("pkcs1v15")
  PKCS1V15("pkcs1v15"),
  @JsonProperty("oaep")
  OAEP("oaep"),
  @JsonProperty("raw")
  RAW("raw"),
  @JsonProperty("other")
  OTHER("other"),
  @JsonProperty("unknown")
  UNKNOWN("unknown");

  private final String name;

  Padding(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
