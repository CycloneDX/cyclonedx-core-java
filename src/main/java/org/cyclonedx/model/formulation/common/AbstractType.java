package org.cyclonedx.model.formulation.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
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
  private List<EnvVariableChoice> environmentVars;
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
  public List<EnvVariableChoice> getEnvironmentVars() {
    return environmentVars;
  }

  public void setEnvironmentVars(final List<EnvVariableChoice> environmentVars) {
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