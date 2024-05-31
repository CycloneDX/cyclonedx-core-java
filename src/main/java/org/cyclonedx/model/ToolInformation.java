package org.cyclonedx.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "tools")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "components",
    "services"
})
public class ToolInformation
{
  private List<Component> components;

  private List<Service> services;

  @JacksonXmlElementWrapper(localName = "components")
  @JacksonXmlProperty(localName = "component")
  public List<Component> getComponents() {
    return components;
  }

  public void setComponents(final List<Component> components) {
    this.components = components;
  }

  @JacksonXmlElementWrapper(localName = "services")
  @JacksonXmlProperty(localName = "service")
  public List<Service> getServices() {
    return services;
  }

  public void setServices(final List<Service> services) {
    this.services = services;
  }
}
