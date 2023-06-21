package org.cyclonedx.model.formulation.trigger;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.formulation.common.BasicDataAbstract;
import org.cyclonedx.model.formulation.common.InputType;
import org.cyclonedx.model.formulation.common.OutputType;

public class Trigger extends BasicDataAbstract
{
  private String type;
  private Event event;
  private List<Condition> conditions;
  private String timeActivated;
  private List<InputType> inputs;
  private List<OutputType> outputs;

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

  @JacksonXmlElementWrapper(localName = "conditions")
  @JacksonXmlProperty(localName = "condition")
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

  @JacksonXmlElementWrapper(localName = "inputs")
  @JacksonXmlProperty(localName = "input")
  public List<InputType> getInputs() {
    return inputs;
  }

  public void setInputs(final List<InputType> inputs) {
    this.inputs = inputs;
  }

  @JacksonXmlElementWrapper(localName = "outputs")
  @JacksonXmlProperty(localName = "output")
  public List<OutputType> getOutputs() {
    return outputs;
  }

  public void setOutputs(final List<OutputType> outputs) {
    this.outputs = outputs;
  }
}