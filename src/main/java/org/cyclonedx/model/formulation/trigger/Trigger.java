package org.cyclonedx.model.formulation.trigger;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.formulation.common.InputType;
import org.cyclonedx.model.formulation.common.OutputType;
import org.cyclonedx.model.formulation.common.ResourceReferenceChoice;

public class Trigger extends ExtensibleElement
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;

  private String uid;

  private String name;

  private String description;

  private List<ResourceReferenceChoice> resourceReferences;

  private String type;
  private Event event;
  private List<Condition> conditions;
  private String timeActivated;
  private List<InputType> inputs;
  private List<OutputType> outputs;
  private List<Property> properties;

  public String getBomRef() {
    return bomRef;
  }

  public void setBomRef(final String bomRef) {
    this.bomRef = bomRef;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(final String uid) {
    this.uid = uid;
  }

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

  public List<ResourceReferenceChoice> getResourceReferences() {
    return resourceReferences;
  }

  public void setResourceReferences(final List<ResourceReferenceChoice> resourceReferences) {
    this.resourceReferences = resourceReferences;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public Event getEvent() {
    return event;
  }

  public void setEvent(final Event event) {
    this.event = event;
  }

  public List<Condition> getConditions() {
    return conditions;
  }

  public void setConditions(final List<Condition> conditions) {
    this.conditions = conditions;
  }

  public String getTimeActivated() {
    return timeActivated;
  }

  public void setTimeActivated(final String timeActivated) {
    this.timeActivated = timeActivated;
  }

  public List<InputType> getInputs() {
    return inputs;
  }

  public void setInputs(final List<InputType> inputs) {
    this.inputs = inputs;
  }

  public List<OutputType> getOutputs() {
    return outputs;
  }

  public void setOutputs(final List<OutputType> outputs) {
    this.outputs = outputs;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }
}