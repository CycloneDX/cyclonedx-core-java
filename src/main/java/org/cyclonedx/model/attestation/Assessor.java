package org.cyclonedx.model.attestation;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.OrganizationalEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "thirdParty",
    "organization"
})
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

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Assessor)) {
      return false;
    }
    Assessor assessor = (Assessor) object;
    return Objects.equals(bomRef, assessor.bomRef) &&
        Objects.equals(thirdParty, assessor.thirdParty) &&
        Objects.equals(organization, assessor.organization);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bomRef, thirdParty, organization);
  }
}
