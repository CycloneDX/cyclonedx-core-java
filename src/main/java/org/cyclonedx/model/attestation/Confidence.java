package org.cyclonedx.model.attestation;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "score",
    "rationale"
})
public class Confidence
{
  private Integer score;

  private String rationale;


  public Integer getScore() {
    return score;
  }

  public void setScore(final Integer score) {
    this.score = score;
  }

  public String getRationale() {
    return rationale;
  }

  public void setRationale(final String rationale) {
    this.rationale = rationale;
  }
}
