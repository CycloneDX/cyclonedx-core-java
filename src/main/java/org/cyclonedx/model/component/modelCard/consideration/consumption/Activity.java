package org.cyclonedx.model.component.modelCard.consideration.consumption;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Activity
{
  @JsonProperty("design")
  DESIGN("design"),
  @JsonProperty("data-collection")
  DATA_COLLECTION("data-collection"),
  @JsonProperty("data-preparation")
  DATA_PREPARATION("data-preparation"),
  @JsonProperty("training")
  TRAINING("training"),
  @JsonProperty("fine-tuning")
  FINE_TUNING("fine-tuning"),
  @JsonProperty("validation")
  VALIDATION("validation"),
  @JsonProperty("deployment")
  DEPLOYMENT("deployment"),
  @JsonProperty("inference")
  INFERENCE("inference"),
  @JsonProperty("other")
  OTHER("other");

  private final String name;

  Activity(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
