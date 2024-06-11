package org.cyclonedx.model.attestation;


import java.util.Objects;

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
  private Double score;

  private String rationale;


  public Double getScore() {
    return score;
  }

  public void setScore(final Double score) {
    this.score = score;
  }

  public String getRationale() {
    return rationale;
  }

  public void setRationale(final String rationale) {
    this.rationale = rationale;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Confidence)) {
      return false;
    }
    Confidence that = (Confidence) object;
    return Objects.equals(score, that.score) && Objects.equals(rationale, that.rationale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(score, rationale);
  }
}
