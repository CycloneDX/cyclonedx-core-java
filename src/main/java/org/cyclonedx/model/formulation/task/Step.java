package org.cyclonedx.model.formulation.task;

import java.util.List;

import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.Property;

public class Step extends ExtensibleElement
{
  private String name;

  private String description;

  private List<Command> commands;

  private List<Property> properties;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public List<Command> getCommands() {
    return commands;
  }

  public void setCommands(final List<Command> commands) {
    this.commands = commands;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }
}