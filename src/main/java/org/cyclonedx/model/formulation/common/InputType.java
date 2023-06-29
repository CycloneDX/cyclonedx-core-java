package org.cyclonedx.model.formulation.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cyclonedx.util.deserializer.InputTypeDeserializer;

@JsonDeserialize(using = InputTypeDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"resource", "parameter", "environmentVars", "data", "source", "target", "properties"})
public class InputType extends AbstractType
{
  private List<Parameter> parameters;

  public List<Parameter> getParameters() {
    return parameters;
  }

  public void setParameters(final List<Parameter> parameters) {
    this.parameters = parameters;
  }

  public static class Parameter {
    private String name;
    private String value;
    private String dataType;

    public String getName() {
      return name;
    }

    public void setName(final String name) {
      this.name = name;
    }

    public String getValue() {
      return value;
    }

    public void setValue(final String value) {
      this.value = value;
    }

    public String getDataType() {
      return dataType;
    }

    public void setDataType(final String dataType) {
      this.dataType = dataType;
    }
  }
}