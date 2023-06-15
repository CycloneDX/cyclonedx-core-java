package org.cyclonedx.model.formulation.common;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cyclonedx.model.AttachmentText;
import org.cyclonedx.model.Property;
import org.cyclonedx.util.InputTypeDeserializer;

@JsonDeserialize(using = InputTypeDeserializer.class)
public class InputType {
  private ResourceReferenceChoice source;
  private ResourceReferenceChoice target;
  private ResourceReferenceChoice resource;
  private List<Parameter> parameters;
  private List<Property> environmentVars;
  private AttachmentText data;
  private List<Property> properties;

  public ResourceReferenceChoice getSource() {
    return source;
  }

  public void setSource(final ResourceReferenceChoice source) {
    this.source = source;
  }

  public ResourceReferenceChoice getTarget() {
    return target;
  }

  public void setTarget(final ResourceReferenceChoice target) {
    this.target = target;
  }

  public ResourceReferenceChoice getResource() {
    return resource;
  }

  public void setResource(final ResourceReferenceChoice resource) {
    this.resource = resource;
  }

  public List<Parameter> getParameters() {
    return parameters;
  }

  public void setParameters(final List<Parameter> parameters) {
    this.parameters = parameters;
  }

  public List<Property> getEnvironmentVars() {
    return environmentVars;
  }

  public void setEnvironmentVars(final List<Property> environmentVars) {
    this.environmentVars = environmentVars;
  }

  public AttachmentText getData() {
    return data;
  }

  public void setData(final AttachmentText data) {
    this.data = data;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
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