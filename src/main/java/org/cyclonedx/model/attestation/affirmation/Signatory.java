package org.cyclonedx.model.attestation.affirmation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Signature;
import org.cyclonedx.util.deserializer.SignatoryDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeName("signatory")
@JsonDeserialize(using = SignatoryDeserializer.class)
public class Signatory
{
  private String name;

  private String role;

  private Signature signature;

  private OrganizationalEntity organization;
  private ExternalReference externalReference;

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

  public Signature getSignature() {
    return signature;
  }

  public void setSignature(final Signature signature) {
    this.signature = signature;

    //If Signature is present organization and external reference are not allowed
    organization = null;
    externalReference = null;
  }

  public OrganizationalEntity getOrganization() {
    return organization;
  }

  public void setOrganization(final OrganizationalEntity organization) {
    this.organization = organization;
    //if organization and external reference are present signature is not allowed
    signature = null;
  }

  public ExternalReference getExternalReference() {
    return externalReference;
  }

  public void setExternalReference(final ExternalReference externalReference) {
    this.externalReference = externalReference;
    //if organization and external reference are present signature is not allowed
    signature = null;
  }

  public void setExternalReferenceAndOrganization(
      final ExternalReference externalReference,
      final OrganizationalEntity organization)
  {
    this.organization = organization;
    this.externalReference = externalReference;
    //if organization and external reference are present signature is not allowed
    signature = null;
  }
}
