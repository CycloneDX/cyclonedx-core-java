package org.cyclonedx.model.attestation.affirmation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cyclonedx.util.deserializer.SignatoryInformationChoiceDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "name",
    "role",
    "signature",
    "organization",
    "externalReference"
})
public class Signatory
{
  private String name;

  private String role;

  @JsonDeserialize(using = SignatoryInformationChoiceDeserializer.class)
  private SignatoryInformationChoice choice;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getRole() {
    return role;
  }

  public void setRole(final String role) {
    this.role = role;
  }

  public SignatoryInformationChoice getChoice() {
    return choice;
  }

  public void setChoice(final SignatoryInformationChoice choice) {
    this.choice = choice;
  }
}
