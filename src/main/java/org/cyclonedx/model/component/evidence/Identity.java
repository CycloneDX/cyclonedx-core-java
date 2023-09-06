package org.cyclonedx.model.component.evidence;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.BomReference;
import org.cyclonedx.model.ExtensibleElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"field", "confidence", "methods", "tools"})
public class Identity extends ExtensibleElement
{
  public Field field;

  public Double confidence;

  public List<Method> methods;

  public List<BomReference> tools;

  public enum Field {
    @JsonProperty("group")
    GROUP("group"),
    @JsonProperty("name")
    NAME("name"),
    @JsonProperty("version")
    VERSION("version"),
    @JsonProperty("purl")
    PURL("purl"),
    @JsonProperty("cpe")
    CPE( "cpe"),
    @JsonProperty("swid")
    SWID("swid"),
    @JsonProperty("hash")
    HASH( "hash");

    private final String name;

    public String getTypeName() {
      return this.name;
    }

    Field(String name) {
      this.name = name;
    }
  }

  public Field getField() {
    return field;
  }

  public void setField(final Field field) {
    this.field = field;
  }

  public Double getConfidence() {
    return confidence;
  }

  public void setConfidence(final Double confidence) {
    this.confidence = confidence;
  }

  @JsonProperty("methods")
  @JacksonXmlElementWrapper(localName = "methods")
  @JacksonXmlProperty(localName = "method")
  public List<Method> getMethods() {
    return methods;
  }

  public void setMethods(final List<Method> methods) {
    this.methods = methods;
  }

  @JsonProperty("tools")
  @JacksonXmlElementWrapper(localName = "tools")
  @JacksonXmlProperty(localName = "tool")
  public List<BomReference> getTools() {
    return tools;
  }

  public void setTools(final List<BomReference> tools) {
    this.tools = tools;
  }
}
