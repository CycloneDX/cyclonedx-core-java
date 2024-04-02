package org.cyclonedx.model.component.modelCard.consideration.consumption.co2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CO2Measure
{
  private double value;
  private Unit unit;

  public double getValue() {
    return value;
  }

  public void setValue(final double value) {
    this.value = value;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(final Unit unit) {
    this.unit = unit;
  }
}
