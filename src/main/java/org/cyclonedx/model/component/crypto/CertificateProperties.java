package org.cyclonedx.model.component.crypto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "subjectName",
    "issuerName",
    "notValidBefore",
    "notValidAfter",
    "signatureAlgorithmRef",
    "subjectPublicKeyRef",
    "certificateFormat",
    "certificateExtension"
    })
public class CertificateProperties
{
  private String subjectName;

  private String issuerName;

  private String notValidBefore;

  private String notValidAfter;

  private String  signatureAlgorithmRef;

  private String subjectPublicKeyRef;

  private String certificateFormat;

  private String certificateExtension;

  public String getSubjectName() {
    return subjectName;
  }

  public void setSubjectName(final String subjectName) {
    this.subjectName = subjectName;
  }

  public String getIssuerName() {
    return issuerName;
  }

  public void setIssuerName(final String issuerName) {
    this.issuerName = issuerName;
  }

  public String getNotValidBefore() {
    return notValidBefore;
  }

  public void setNotValidBefore(final String notValidBefore) {
    this.notValidBefore = notValidBefore;
  }

  public String getNotValidAfter() {
    return notValidAfter;
  }

  public void setNotValidAfter(final String notValidAfter) {
    this.notValidAfter = notValidAfter;
  }

  public String getSignatureAlgorithmRef() {
    return signatureAlgorithmRef;
  }

  public void setSignatureAlgorithmRef(final String signatureAlgorithmRef) {
    this.signatureAlgorithmRef = signatureAlgorithmRef;
  }

  public String getSubjectPublicKeyRef() {
    return subjectPublicKeyRef;
  }

  public void setSubjectPublicKeyRef(final String subjectPublicKeyRef) {
    this.subjectPublicKeyRef = subjectPublicKeyRef;
  }

  public String getCertificateFormat() {
    return certificateFormat;
  }

  public void setCertificateFormat(final String certificateFormat) {
    this.certificateFormat = certificateFormat;
  }

  public String getCertificateExtension() {
    return certificateExtension;
  }

  public void setCertificateExtension(final String certificateExtension) {
    this.certificateExtension = certificateExtension;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof CertificateProperties)) {
      return false;
    }
    CertificateProperties that = (CertificateProperties) object;
    return Objects.equals(subjectName, that.subjectName) &&
        Objects.equals(issuerName, that.issuerName) &&
        Objects.equals(notValidBefore, that.notValidBefore) &&
        Objects.equals(notValidAfter, that.notValidAfter) &&
        Objects.equals(signatureAlgorithmRef, that.signatureAlgorithmRef) &&
        Objects.equals(subjectPublicKeyRef, that.subjectPublicKeyRef) &&
        Objects.equals(certificateFormat, that.certificateFormat) &&
        Objects.equals(certificateExtension, that.certificateExtension);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subjectName, issuerName, notValidBefore, notValidAfter, signatureAlgorithmRef,
        subjectPublicKeyRef, certificateFormat, certificateExtension);
  }
}
