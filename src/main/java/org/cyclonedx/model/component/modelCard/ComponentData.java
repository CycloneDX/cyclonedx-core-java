package org.cyclonedx.model.component.modelCard;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.component.modelCard.data.Content;
import org.cyclonedx.model.component.modelCard.data.Governance;
import org.cyclonedx.model.component.modelCard.data.Graphics;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ComponentData extends ExtensibleElement
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;

  private ComponentDataType type;

  private String name;

  private Content contents;

  private String classification;

  private List<String> sensitiveData;

  private Graphics graphics;

  private String description;

  private Governance governance;


  public enum ComponentDataType{
    @JsonProperty("source-code")
    SOURCE_CODE("source-code"),
    @JsonProperty("configuration")
    CONFIGURATION("configuration"),
    @JsonProperty("dataset")
    DATASET("dataset"),
    @JsonProperty("other")
    OTHER("other");

    private final String name;

    public String getTypeName() {
      return this.name;
    }

    ComponentDataType(String name) {
      this.name = name;
    }
  }


  public String getBomRef() {
    return bomRef;
  }

  public void setBomRef(final String bomRef) {
    this.bomRef = bomRef;
  }

  public ComponentDataType getType() {
    return type;
  }

  public void setType(final ComponentDataType type) {
    this.type = type;
  }

  public Content getContents() {
    return contents;
  }

  public void setContents(final Content contents) {
    this.contents = contents;
  }

  public List<String> getSensitiveData() {
    return sensitiveData;
  }

  public void setSensitiveData(final List<String> sensitiveData) {
    this.sensitiveData = sensitiveData;
  }

  public Graphics getGraphics() {
    return graphics;
  }

  public void setGraphics(final Graphics graphics) {
    this.graphics = graphics;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public void setClassification(final String classification) {
    this.classification = classification;
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

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }
}
