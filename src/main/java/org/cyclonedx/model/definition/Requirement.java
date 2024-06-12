package org.cyclonedx.model.definition;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Property;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "identifier",
    "title",
    "text",
    "descriptions",
    "openCre",
    "parent",
    "properties",
    "externalReferences"
})
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

  @JacksonXmlElementWrapper(localName = "descriptions")
  @JacksonXmlProperty(localName = "description")
  public List<String> getDescriptions() {
    return descriptions;
  }

  public void setDescriptions(final List<String> descriptions) {
    this.descriptions = descriptions;
  }

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "openCre")
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

  @JacksonXmlElementWrapper(localName = "properties")
  @JacksonXmlProperty(localName = "property")
  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }

  @JacksonXmlElementWrapper(localName = "externalReferences")
  @JacksonXmlProperty(localName = "externalReference")
  public List<ExternalReference> getExternalReferences() {
    return externalReferences;
  }

  public void setExternalReferences(final List<ExternalReference> externalReferences) {
    this.externalReferences = externalReferences;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Requirement)) {
      return false;
    }
    Requirement that = (Requirement) object;
    return Objects.equals(bomRef, that.bomRef) && Objects.equals(identifier, that.identifier) &&
        Objects.equals(title, that.title) && Objects.equals(text, that.text) &&
        Objects.equals(descriptions, that.descriptions) && Objects.equals(openCre, that.openCre) &&
        Objects.equals(parent, that.parent) && Objects.equals(properties, that.properties) &&
        Objects.equals(externalReferences, that.externalReferences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bomRef, identifier, title, text, descriptions, openCre, parent, properties, externalReferences);
  }
}
