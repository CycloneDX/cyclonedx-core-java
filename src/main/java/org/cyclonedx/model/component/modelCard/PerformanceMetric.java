package org.cyclonedx.model.component.modelCard;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.cyclonedx.model.ExtensibleElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PerformanceMetric extends ExtensibleElement
{
  private String type;
  private String value;

  private String slice;

  private ConfidenceInterval confidenceInterval;

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class ConfidenceInterval
  {
    @JsonProperty("lowerBound")
    private String lowerBound;

    @JsonProperty("upperBound")
    private String upperBound;

    public String getLowerBound() {
      return lowerBound;
    }

    public void setLowerBound(final String lowerBound) {
      this.lowerBound = lowerBound;
    }

    public String getUpperBound() {
      return upperBound;
    }

    public void setUpperBound(final String upperBound) {
      this.upperBound = upperBound;
    }
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public String getSlice() {
    return slice;
  }

  public void setSlice(final String slice) {
    this.slice = slice;
  }

  public ConfidenceInterval getConfidenceInterval() {
    return confidenceInterval;
  }

  public void setConfidenceInterval(final ConfidenceInterval confidenceInterval) {
    this.confidenceInterval = confidenceInterval;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof PerformanceMetric)) {
      return false;
    }
    PerformanceMetric that = (PerformanceMetric) object;
    return Objects.equals(type, that.type) && Objects.equals(value, that.value) &&
        Objects.equals(slice, that.slice) &&
        Objects.equals(confidenceInterval, that.confidenceInterval);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, value, slice, confidenceInterval);
  }
}
