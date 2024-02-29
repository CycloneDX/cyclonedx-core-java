package org.cyclonedx.model.attestation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.cyclonedx.model.Signature;

@JacksonXmlRootElement(localName = "conformance")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "summary",
    "assessor",
    "mapList",
    "signature"
})
public class Attestation
{
  private String summary;

  @JacksonXmlProperty(isAttribute = true, localName = "assessor")
  @JsonProperty("assessor")
  private String assessor;

  private List<AttestationMap> mapList;

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

  public List<AttestationMap> getMapList() {
    return mapList;
  }

  public void setMapList(final List<AttestationMap> mapList) {
    this.mapList = mapList;
  }

  public Signature getSignature() {
    return signature;
  }

  public void setSignature(final Signature signature) {
    this.signature = signature;
  }
}
