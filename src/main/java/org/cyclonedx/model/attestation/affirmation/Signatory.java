package org.cyclonedx.model.attestation.affirmation;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.JsonOnly;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Signature;
import org.cyclonedx.util.deserializer.SignatoryDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"name", "role", "signature", "organization", "externalReference"})
@JsonDeserialize(using = SignatoryDeserializer.class)
public class Signatory extends ExtensibleElement
{
  private String name;

  private String role;

  @JsonOnly
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

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Signatory)) {
      return false;
    }
    Signatory signatory = (Signatory) object;
    return Objects.equals(name, signatory.name) && Objects.equals(role, signatory.role) &&
        Objects.equals(signature, signatory.signature) &&
        Objects.equals(organization, signatory.organization) &&
        Objects.equals(externalReference, signatory.externalReference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, role, signature, organization, externalReference);
  }
}
