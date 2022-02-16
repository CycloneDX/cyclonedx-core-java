package org.cyclonedx.util.mixin;

import com.fasterxml.jackson.annotation.JsonValue;

public class MixInBomReference
{
  @JsonValue
  private String ref;

  public String getRef() {
    return ref;
  }

  public void setRef(final String ref) {
    this.ref = ref;
  }
}
