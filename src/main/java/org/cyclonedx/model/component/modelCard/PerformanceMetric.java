package org.cyclonedx.model.component.modelCard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PerformanceMetric extends ExtensibleElement
{
  private String type;
  private String value;

  private String slice;

  @JacksonXmlProperty(localName = "confidenceInterval")
  private ConfidentialInterval confidentialInterval;

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public static class ConfidentialInterval
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

  public ConfidentialInterval getConfidentialInterval() {
    return confidentialInterval;
  }

  public void setConfidentialInterval(final ConfidentialInterval confidentialInterval) {
    this.confidentialInterval = confidentialInterval;
  }
}
