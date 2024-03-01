package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ImplementationPlatform
{
  @JsonProperty("generic")
  GENERIC("generic"),
  @JsonProperty("x86_32")
  X86_32("x86_32"),
  @JsonProperty("x86_64")
  X86_64("x86_64"),
  @JsonProperty("armv7-a")
  ARMV7_A("armv7-a"),
  @JsonProperty("armv7-m")
  ARMV7_M("armv7-m"),
  @JsonProperty("armv8-a")
  ARMV8_A("armv8-a"),
  @JsonProperty("armv8-m")
  ARMV8_M("armv8-m"),
  @JsonProperty("armv9-a")
  ARMV9_A("armv9-a"),
  @JsonProperty("armv9-m")
  ARMV9_M("armv9-m"),
  @JsonProperty("s390x")
  S390X("s390x"),
  @JsonProperty("ppc64")
  PPC64("ppc64"),
  @JsonProperty("ppc64le")
  PPC64LE("ppc64le"),
  @JsonProperty("other")
  OTHER("other"),
  @JsonProperty("unknown")
  UNKNOWN("unknown");

  private final String name;

  ImplementationPlatform(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
