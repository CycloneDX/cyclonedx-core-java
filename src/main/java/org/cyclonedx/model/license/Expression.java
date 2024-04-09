package org.cyclonedx.model.license;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import org.cyclonedx.util.deserializer.ExpressionDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
@JsonPropertyOrder({"id", "acknowledgement", "bom-ref"})
@JsonDeserialize(using = ExpressionDeserializer.class)
public class Expression
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;
  @JacksonXmlProperty(isAttribute = true, localName = "acknowledgement")
  @JsonProperty("acknowledgement")
  private String acknowledgement;

  @JacksonXmlText
  @JsonProperty("expression")
  private String id;

  public Expression() {

  }

  public Expression(String id) {
    this.id = id;
  }

  public String getBomRef() {
    return bomRef;
  }

  public void setBomRef(final String bomRef) {
    this.bomRef = bomRef;
  }

  public String getAcknowledgement() {
    return acknowledgement;
  }

  public void setAcknowledgement(final String acknowledgement) {
    this.acknowledgement = acknowledgement;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }
}
