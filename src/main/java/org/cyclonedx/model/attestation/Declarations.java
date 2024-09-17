package org.cyclonedx.model.attestation;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.JsonOnly;
import org.cyclonedx.model.Signature;
import org.cyclonedx.model.attestation.affirmation.Affirmation;
import org.cyclonedx.model.attestation.evidence.Evidence;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "assessors",
    "attestations",
    "claims",
    "evidence",
    "targets",
    "affirmation",
    "signature"
})
public class Declarations extends ExtensibleElement
{
  private List<Assessor> assessors;

  private List<Attestation> attestations;

  private List<Claim> claims;

  private List<Evidence> evidence;

  private Targets targets;

  private Affirmation affirmation;

  @JsonOnly
  private Signature signature;

  @JacksonXmlElementWrapper(localName = "assessors")
  @JacksonXmlProperty(localName = "assessor")
  public List<Assessor> getAssessors() {
    return assessors;
  }

  public void setAssessors(final List<Assessor> assessors) {
    this.assessors = assessors;
  }

  @JacksonXmlElementWrapper(localName = "attestations")
  @JacksonXmlProperty(localName = "attestation")
  public List<Attestation> getAttestations() {
    return attestations;
  }

  public void setAttestations(final List<Attestation> attestations) {
    this.attestations = attestations;
  }

  @JacksonXmlElementWrapper(localName = "claims")
  @JacksonXmlProperty(localName = "claim")
  public List<Claim> getClaims() {
    return claims;
  }

  public void setClaims(final List<Claim> claims) {
    this.claims = claims;
  }

  @JacksonXmlElementWrapper(localName = "evidence")
  @JacksonXmlProperty(localName = "evidence")
  public List<Evidence> getEvidence() {
    return evidence;
  }

  public void setEvidence(final List<Evidence> evidence) {
    this.evidence = evidence;
  }

  public Targets getTargets() {
    return targets;
  }

  public void setTargets(final Targets targets) {
    this.targets = targets;
  }

  public Affirmation getAffirmation() {
    return affirmation;
  }

  public void setAffirmation(final Affirmation affirmation) {
    this.affirmation = affirmation;
  }

  public Signature getSignature() {
    return signature;
  }

  public void setSignature(final Signature signature) {
    this.signature = signature;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Declarations)) {
      return false;
    }
    Declarations that = (Declarations) object;
    return Objects.equals(assessors, that.assessors) &&
        Objects.equals(attestations, that.attestations) && Objects.equals(claims, that.claims) &&
        Objects.equals(evidence, that.evidence) && Objects.equals(targets, that.targets) &&
        Objects.equals(affirmation, that.affirmation) && Objects.equals(signature, that.signature);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assessors, attestations, claims, evidence, targets, affirmation, signature);
  }
}
