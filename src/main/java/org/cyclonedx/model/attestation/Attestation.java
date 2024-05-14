package org.cyclonedx.model.attestation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.Signature;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "summary",
    "assessor",
    "map",
    "signature"
})
public class Attestation
{
  private String summary;

  private String assessor;

  private List<AttestationMap> map;

  private Signature signature;

  public String getSummary() {
    return summary;
  }

  public void setSummary(final String summary) {
    this.summary = summary;
  }

  public String getAssessor() {
    return assessor;
  }

  public void setAssessor(final String assessor) {
    this.assessor = assessor;
  }

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "map")
  public List<AttestationMap> getMap() {
    return map;
  }

  public void setMap(final List<AttestationMap> map) {
    this.map = map;
  }

  public Signature getSignature() {
    return signature;
  }

  public void setSignature(final Signature signature) {
    this.signature = signature;
  }
}
