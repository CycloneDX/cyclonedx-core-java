package org.cyclonedx.model.attestation.affirmation;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Affirmation
{
  private String statement;


  private List<Signatory> signatories;

  public String getStatement() {
    return statement;
  }

  public void setStatement(final String statement) {
    this.statement = statement;
  }

  @JacksonXmlElementWrapper(localName = "signatories")
  @JacksonXmlProperty(localName = "signatory")
  public List<Signatory> getSignatories() {
    return signatories;
  }

  public void setSignatories(final List<Signatory> signatories) {
    this.signatories = signatories;
  }
}
