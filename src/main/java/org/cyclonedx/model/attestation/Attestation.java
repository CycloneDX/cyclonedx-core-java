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
import org.cyclonedx.model.JsonOnly;
import org.cyclonedx.model.Signature;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "summary",
    "assessor",
    "map",
    "signature"
})
public class Attestation extends ExtensibleElement
{
  private String summary;

  private String assessor;

  private List<AttestationMap> map;

  @JsonProperty("signature")
  @JsonOnly
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

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Attestation)) {
      return false;
    }
    Attestation that = (Attestation) object;
    return Objects.equals(summary, that.summary) && Objects.equals(assessor, that.assessor) &&
        Objects.equals(map, that.map) && Objects.equals(signature, that.signature);
  }

  @Override
  public int hashCode() {
    return Objects.hash(summary, assessor, map, signature);
  }
}
