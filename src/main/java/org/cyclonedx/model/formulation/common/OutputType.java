package org.cyclonedx.model.formulation.common;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cyclonedx.util.deserializer.OutputTypeDeserializer;

@JsonDeserialize(using = OutputTypeDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class OutputType
    extends AbstractType
{
  @JsonProperty("type")
  private OutputTypeEnum type;

  public OutputTypeEnum getType() {
    return type;
  }

  public void setType(final OutputTypeEnum type) {
    this.type = type;
  }

  public enum OutputTypeEnum {

    @JsonProperty("artifact")
    ARTIFACT("artifact"),
    @JsonProperty("attestation")
    ATTESTATION("attestation"),
    @JsonProperty("log")
    LOG("log"),
    @JsonProperty("evidence")
    EVIDENCE("evidence"),
    @JsonProperty("metrics")
    METRICS("metrics"),
    @JsonProperty("other")
    OTHER("other");

    private final String name;

    public String getTypeName() {
      return this.name;
    }

    OutputTypeEnum(String name) {
      this.name = name;
    }
  }
}
