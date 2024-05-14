package org.cyclonedx.model.component.modelCard.consideration.consumption.co2;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Unit
{
  @JsonProperty("tCO2eq")
  TCO2EQ("tCO2eq");

  private final String name;

  Unit(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
