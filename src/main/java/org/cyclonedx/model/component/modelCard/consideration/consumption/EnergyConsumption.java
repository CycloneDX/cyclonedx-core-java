package org.cyclonedx.model.component.modelCard.consideration.consumption;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.component.modelCard.consideration.consumption.co2.CO2Measure;
import org.cyclonedx.model.component.modelCard.consideration.consumption.energy.EnergyMeasure;
import org.cyclonedx.model.component.modelCard.consideration.consumption.energy.EnergyProvider;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EnergyConsumption
{
  private Activity activity;
  private List<EnergyProvider> energyProviders;
  private EnergyMeasure activityEnergyCost;
  private CO2Measure co2CostEquivalent;
  private CO2Measure co2CostOffset;
  private List<Property> properties;

  public Activity getActivity() {
    return activity;
  }

  public void setActivity(final Activity activity) {
    this.activity = activity;
  }

  @JacksonXmlElementWrapper(useWrapping = false)
  public List<EnergyProvider> getEnergyProviders() {
    return energyProviders;
  }

  public void setEnergyProviders(final List<EnergyProvider> energyProviders) {
    this.energyProviders = energyProviders;
  }

  public EnergyMeasure getActivityEnergyCost() {
    return activityEnergyCost;
  }

  public void setActivityEnergyCost(final EnergyMeasure activityEnergyCost) {
    this.activityEnergyCost = activityEnergyCost;
  }

  public CO2Measure getCo2CostEquivalent() {
    return co2CostEquivalent;
  }

  public void setCo2CostEquivalent(final CO2Measure co2CostEquivalent) {
    this.co2CostEquivalent = co2CostEquivalent;
  }

  public CO2Measure getCo2CostOffset() {
    return co2CostOffset;
  }

  public void setCo2CostOffset(final CO2Measure co2CostOffset) {
    this.co2CostOffset = co2CostOffset;
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
