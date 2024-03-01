package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MemoryType
{
  @JsonProperty("rom")
  ROM("rom"),
  @JsonProperty("flash")
  FLASH("flash"),
  @JsonProperty("eeprom")
  EEPROM("eeprom"),
  @JsonProperty("ram")
  RAM("ram"),
  @JsonProperty("other")
  OTHER("other"),
  @JsonProperty("unknown")
  UNKNOWN("unknown");

  private final String name;

  MemoryType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
