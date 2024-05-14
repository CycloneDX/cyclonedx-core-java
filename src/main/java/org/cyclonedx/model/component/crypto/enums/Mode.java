package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Mode
{
  @JsonProperty("cbc")
  CBC("cbc"),
  @JsonProperty("ecb")
  ECB("ecb"),
  @JsonProperty("ccm")
  CCM("ccm"),
  @JsonProperty("gcm")
  GCM("gcm"),
  @JsonProperty("cfb")
  CFB("cfb"),
  @JsonProperty("ofb")
  OFB("ofb"),
  @JsonProperty("ctr")
  CTR("ctr"),
  @JsonProperty("other")
  OTHER("other"),
  @JsonProperty("unknown")
  UNKNOWN("unknown");

  private final String name;

  Mode(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
