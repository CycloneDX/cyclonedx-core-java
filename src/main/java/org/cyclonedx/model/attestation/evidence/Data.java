package org.cyclonedx.model.attestation.evidence;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.component.modelCard.data.Governance;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "name",
    "contents",
    "classification",
    "sensitiveData",
    "governance"
})
public class Data
{
  private String name;

  private Contents contents;

  private String classification;

  private List<String> sensitiveData;

  private Governance governance;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }


  public Contents getContents() {
    return contents;
  }

  public void setContents(final Contents contents) {
    this.contents = contents;
  }

 @JacksonXmlElementWrapper(useWrapping = false)
 @JacksonXmlProperty(localName = "sensitiveData")
  public List<String> getSensitiveData() {
    return sensitiveData;
  }

  public void setSensitiveData(final List<String> sensitiveData) {
    this.sensitiveData = sensitiveData;
  }

  public Governance getGovernance() {
    return governance;
  }

  public void setGovernance(final Governance governance) {
    this.governance = governance;
  }

  public String getClassification() {
    return classification;
  }

  public void setClassification(final String classification) {
    this.classification = classification;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Data)) {
      return false;
    }
    Data data = (Data) object;
    return Objects.equals(name, data.name) && Objects.equals(contents, data.contents) &&
        Objects.equals(classification, data.classification) &&
        Objects.equals(sensitiveData, data.sensitiveData) &&
        Objects.equals(governance, data.governance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, contents, classification, sensitiveData, governance);
  }
}
