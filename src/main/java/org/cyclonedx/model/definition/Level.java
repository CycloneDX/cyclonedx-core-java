package org.cyclonedx.model.definition;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Level
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;

  private String identifier;

  private String title;

  private String description;

  private List<String> requirements;

  public String getBomRef() {
    return bomRef;
  }

  public void setBomRef(final String bomRef) {
    this.bomRef = bomRef;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(final String identifier) {
    this.identifier = identifier;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public List<String> getRequirements() {
    return requirements;
  }

  public void setRequirements(final List<String> requirements) {
    this.requirements = requirements;
  }
}
