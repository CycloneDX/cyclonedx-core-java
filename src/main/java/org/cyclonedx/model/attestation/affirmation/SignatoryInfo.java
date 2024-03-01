package org.cyclonedx.model.attestation.affirmation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.OrganizationalEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "organization",
    "externalReference"
})
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
