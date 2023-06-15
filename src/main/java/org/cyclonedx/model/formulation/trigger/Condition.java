package org.cyclonedx.model.formulation.trigger;

import java.util.List;

import org.cyclonedx.model.Property;

public class Condition {
  private String description;
  private String expression;
  private List<Property> properties;

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getExpression() {
    return expression;
  }

  public void setExpression(final String expression) {
    this.expression = expression;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }
}