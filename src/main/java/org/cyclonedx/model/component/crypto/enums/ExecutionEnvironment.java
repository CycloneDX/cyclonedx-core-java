package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ExecutionEnvironment
{
  @JsonProperty("software-plain-ram")
  SOFTWARE_PLAIN_RAM("software-plain-ram"),
  @JsonProperty("software-encrypted-ram")
  SOFTWARE_ENCRYPTED_RAM("software-encrypted-ram"),
  @JsonProperty("software-tee")
  SOFTWARE_TEE("software-tee"),
  @JsonProperty("hardware")
  HARDWARE("hardware"),
  @JsonProperty("other")
  OTHER("other"),
  @JsonProperty("unknown")
  UNKNOWN("unknown");


  private final String name;

  ExecutionEnvironment(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
