package org.cyclonedx.model.attestation.evidence;

import java.util.List;

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
}
