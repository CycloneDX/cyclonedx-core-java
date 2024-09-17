package org.cyclonedx.model.definition;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.JsonOnly;
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
public class Standard extends ExtensibleElement
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

  @JsonOnly
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

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Standard)) {
      return false;
    }
    Standard standard = (Standard) object;
    return Objects.equals(bomRef, standard.bomRef) && Objects.equals(name, standard.name) &&
        Objects.equals(version, standard.version) &&
        Objects.equals(description, standard.description) && Objects.equals(owner, standard.owner) &&
        Objects.equals(requirements, standard.requirements) &&
        Objects.equals(levels, standard.levels) &&
        Objects.equals(externalReferences, standard.externalReferences) &&
        Objects.equals(signature, standard.signature);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bomRef, name, version, description, owner, requirements, levels, externalReferences, signature);
  }
}
