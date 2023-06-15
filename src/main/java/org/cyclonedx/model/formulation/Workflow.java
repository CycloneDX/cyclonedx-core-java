package org.cyclonedx.model.formulation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.cyclonedx.model.formulation.task.Task;
@JacksonXmlRootElement(localName = "task")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(
    {
        "uid", "name", "description", "resourceReferences", "trigger", "taskTypes", "inputTypes", "outputTypes",
        "timeStart", "timeEnd", "workspaces", "runtimeTopology", "taskDependencyGraph", "properties", "tasks"
    })
public class Workflow extends FormulationCommon
{
  private List<Task> tasks;

  public List<Task> getTasks() {
    return tasks;
  }

  public void setTasks(final List<Task> tasks) {
    this.tasks = tasks;
  }
}
