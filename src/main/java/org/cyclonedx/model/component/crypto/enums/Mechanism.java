package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Mechanism
{
  @JsonProperty("HSM")
  HSM("HSM"),
  @JsonProperty("TPM")
  TPM("TPM"),
  @JsonProperty("SGX")
  SGX("SGX"),
  @JsonProperty("Software")
  SOFTWARE("Software"),
  @JsonProperty("None")
  NONE("None");

  private final String name;

  Mechanism(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
