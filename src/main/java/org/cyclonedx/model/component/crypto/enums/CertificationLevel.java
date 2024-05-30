package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum CertificationLevel
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
  FIPS140_3_L4("fips140-3-l4"),
  @JsonProperty("cc-eal1")
  CC_EAL1("cc-eal1"),
  @JsonProperty("cc-eal1+")
  CC_EAL1_PLUS("cc-eal1+"),
  @JsonProperty("cc-eal2")
  CC_EAL2("cc-eal2"),
  @JsonProperty("cc-eal2+")
  CC_EAL2_PLUS("cc-eal2+"),
  @JsonProperty("cc-eal3")
  CC_EAL3("cc-eal3"),
  @JsonProperty("cc-eal3+")
  CC_EAL3_PLUS("cc-eal3+"),
  @JsonProperty("cc-eal4")
  CC_EAL4("cc-eal4"),
  @JsonProperty("cc-eal4+")
  CC_EAL4_PLUS("cc-eal4+"),
  @JsonProperty("cc-eal5")
  CC_EAL5("cc-eal5"),
  @JsonProperty("cc-eal5+")
  CC_EAL5_PLUS("cc-eal5+"),
  @JsonProperty("cc-eal6")
  CC_EAL6("cc-eal6"),
  @JsonProperty("cc-eal6+")
  CC_EAL6_PLUS("cc-eal6+"),
  @JsonProperty("cc-eal7")
  CC_EAL7("cc-eal7"),
  @JsonProperty("cc-eal7+")
  CC_EAL7_PLUS("cc-eal7+"),
  @JsonProperty("other")
  OTHER("other"),
  @JsonProperty("unknown")
  UNKNOWN("unknown");

  private final String name;

  CertificationLevel(String name) {
    this.name = name;
  }

  @JsonCreator
  public static CertificationLevel fromString(String value) {
    for (CertificationLevel level : CertificationLevel.values()) {
      if (level.name.equalsIgnoreCase(value)) {
        return level;
      }
    }
    throw new IllegalArgumentException("Invalid level: " + value);
  }

  public String getName() {
    return name;
  }
}
