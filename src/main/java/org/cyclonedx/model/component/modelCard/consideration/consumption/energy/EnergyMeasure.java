package org.cyclonedx.model.component.modelCard.consideration.consumption.energy;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EnergyMeasure
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

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof EnergyMeasure)) {
      return false;
    }
    EnergyMeasure that = (EnergyMeasure) object;
    return Double.compare(value, that.value) == 0 && unit == that.unit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, unit);
  }
}
