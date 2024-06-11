package org.cyclonedx.model.metadata;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Service;

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
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof ToolInformation)) {
      return false;
    }
    ToolInformation that = (ToolInformation) object;
    return Objects.equals(components, that.components) && Objects.equals(services, that.services);
  }

  @Override
  public int hashCode() {
    return Objects.hash(components, services);
  }
}
