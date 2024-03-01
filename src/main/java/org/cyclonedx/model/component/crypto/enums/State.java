package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum State
{
  @JsonProperty("pre-activation")
PRE_ACTIVATION("pre-activation", "Key state: Pre-activation"),
  @JsonProperty("active")
  ACTIVE("active", "Key state: Active"),
  @JsonProperty("suspended")
  SUSPENDED("suspended", "Key state: Suspended"),
  @JsonProperty("deactivated")
  DEACTIVATED("deactivated", "Key state: Deactivated"),
  @JsonProperty("compromised")
  COMPROMISED("compromised", "Key state: Compromised"),
  @JsonProperty("destroyed")
  DESTROYED("destroyed", "Key state: Destroyed");

  private final String name;
  private final String description;

  State(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
