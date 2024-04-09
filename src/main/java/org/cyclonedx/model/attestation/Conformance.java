package org.cyclonedx.model.attestation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "score",
    "rationale",
    "mitigationStrategies"
})
public class Conformance
{
  private Double score;

  private String rationale;

  private List<String> mitigationStrategies;

  public Double getScore() {
    return score;
  }

  public void setScore(final Double score) {
    this.score = score;
  }

  public String getRationale() {
    return rationale;
  }

  public void setRationale(final String rationale) {
    this.rationale = rationale;
  }
  @JacksonXmlElementWrapper(localName = "mitigationStrategies")
  @JacksonXmlProperty(localName = "mitigationStrategy")
  public List<String> getMitigationStrategies() {
    return mitigationStrategies;
  }

  public void setMitigationStrategies(final List<String> mitigationStrategies) {
    this.mitigationStrategies = mitigationStrategies;
  }
}
