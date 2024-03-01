package org.cyclonedx.model.definition;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Signature;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "name",
    "version",
    "description",
    "owner",
    "requirements",
    "levels",
    "externalReferences",
    "signature"
})
public class Standard
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;

  private String name;

  private String version;

  private String description;

  private String owner;

  private List<Requirement> requirements;

  private List<Level> levels;

  private List<ExternalReference> externalReferences;

  private Signature signature;

  public String getBomRef() {
    return bomRef;
  }

  public void setBomRef(final String bomRef) {
    this.bomRef = bomRef;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(final String version) {
    this.version = version;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(final String owner) {
    this.owner = owner;
  }

  @JacksonXmlElementWrapper(localName = "requirements")
  @JacksonXmlProperty(localName = "requirement")
  public List<Requirement> getRequirements() {
    return requirements;
  }

  public void setRequirements(final List<Requirement> requirements) {
    this.requirements = requirements;
  }

  public List<Level> getLevels() {
    return levels;
  }

  public void setLevels(final List<Level> levels) {
    this.levels = levels;
  }

  public List<ExternalReference> getExternalReferences() {
    return externalReferences;
  }

  public void setExternalReferences(final List<ExternalReference> externalReferences) {
    this.externalReferences = externalReferences;
  }

  public Signature getSignature() {
    return signature;
  }

  public void setSignature(final Signature signature) {
    this.signature = signature;
  }
}
