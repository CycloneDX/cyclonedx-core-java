package org.cyclonedx.model.formulation.common;

import java.util.List;

import org.cyclonedx.model.AttachmentText;
import org.cyclonedx.model.Property;

public class OutputType {
  private String type;
  private ResourceReferenceChoice source;
  private ResourceReferenceChoice target;
  private ResourceReferenceChoice resource;
  private AttachmentText data;
  private List<Object> environmentVars;
  private List<Property> properties;

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

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

  public AttachmentText getData() {
    return data;
  }

  public void setData(final AttachmentText data) {
    this.data = data;
  }

  public List<Object> getEnvironmentVars() {
    return environmentVars;
  }

  public void setEnvironmentVars(final List<Object> environmentVars) {
    this.environmentVars = environmentVars;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }
}
