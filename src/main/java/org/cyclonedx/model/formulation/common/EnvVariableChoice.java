package org.cyclonedx.model.formulation.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.Property;
import org.cyclonedx.util.EnvVariableChoiceDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(using = EnvVariableChoiceDeserializer.class)
public class EnvVariableChoice
{
  private String value;

  private Property environmentVar;

  @JacksonXmlProperty(localName = "value")
  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  @JacksonXmlProperty(localName = "environmentVar")
  public Property getEnvironmentVar() {
    return environmentVar;
  }

  public void setEnvironmentVar(final Property environmentVar) {
    this.environmentVar = environmentVar;
  }
}