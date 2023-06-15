package org.cyclonedx.model.formulation;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.DependencyList;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.formulation.common.InputType;
import org.cyclonedx.model.formulation.common.OutputType;
import org.cyclonedx.model.formulation.common.ResourceReferenceChoice;
import org.cyclonedx.model.formulation.trigger.Trigger;
import org.cyclonedx.model.formulation.workspace.Workspace;
import org.cyclonedx.util.CustomDateSerializer;
import org.cyclonedx.util.DependencyDeserializer;

public abstract class FormulationCommon extends ExtensibleElement
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  protected String bomRef;

  protected String uid;

  protected String name;

  protected String description;

  protected List<ResourceReferenceChoice> resourceReferences;

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

  @JsonProperty("taskDependencyGraph")
  protected DependencyList taskDependencyGraph;

  protected List<Property> properties;


  @JacksonXmlElementWrapper(useWrapping = false)
  //@JsonDeserialize(using = DependencyDeserializer.class)
  public List<Dependency> getTaskDependencyGraph() {
    return taskDependencyGraph;
  }

  public void setTaskDependencyGraph(List<Dependency> taskDependencyGraph) {
    this.taskDependencyGraph = new DependencyList(taskDependencyGraph);
  }

  @JacksonXmlElementWrapper(useWrapping = false)
  //@JsonDeserialize(using = DependencyDeserializer.class)
  public List<Dependency> getRuntimeTopology() {
    return runtimeTopology;
  }

  public void setRuntimeTopology(List<Dependency> runtimeTopology) {
    this.runtimeTopology = new DependencyList(runtimeTopology);
  }

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

  public Trigger getTrigger() {
    return trigger;
  }

  public void setTrigger(final Trigger trigger) {
    this.trigger = trigger;
  }

  public List<TaskType> getTaskTypes() {
    return taskTypes;
  }

  public void setTaskTypes(final List<TaskType> taskTypes) {
    this.taskTypes = taskTypes;
  }

  public List<InputType> getInputTypes() {
    return inputTypes;
  }

  public void setInputTypes(final List<InputType> inputTypes) {
    this.inputTypes = inputTypes;
  }

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

  public List<Workspace> getWorkspaces() {
    return workspaces;
  }

  public void setWorkspaces(final List<Workspace> workspaces) {
    this.workspaces = workspaces;
  }

  public void setRuntimeTopology(final DependencyList runtimeTopology) {
    this.runtimeTopology = runtimeTopology;
  }

  public void setTaskDependencyGraph(final DependencyList taskDependencyGraph) {
    this.taskDependencyGraph = taskDependencyGraph;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(final List<Property> properties) {
    this.properties = properties;
  }

  public enum TaskType {
    COPY,
    CLONE,
    LINT,
    SCAN,
    MERGE,
    BUILD,
    TEST,
    DELIVER,
    DEPLOY,
    RELEASE,
    CLEAN,
    OTHER
  }
}
