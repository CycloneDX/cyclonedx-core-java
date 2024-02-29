package org.cyclonedx.model.definition;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Property;

public class Requirement
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;
  private String identifier;

  private String title;

  private String text;

  private List<String> descriptions;

  private List<String> openCre;

  private String parent;

  private List<Property> properties;

  private List<ExternalReference> externalReferences;

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

  public String getText() {
    return text;
  }

  public void setText(final String text) {
    this.text = text;
  }

  public List<String> getDescriptions() {
    return descriptions;
  }

  public void setDescriptions(final List<String> descriptions) {
    this.descriptions = descriptions;
  }

  public List<String> getOpenCre() {
    return openCre;
  }

  public void setOpenCre(final List<String> openCre) {
    this.openCre = openCre;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(final String parent) {
    this.parent = parent;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }

  public List<ExternalReference> getExternalReferences() {
    return externalReferences;
  }

  public void setExternalReferences(final List<ExternalReference> externalReferences) {
    this.externalReferences = externalReferences;
  }
}
