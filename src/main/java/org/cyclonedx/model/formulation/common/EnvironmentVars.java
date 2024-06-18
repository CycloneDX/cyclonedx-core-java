package org.cyclonedx.model.formulation.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cyclonedx.util.deserializer.EnvironmentVarsDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonDeserialize(using = EnvironmentVarsDeserializer.class)
public class EnvironmentVars
{
  private List<Object> choices;

  public List<Object> getChoices() {
    return choices;
  }

  public void setChoices(List<Object> choices) {
    this.choices = choices;
  }
}