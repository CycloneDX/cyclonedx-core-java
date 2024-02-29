package org.cyclonedx.model.attestation.evidence;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.cyclonedx.model.component.modelCard.data.Governance;


@JacksonXmlRootElement(localName = "data")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "name",
    "contents",
    "dataClassification",
    "sensitiveData",
    "governance"
})
public class Data
{
  private String name;

  private Contents contents;

  private DataClassification dataClassification;

  private List<String> sensitiveData;

  private Governance governance;

  public String getData() {
    return data;
  }

  public void setData(final String data) {
    this.data = data;
  }

  public Contents getContents() {
    return contents;
  }

  public void setContents(final Contents contents) {
    this.contents = contents;
  }

  public DataClassification getDataClassification() {
    return dataClassification;
  }

  public void setDataClassification(final DataClassification dataClassification) {
    this.dataClassification = dataClassification;
  }

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
}
