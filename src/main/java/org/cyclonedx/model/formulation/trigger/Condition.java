package org.cyclonedx.model.formulation.trigger;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
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

  @JacksonXmlElementWrapper(localName = "properties")
  @JacksonXmlProperty(localName = "property")
  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Condition)) {
      return false;
    }
    Condition condition = (Condition) object;
    return Objects.equals(description, condition.description) &&
        Objects.equals(expression, condition.expression) &&
        Objects.equals(properties, condition.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, expression, properties);
  }
}