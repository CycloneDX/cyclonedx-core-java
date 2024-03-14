package org.cyclonedx.model.component.crypto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"", "mechanism", "algorithmRef"})
public class SecuredBy
{
  private String mechanism;

  private String algorithmRef;

  public String getMechanism() {
    return mechanism;
  }

  public void setMechanism(final String mechanism) {
    this.mechanism = mechanism;
  }

  public String getAlgorithmRef() {
    return algorithmRef;
  }

  public void setAlgorithmRef(final String algorithmRef) {
    this.algorithmRef = algorithmRef;
  }
}
