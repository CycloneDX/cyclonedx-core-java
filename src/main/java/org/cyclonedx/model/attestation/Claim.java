package org.cyclonedx.model.attestation;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.JsonOnly;
import org.cyclonedx.model.Signature;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "target",
    "predicate",
    "mitigationStrategies",
    "reasoning",
    "evidence",
    "counterEvidence",
    "externalReferences",
    "signature"
})
public class Claim extends ExtensibleElement
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;

  private String target;

  private String predicate;

  private List<String> mitigationStrategies;

  private String reasoning;

  private List<String> evidence;

  private List<String> counterEvidence;

  private List<ExternalReference> externalReferences;

  @JsonOnly
  private Signature signature;

  public String getBomRef() {
    return bomRef;
  }

  public void setBomRef(final String bomRef) {
    this.bomRef = bomRef;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(final String target) {
    this.target = target;
  }

  public String getPredicate() {
    return predicate;
  }

  public void setPredicate(final String predicate) {
    this.predicate = predicate;
  }

  @JacksonXmlElementWrapper(localName = "mitigationStrategies")
  @JacksonXmlProperty(localName = "mitigationStrategy")
  public List<String> getMitigationStrategies() {
    return mitigationStrategies;
  }

  public void setMitigationStrategies(final List<String> mitigationStrategies) {
    this.mitigationStrategies = mitigationStrategies;
  }

  public String getReasoning() {
    return reasoning;
  }

  public void setReasoning(final String reasoning) {
    this.reasoning = reasoning;
  }

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "evidence")
  @JsonProperty("evidence")
  public List<String> getEvidence() {
    return evidence;
  }

  public void setEvidence(final List<String> evidence) {
    this.evidence = evidence;
  }

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "counterEvidence")
  @JsonProperty("counterEvidence")
  public List<String> getCounterEvidence() {
    return counterEvidence;
  }

  public void setCounterEvidence(final List<String> counterEvidence) {
    this.counterEvidence = counterEvidence;
  }

  @JacksonXmlElementWrapper(localName = "externalReferences")
  @JacksonXmlProperty(localName = "reference")
  public List<ExternalReference> getExternalReferences() {
    return externalReferences;
  }

  public void setExternalReferences(final List<ExternalReference> externalReferences) {
    this.externalReferences = externalReferences;
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
    if (!(object instanceof Claim)) {
      return false;
    }
    Claim claim = (Claim) object;
    return Objects.equals(bomRef, claim.bomRef) && Objects.equals(target, claim.target) &&
        Objects.equals(predicate, claim.predicate) &&
        Objects.equals(mitigationStrategies, claim.mitigationStrategies) &&
        Objects.equals(reasoning, claim.reasoning) && Objects.equals(evidence, claim.evidence) &&
        Objects.equals(counterEvidence, claim.counterEvidence) &&
        Objects.equals(externalReferences, claim.externalReferences) &&
        Objects.equals(signature, claim.signature);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bomRef, target, predicate, mitigationStrategies, reasoning, evidence, counterEvidence,
        externalReferences, signature);
  }
}
