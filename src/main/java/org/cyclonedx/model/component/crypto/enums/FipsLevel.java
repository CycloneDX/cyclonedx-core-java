package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FipsLevel
{
  @JsonProperty("none")
  NONE("none"),
  @JsonProperty("fips140-1-l1")
  FIPS140_1_L1("fips140-1-l1"),
  @JsonProperty("fips140-1-l2")
  FIPS140_1_L2("fips140-1-l2"),
  @JsonProperty("fips140-1-l3")
  FIPS140_1_L3("fips140-1-l3"),
  @JsonProperty("fips140-1-l4")
  FIPS140_1_L4("fips140-1-l4"),
  @JsonProperty("fips140-2-l1")
  FIPS140_2_L1("fips140-2-l1"),
  @JsonProperty("fips140-2-l2")
  FIPS140_2_L2("fips140-2-l2"),
  @JsonProperty("fips140-2-l3")
  FIPS140_2_L3("fips140-2-l3"),
  @JsonProperty("fips140-2-l4")
  FIPS140_2_L4("fips140-2-l4"),
  @JsonProperty("fips140-3-l1")
  FIPS140_3_L1("fips140-3-l1"),
  @JsonProperty("fips140-3-l2")
  FIPS140_3_L2("fips140-3-l2"),
  @JsonProperty("fips140-3-l3")
  FIPS140_3_L3("fips140-3-l3"),
  @JsonProperty("fips140-3-l4")
  FIPS140_3_L4("fips140-3-l4");

  private final String name;

  FipsLevel(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
