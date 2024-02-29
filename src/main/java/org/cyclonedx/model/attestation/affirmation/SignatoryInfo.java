package org.cyclonedx.model.attestation.affirmation;

import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.OrganizationalEntity;

public class SignatoryInfo
{
  private OrganizationalEntity organization;
  private ExternalReference externalReference;

  public OrganizationalEntity getOrganization() {
    return organization;
  }

  public void setOrganization(final OrganizationalEntity organization) {
    this.organization = organization;
  }

  public ExternalReference getExternalReference() {
    return externalReference;
  }

  public void setExternalReference(final ExternalReference externalReference) {
    this.externalReference = externalReference;
  }
}
