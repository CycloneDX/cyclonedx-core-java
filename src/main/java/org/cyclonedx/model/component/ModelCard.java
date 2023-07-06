package org.cyclonedx.model.component;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.component.modelCard.Considerations;
import org.cyclonedx.model.component.modelCard.ModelParameters;
import org.cyclonedx.model.component.modelCard.QuantitativeAnalysis;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ModelCard extends ExtensibleElement
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;

  private ModelParameters modelParameters;

  private QuantitativeAnalysis quantitativeAnalysis;

  private Considerations considerations;

  private List<Property> properties;

  public String getBomRef() {
    return bomRef;
  }

  public void setBomRef(final String bomRef) {
    this.bomRef = bomRef;
  }

  public ModelParameters getModelParameters() {
    return modelParameters;
  }

  public void setModelParameters(final ModelParameters modelParameters) {
    this.modelParameters = modelParameters;
  }

  public QuantitativeAnalysis getQuantitativeAnalysis() {
    return quantitativeAnalysis;
  }

  public void setQuantitativeAnalysis(final QuantitativeAnalysis quantitativeAnalysis) {
    this.quantitativeAnalysis = quantitativeAnalysis;
  }

  public Considerations getConsiderations() {
    return considerations;
  }

  public void setConsiderations(final Considerations considerations) {
    this.considerations = considerations;
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
