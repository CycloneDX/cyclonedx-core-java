package org.cyclonedx.model.component.modelCard.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Governance
{
  private List<DataGovernanceResponsibleParty> custodians;

  private List<DataGovernanceResponsibleParty> stewards;

  private List<DataGovernanceResponsibleParty> owners;


  public static class DataGovernanceResponsibleParty {
    private OrganizationalEntity organization;

    private OrganizationalContact contact;

    public OrganizationalEntity getOrganization() {
      return organization;
    }

    public void setOrganization(final OrganizationalEntity organization) {
      this.organization = organization;
    }

    public OrganizationalContact getContact() {
      return contact;
    }

    public void setContact(final OrganizationalContact contact) {
      this.contact = contact;
    }
  }

  public List<DataGovernanceResponsibleParty> getCustodians() {
    return custodians;
  }

  public void setCustodians(final List<DataGovernanceResponsibleParty> custodians) {
    this.custodians = custodians;
  }

  public List<DataGovernanceResponsibleParty> getStewards() {
    return stewards;
  }

  public void setStewards(final List<DataGovernanceResponsibleParty> stewards) {
    this.stewards = stewards;
  }

  public List<DataGovernanceResponsibleParty> getOwners() {
    return owners;
  }

  public void setOwners(final List<DataGovernanceResponsibleParty> owners) {
    this.owners = owners;
  }
}
