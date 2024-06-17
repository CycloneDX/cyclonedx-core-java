package org.cyclonedx.model.component.modelCard.consideration;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.component.modelCard.consideration.consumption.EnergyConsumption;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EnvironmentalConsideration
{
  private List<EnergyConsumption> energyConsumptions;

  private List<Property> properties;

  @JacksonXmlElementWrapper(localName = "energyConsumptions")
  @JacksonXmlProperty(localName = "energyConsumption")
  public List<EnergyConsumption> getEnergyConsumptions() {
    return energyConsumptions;
  }

  public void setEnergyConsumptions(final List<EnergyConsumption> energyConsumptions) {
    this.energyConsumptions = energyConsumptions;
  }

  @JacksonXmlElementWrapper(localName = "properties")
  @JacksonXmlProperty(localName = "property")
  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof EnvironmentalConsideration)) {
      return false;
    }
    EnvironmentalConsideration that = (EnvironmentalConsideration) object;
    return Objects.equals(energyConsumptions, that.energyConsumptions) &&
        Objects.equals(properties, that.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(energyConsumptions, properties);
  }
}
