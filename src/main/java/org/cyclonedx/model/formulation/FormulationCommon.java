package org.cyclonedx.model.formulation;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.DependencyList;
import org.cyclonedx.model.formulation.common.BasicDataAbstract;
import org.cyclonedx.model.formulation.common.InputType;
import org.cyclonedx.model.formulation.common.OutputType;
import org.cyclonedx.model.formulation.task.Step;
import org.cyclonedx.model.formulation.trigger.Trigger;
import org.cyclonedx.model.formulation.workspace.Workspace;
import org.cyclonedx.util.deserializer.DependencyDeserializer;
import org.cyclonedx.util.serializer.CustomDateSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class FormulationCommon extends BasicDataAbstract
{
  protected Trigger trigger;

  protected List<TaskType> taskTypes;

  protected List<InputType> inputTypes;

  protected List<OutputType> outputTypes;

  @JsonSerialize(using = CustomDateSerializer.class)
  protected Date timeStart;

  @JsonSerialize(using = CustomDateSerializer.class)
  protected Date timeEnd;

  protected List<Workspace> workspaces;

  @JsonProperty("runtimeTopology")
  protected DependencyList runtimeTopology;

  @JsonProperty("taskDependencies")
  protected DependencyList taskDependencies;

  private List<Step> steps;

  @JacksonXmlElementWrapper(localName = "steps")
  @JacksonXmlProperty(localName = "step")
  public List<Step> getSteps() {
    return steps;
  }

  public void setSteps(final List<Step> steps) {
    this.steps = steps;
  }

  @JacksonXmlElementWrapper(useWrapping = false)
  @JsonDeserialize(using = DependencyDeserializer.class)
  public List<Dependency> getTaskDependencies() {
    return taskDependencies;
  }

  public void setTaskDependencies(List<Dependency> taskDependencies) {
    this.taskDependencies = new DependencyList(taskDependencies);
  }

  @JacksonXmlElementWrapper(useWrapping = false)
  @JsonDeserialize(using = DependencyDeserializer.class)
  public List<Dependency> getRuntimeTopology() {
    return runtimeTopology;
  }

  public void setRuntimeTopology(List<Dependency> runtimeTopology) {
    this.runtimeTopology = new DependencyList(runtimeTopology);
  }

  public Trigger getTrigger() {
    return trigger;
  }

  public void setTrigger(final Trigger trigger) {
    this.trigger = trigger;
  }

  @JacksonXmlElementWrapper(localName = "taskTypes")
  @JacksonXmlProperty(localName = "taskType")
  public List<TaskType> getTaskTypes() {
    return taskTypes;
  }

  public void setTaskTypes(final List<TaskType> taskTypes) {
    this.taskTypes = taskTypes;
  }

  @JacksonXmlElementWrapper(localName = "inputs")
  @JacksonXmlProperty(localName = "input")
  public List<InputType> getInputTypes() {
    return inputTypes;
  }

  public void setInputTypes(final List<InputType> inputTypes) {
    this.inputTypes = inputTypes;
  }

  @JacksonXmlElementWrapper(localName = "outputs")
  @JacksonXmlProperty(localName = "output")
  public List<OutputType> getOutputTypes() {
    return outputTypes;
  }

  public void setOutputTypes(final List<OutputType> outputTypes) {
    this.outputTypes = outputTypes;
  }

  public Date getTimeStart() {
    return timeStart;
  }

  public void setTimeStart(final Date timeStart) {
    this.timeStart = timeStart;
  }

  public Date getTimeEnd() {
    return timeEnd;
  }

  public void setTimeEnd(final Date timeEnd) {
    this.timeEnd = timeEnd;
  }

  @JacksonXmlElementWrapper(localName = "workspaces")
  @JacksonXmlProperty(localName = "workspace")
  public List<Workspace> getWorkspaces() {
    return workspaces;
  }

  public void setWorkspaces(final List<Workspace> workspaces) {
    this.workspaces = workspaces;
  }

  public enum TaskType {
    @JsonProperty("copy")
    COPY("copy"),
    @JsonProperty("clone")
    CLONE("clone"),
    @JsonProperty("LINT")
    LINT("LINT"),
    @JsonProperty("scan")
    SCAN("scan"),
    @JsonProperty("merge")
    MERGE("merge"),
    @JsonProperty("build")
    BUILD("build"),
    @JsonProperty("test")
    TEST("test"),
    @JsonProperty("deliver")
    DELIVER("deliver"),
    @JsonProperty("deploy")
    DEPLOY("deploy"),
    @JsonProperty("release")
    RELEASE("release"),
    @JsonProperty("clean")
    CLEAN("clean"),
    @JsonProperty("other")
    OTHER("other");

    private final String name;

    public String getTypeName() {
      return this.name;
    }

    TaskType(String name) {
      this.name = name;
    }
  }
}
