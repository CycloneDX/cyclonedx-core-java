package org.cyclonedx.model.formulation.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.Property;
import org.cyclonedx.util.deserializer.EnvVariableChoiceDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(using = EnvVariableChoiceDeserializer.class)
public class EnvVariableChoice
{
  @JacksonXmlProperty(localName = "value")
  private String value;

  @JacksonXmlProperty(localName = "environmentVar")
  private Property environmentVar;

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public Property getEnvironmentVar() {
    return environmentVar;
  }

  public void setEnvironmentVar(final Property environmentVar) {
    this.environmentVar = environmentVar;
  }
}