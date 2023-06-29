package org.cyclonedx.model.evidence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.cyclonedx.model.ExtensibleElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"technique", "confidence", "value"})
public class Method extends ExtensibleElement
{

  private Technique technique;

  private Double confidence;

  private String value;

  public Technique getTechnique() {
    return technique;
  }

  public void setTechnique(final Technique technique) {
    this.technique = technique;
  }

  public Double getConfidence() {
    return confidence;
  }

  public void setConfidence(final Double confidence) {
    this.confidence = confidence;
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public enum Technique {

    @JsonProperty("source-code-analysis")
    SOURCE_CODE_ANALYSIS("source-code-analysis"),
    @JsonProperty("binary-analysis")
    BINARY_ANALYSIS(  "binary-analysis"),
    @JsonProperty("manifest-analysis")
    MANIFEST_ANALYSIS("manifest-analysis"),
    @JsonProperty("ast-fingerprint")
    AST_FINGERPRINT("ast-fingerprint"),
    @JsonProperty("hash-comparison")
    HASH_COMPARISON("hash-comparison"),
    @JsonProperty("instrumentation")
    INSTRUMENTATION("instrumentation"),
    @JsonProperty("dynamic-analysis")
    DYNAMIC_ANALYSIS("dynamic-analysis"),
    @JsonProperty("filename")
    FILENAME("filename"),
    @JsonProperty("attestation")
    ATTESTATION("attestation"),
    @JsonProperty("other")
    OTHER("other");

    private final String name;

    public String getTypeName() {
      return this.name;
    }

    Technique(String name) {
      this.name = name;
    }
  }
}
