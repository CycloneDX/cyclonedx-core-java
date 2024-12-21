package org.cyclonedx.model.license;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.cyclonedx.model.ExternalReference.Type;

public enum Acknowledgement
{
  @JsonProperty("declared")
  DECLARED("declared"),
  @JsonProperty("concluded")
  CONCLUDED("concluded");

  private final String name;

  public String getValue() {
    return this.name;
  }

  Acknowledgement(String name) {
    this.name = name;
  }

  public static Acknowledgement fromString(String text) {
    for (Acknowledgement t : Acknowledgement.values()) {
      if (t.name.equals(text)) {
        return t;
      }
    }
    return null;
  }
}
