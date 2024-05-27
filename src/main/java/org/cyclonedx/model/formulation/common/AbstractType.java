package org.cyclonedx.model.formulation.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.cyclonedx.model.AttachmentText;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.Property;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class AbstractType extends ExtensibleElement
{
  private ResourceReferenceChoice source;
  private ResourceReferenceChoice target;
  private ResourceReferenceChoice resource;
  private EnvironmentVars environmentVars;
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

  @JacksonXmlElementWrapper(localName = "environmentVars")
  public EnvironmentVars getEnvironmentVars() {
    return environmentVars;
  }

  public void setEnvironmentVars(final EnvironmentVars environmentVars) {
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
}