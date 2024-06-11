package org.cyclonedx.model.definition;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "identifier",
    "title",
    "text",
    "description",
    "requirements"
})
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

  @JacksonXmlElementWrapper(localName = "requirements")
  @JacksonXmlProperty(localName = "requirement")
  public List<String> getRequirements() {
    return requirements;
  }

  public void setRequirements(final List<String> requirements) {
    this.requirements = requirements;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Level)) {
      return false;
    }
    Level level = (Level) object;
    return Objects.equals(bomRef, level.bomRef) && Objects.equals(identifier, level.identifier) &&
        Objects.equals(title, level.title) && Objects.equals(description, level.description) &&
        Objects.equals(requirements, level.requirements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bomRef, identifier, title, description, requirements);
  }
}
