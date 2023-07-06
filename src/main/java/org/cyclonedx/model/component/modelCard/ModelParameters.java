package org.cyclonedx.model.component.modelCard;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ModelParameters extends ExtensibleElement
{
  private Approach approach;

  private String task;

  private String architectureFamily;

  private String modelArchitecture;

  private List<DatasetChoice> datasets;

  private List<InputOutputParameter> inputs;

  private List<InputOutputParameter> outputs;

  public static class Approach
  {
    private ApproachType type;

    public enum ApproachType
    {
      @JsonProperty("supervised")
      SUPERVISED("supervised"),
      @JsonProperty("unsupervised")
      UNSUPERVISED("unsupervised"),
      @JsonProperty("reinforcement-learning")
      REINFORCEMENT_LEARNING("reinforcement-learning"),
      @JsonProperty("semi-supervised")
      SEMI_SUPERVISED("semi-supervised"),
      @JsonProperty("self-supervised")
      SELF_SUPERVISED("self-supervised");

      private final String typeName;

      public String getTypeName() {
        return this.typeName;
      }

      ApproachType(String name) {
        this.typeName = name;
      }
    }

    public ApproachType getType() {
      return type;
    }

    public void setType(final ApproachType type) {
      this.type = type;
    }
  }

  public Approach getApproach() {
    return approach;
  }

  public void setApproach(final Approach approach) {
    this.approach = approach;
  }

  public String getTask() {
    return task;
  }

  public void setTask(final String task) {
    this.task = task;
  }

  public String getArchitectureFamily() {
    return architectureFamily;
  }

  public void setArchitectureFamily(final String architectureFamily) {
    this.architectureFamily = architectureFamily;
  }

  public String getModelArchitecture() {
    return modelArchitecture;
  }

  public void setModelArchitecture(final String modelArchitecture) {
    this.modelArchitecture = modelArchitecture;
  }

  @JacksonXmlElementWrapper(localName = "datasets")
  @JacksonXmlProperty(localName = "dataset")
  public List<DatasetChoice> getDatasets() {
    return datasets;
  }

  public void setDatasets(final List<DatasetChoice> datasets) {
    this.datasets = datasets;
  }

  @JacksonXmlElementWrapper(localName = "inputs")
  @JacksonXmlProperty(localName = "input")
  public List<InputOutputParameter> getInputs() {
    return inputs;
  }

  public void setInputs(final List<InputOutputParameter> inputs) {
    this.inputs = inputs;
  }

  @JacksonXmlElementWrapper(localName = "outputs")
  @JacksonXmlProperty(localName = "output")
  public List<InputOutputParameter> getOutputs() {
    return outputs;
  }

  public void setOutputs(final List<InputOutputParameter> outputs) {
    this.outputs = outputs;
  }
}
