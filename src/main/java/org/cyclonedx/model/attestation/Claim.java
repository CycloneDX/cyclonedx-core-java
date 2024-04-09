package org.cyclonedx.model.attestation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExternalReference;
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
public class Claim
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
  @JacksonXmlProperty(localName = "externalReference")
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
}
