package org.cyclonedx.model.attestation.affirmation;

public class Signatory
{
  private String name;

  private String role;

  private SignatoryChoice signatoryChoice;

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

  public SignatoryChoice getSignatoryChoice() {
    return signatoryChoice;
  }

  public void setSignatoryChoice(final SignatoryChoice signatoryChoice) {
    this.signatoryChoice = signatoryChoice;
  }
}
