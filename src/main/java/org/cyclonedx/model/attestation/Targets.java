package org.cyclonedx.model.attestation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Service;

@JacksonXmlRootElement(localName = "targets")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "organizations",
    "components",
    "services"
})
public class Targets
{
  private List<OrganizationalEntity> organizations;

  private List<Component> components;

  private List<Service> services;

  @JacksonXmlElementWrapper(localName = "organizations")
  @JacksonXmlProperty(localName = "organization")
  public List<OrganizationalEntity> getOrganizations() {
    return organizations;
  }

  public void setOrganizations(final List<OrganizationalEntity> organizations) {
    this.organizations = organizations;
  }

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
