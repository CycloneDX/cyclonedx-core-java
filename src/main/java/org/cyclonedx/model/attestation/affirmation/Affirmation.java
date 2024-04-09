package org.cyclonedx.model.attestation.affirmation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.Signature;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "statement",
    "signatories",
    "signature"
})
public class Affirmation
{
  private String statement;

  private List<Signatory> signatories;

  private Signature signature;

  public String getStatement() {
    return statement;
  }

  public void setStatement(final String statement) {
    this.statement = statement;
  }

  @JacksonXmlProperty(localName = "signatory")
  @JacksonXmlElementWrapper(localName = "signatories")
  public List<Signatory> getSignatories() {
    return signatories;
  }

  public void setSignatories(final List<Signatory> signatories) {
    this.signatories = signatories;
  }

  public Signature getSignature() {
    return signature;
  }

  public void setSignature(final Signature signature) {
    this.signature = signature;
  }
}
