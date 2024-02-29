package org.cyclonedx.model.attestation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.OrganizationalEntity;

public class Assessor
{
  @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
  @JsonProperty("bom-ref")
  private String bomRef;

  private Boolean thirdParty;

  private OrganizationalEntity organization;

  public String getBomRef() {
    return bomRef;
  }

  public void setBomRef(final String bomRef) {
    this.bomRef = bomRef;
  }

  public Boolean getThirdParty() {
    return thirdParty;
  }

  public void setThirdParty(final Boolean thirdParty) {
    this.thirdParty = thirdParty;
  }

  public OrganizationalEntity getOrganization() {
    return organization;
  }

  public void setOrganization(final OrganizationalEntity organization) {
    this.organization = organization;
  }
}
