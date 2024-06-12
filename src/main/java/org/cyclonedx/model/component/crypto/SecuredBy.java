package org.cyclonedx.model.component.crypto;

import java.util.Objects;

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

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof SecuredBy)) {
      return false;
    }
    SecuredBy securedBy = (SecuredBy) object;
    return Objects.equals(mechanism, securedBy.mechanism) &&
        Objects.equals(algorithmRef, securedBy.algorithmRef);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mechanism, algorithmRef);
  }
}
