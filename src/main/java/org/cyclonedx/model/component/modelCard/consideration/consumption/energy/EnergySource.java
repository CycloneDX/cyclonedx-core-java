package org.cyclonedx.model.component.modelCard.consideration.consumption.energy;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EnergySource
{
  @JsonProperty("coal")
  COAL("coal"),
  @JsonProperty("oil")
  OIL("oil"),
  @JsonProperty("natural-gas")
  NATURAL_GAS("natural-gas"),
  @JsonProperty("nuclear")
  NUCLEAR("nuclear"),
  @JsonProperty("wind")
  WIND("wind"),
  @JsonProperty("solar")
  SOLAR("solar"),
  @JsonProperty("geothermal")
  GEOTHERMAL("geothermal"),
  @JsonProperty("hydropower")
  HYDROPOWER("hydropower"),
  @JsonProperty("biofuel")
  BIOFUEL("biofuel"),
  @JsonProperty("unknown")
  UNKNOWN("unknown"),
  @JsonProperty("other")
  OTHER("other");

  private final String name;

  EnergySource(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
