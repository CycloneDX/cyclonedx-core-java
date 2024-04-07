package org.cyclonedx.model.component.modelCard.consideration;

import java.util.List;

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
}
