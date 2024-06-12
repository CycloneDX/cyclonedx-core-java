package org.cyclonedx.model.attestation;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "requirement",
    "claims",
    "counterClaims",
    "conformance",
    "confidence"
})
public class AttestationMap
{
  private String requirement;

  private List<String> claims;

  private List<String> counterClaims;

  private Conformance conformance;

  private Confidence confidence;

  public String getRequirement() {
    return requirement;
  }

  public void setRequirement(final String requirement) {
    this.requirement = requirement;
  }

  @JacksonXmlElementWrapper(localName = "claims")
  @JacksonXmlProperty(localName = "claim")
  public List<String> getClaims() {
    return claims;
  }

  public void setClaims(final List<String> claims) {
    this.claims = claims;
  }

  @JacksonXmlElementWrapper(localName = "counterClaims")
  @JacksonXmlProperty(localName = "counterClaim")
  public List<String> getCounterClaims() {
    return counterClaims;
  }

  public void setCounterClaims(final List<String> counterClaims) {
    this.counterClaims = counterClaims;
  }

  public Conformance getConformance() {
    return conformance;
  }

  public void setConformance(final Conformance conformance) {
    this.conformance = conformance;
  }

  public Confidence getConfidence() {
    return confidence;
  }

  public void setConfidence(final Confidence confidence) {
    this.confidence = confidence;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof AttestationMap)) {
      return false;
    }
    AttestationMap that = (AttestationMap) object;
    return Objects.equals(requirement, that.requirement) && Objects.equals(claims, that.claims) &&
        Objects.equals(counterClaims, that.counterClaims) &&
        Objects.equals(conformance, that.conformance) && Objects.equals(confidence, that.confidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requirement, claims, counterClaims, conformance, confidence);
  }
}
