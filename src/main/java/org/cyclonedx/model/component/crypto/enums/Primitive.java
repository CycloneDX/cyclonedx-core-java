package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Primitive
{
  @JsonProperty("drbg")
  DRBG("drbg"),
  @JsonProperty("mac")
  MAC("mac"),
  @JsonProperty("block-cipher")
  BLOCK_CIPHER("block-cipher"),
  @JsonProperty("stream-cipher")
  STREAM_CIPHER("stream-cipher"),
  @JsonProperty("signature")
  SIGNATURE("signature"),
  @JsonProperty("hash")
  HASH("hash"),
  @JsonProperty("pke")
  PKE("pke"),
  @JsonProperty("xof")
  XOF("xof"),
  @JsonProperty("kdf")
  KDF("kdf"),
  @JsonProperty("key-agree")
  KEY_AGREE("key-agree"),
  @JsonProperty("kem")
  KEM("kem"),
  @JsonProperty("ae")
  AE("ae"),
  @JsonProperty("combiner")
  COMBINER("combiner"),
  @JsonProperty("other")
  OTHER("other"),
  @JsonProperty("unknown")
  UNKNOWN("unknown");

  private final String name;

  Primitive(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
