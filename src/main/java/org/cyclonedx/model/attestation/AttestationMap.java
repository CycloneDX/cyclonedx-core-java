package org.cyclonedx.model.attestation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "map")
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
  @JacksonXmlProperty(isAttribute = true, localName = "requirement")
  @JsonProperty("requirement")
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
}
