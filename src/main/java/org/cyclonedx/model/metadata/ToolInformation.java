package org.cyclonedx.model.metadata;

import java.util.List;
import java.util.Objects;

import org.cyclonedx.model.Component;
import org.cyclonedx.model.Service;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ToolInformation
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
  
  @Override
  public int hashCode() {
      return Objects.hash(components, services);
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ToolInformation other = (ToolInformation) o;
      return Objects.equals(components, other.components) &&
              Objects.equals(services, other.services);
  }
}
