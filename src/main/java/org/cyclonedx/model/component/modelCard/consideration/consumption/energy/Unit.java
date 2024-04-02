package org.cyclonedx.model.component.modelCard.consideration.consumption.energy;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Unit
{
  @JsonProperty("kWh")
  KWH("kWh");

  private final String name;

  Unit(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
