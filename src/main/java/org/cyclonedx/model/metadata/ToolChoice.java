package org.cyclonedx.model.metadata;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Service;
import org.cyclonedx.util.ToolChoiceDeserializer;

@JsonDeserialize(using = ToolChoiceDeserializer.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
public class ToolChoice
{

  private List<Component> components;

  private List<Service> services;

  public List<Component> getComponents() {
    return components;
  }

  public void setComponents(final List<Component> components) {
    this.components = components;
  }

  public List<Service> getServices() {
    return services;
  }

  public void setServices(final List<Service> services) {
    this.services = services;
  }
}
