package org.cyclonedx.model.component.modelCard;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.component.data.Graphics;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"performanceMetrics", "graphics"})
public class QuantitativeAnalysis
    extends ExtensibleElement
{
  private List<PerformanceMetric> performanceMetrics;
  private Graphics graphics;

  @JacksonXmlElementWrapper(localName = "performanceMetrics")
  @JacksonXmlProperty(localName = "performanceMetric")
  public List<PerformanceMetric> getPerformanceMetrics() {
    return performanceMetrics;
  }

  public void setPerformanceMetrics(final List<PerformanceMetric> performanceMetrics) {
    this.performanceMetrics = performanceMetrics;
  }

  public Graphics getGraphics() {
    return graphics;
  }

  public void setGraphics(final Graphics graphics) {
    this.graphics = graphics;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof QuantitativeAnalysis)) {
      return false;
    }
    QuantitativeAnalysis that = (QuantitativeAnalysis) object;
    return Objects.equals(performanceMetrics, that.performanceMetrics) &&
        Objects.equals(graphics, that.graphics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(performanceMetrics, graphics);
  }
}
