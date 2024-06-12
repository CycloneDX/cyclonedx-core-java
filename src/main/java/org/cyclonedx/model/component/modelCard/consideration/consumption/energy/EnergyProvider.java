package org.cyclonedx.model.component.modelCard.consideration.consumption.energy;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.util.deserializer.ExternalReferencesDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EnergyProvider
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;
  private String description;
  private OrganizationalEntity organization;
  private EnergySource energySource;
  private EnergyMeasure energyProvided;
  private List<ExternalReference> externalReferences;

  public String getBomRef() {
    return bomRef;
  }

  public void setBomRef(final String bomRef) {
    this.bomRef = bomRef;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public OrganizationalEntity getOrganization() {
    return organization;
  }

  public void setOrganization(final OrganizationalEntity organization) {
    this.organization = organization;
  }

  public EnergySource getEnergySource() {
    return energySource;
  }

  public void setEnergySource(final EnergySource energySource) {
    this.energySource = energySource;
  }

  public EnergyMeasure getEnergyProvided() {
    return energyProvided;
  }

  public void setEnergyProvided(final EnergyMeasure energyProvided) {
    this.energyProvided = energyProvided;
  }

  @JacksonXmlElementWrapper(localName = "externalReferences")
  @JacksonXmlProperty(localName = "reference")
  @JsonDeserialize(using = ExternalReferencesDeserializer.class)
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
    if (!(object instanceof EnergyProvider)) {
      return false;
    }
    EnergyProvider that = (EnergyProvider) object;
    return Objects.equals(bomRef, that.bomRef) && Objects.equals(description, that.description) &&
        Objects.equals(organization, that.organization) && energySource == that.energySource &&
        Objects.equals(energyProvided, that.energyProvided) &&
        Objects.equals(externalReferences, that.externalReferences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bomRef, description, organization, energySource, energyProvided, externalReferences);
  }
}
