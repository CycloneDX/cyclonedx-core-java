package org.cyclonedx.model.formulation.task;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.cyclonedx.model.formulation.FormulationCommon;


@JacksonXmlRootElement(localName = "task")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(
    {
        "uid", "name", "description", "resourceReferences", "trigger", "taskTypes", "inputTypes", "outputTypes",
        "timeStart", "timeEnd", "workspaces", "runtimeTopology", "taskDependencyGraph", "properties", "steps"
    })
public class Task
    extends FormulationCommon
{
  private List<Step> steps;

  public List<Step> getSteps() {
    return steps;
  }

  public void setSteps(final List<Step> steps) {
    this.steps = steps;
  }
}
