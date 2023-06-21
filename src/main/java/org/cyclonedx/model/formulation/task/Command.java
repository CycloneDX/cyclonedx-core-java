package org.cyclonedx.model.formulation.task;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.Property;

public class Command extends ExtensibleElement
{
  private String executed;

  private List<Property> properties;

  public String getExecuted() {
    return executed;
  }

  public void setExecuted(final String executed) {
    this.executed = executed;
  }

  @JacksonXmlElementWrapper(localName = "properties")
  @JacksonXmlProperty(localName = "property")
  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }
}
