package org.cyclonedx.model.component.crypto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Ikev2TransformTypes {

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "encr")
  @JsonProperty("encr")
  private List<IkeV2Enc> encr;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "prf")
  @JsonProperty("prf")
  private List<IkeV2Prf> prf;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "integ")
  @JsonProperty("integ")
  private List<IkeV2Integ> integ;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "ke")
  @JsonProperty("ke")
  private List<IkeV2Ke> ke;

  @JsonProperty("esn")
  private Boolean esn;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "auth")
  @JsonProperty("auth")
  private List<IkeV2Auth> auth;

  public Ikev2TransformTypes() {
  }

  public List<IkeV2Enc> getEncr() {
    return encr;
  }

  public void setEncr(List<IkeV2Enc> encr) {
    this.encr = encr;
  }

  public List<IkeV2Prf> getPrf() {
    return prf;
  }

  public void setPrf(List<IkeV2Prf> prf) {
    this.prf = prf;
  }

  public List<IkeV2Integ> getInteg() {
    return integ;
  }

  public void setInteg(List<IkeV2Integ> integ) {
    this.integ = integ;
  }

  public List<IkeV2Ke> getKe() {
    return ke;
  }

  public void setKe(List<IkeV2Ke> ke) {
    this.ke = ke;
  }

  public Boolean getEsn() {
    return esn;
  }

  public void setEsn(Boolean esn) {
    this.esn = esn;
  }

  public List<IkeV2Auth> getAuth() {
    return auth;
  }

  public void setAuth(List<IkeV2Auth> auth) {
    this.auth = auth;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Ikev2TransformTypes)) {
      return false;
    }
    Ikev2TransformTypes that = (Ikev2TransformTypes) object;
    return Objects.equals(encr, that.encr) && Objects.equals(prf, that.prf) &&
        Objects.equals(integ, that.integ) && Objects.equals(ke, that.ke) &&
        Objects.equals(esn, that.esn) && Objects.equals(auth, that.auth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(encr, prf, integ, ke, esn, auth);
  }
}
