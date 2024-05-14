package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CryptoFunction
{
  @JsonProperty("generate")
  GENERATE("generate"),
  @JsonProperty("keygen")
  KEYGEN("keygen"),
  @JsonProperty("encrypt")
  ENCRYPT("encrypt"),
  @JsonProperty("decrypt")
  DECRYPT("decrypt"),
  @JsonProperty("digest")
  DIGEST("digest"),
  @JsonProperty("tag")
  TAG("tag"),
  @JsonProperty("keyderive")
  KEYDERIVE("keyderive"),
  @JsonProperty("sign")
  SIGN("sign"),
  @JsonProperty("verify")
  VERIFY("verify"),
  @JsonProperty("encapsulate")
  ENCAPSULATE("encapsulate"),
  @JsonProperty("decapsulate")
  DECAPSULATE("decapsulate"),
  @JsonProperty("other")
  OTHER("other"),
  @JsonProperty("unknown")
  UNKNOWN("unknown");

  private final String name;

  CryptoFunction(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
