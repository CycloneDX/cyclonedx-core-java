package org.cyclonedx.model.attestation.affirmation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.cyclonedx.model.Signature;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SignatoryInformationChoice
{
  private Signature signature;

  private SignatoryInfo signatoryInfo;

  public Signature getSignature() {
    return signature;
  }

  public void setSignature(final Signature signature) {
    this.signature = signature;
  }

  public SignatoryInfo getSignatoryInfo() {
    return signatoryInfo;
  }

  public void setSignatoryInfo(final SignatoryInfo signatoryInfo) {
    this.signatoryInfo = signatoryInfo;
  }
}
