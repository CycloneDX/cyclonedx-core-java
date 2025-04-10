package org.cyclonedx.model.component.crypto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class Ikev2TransformTypes {

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "encr")
  @JsonProperty("encr")
  private List<String> encr;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "prf")
  @JsonProperty("prf")
  private List<String> prf;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "integ")
  @JsonProperty("integ")
  private List<String> integ;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "ke")
  @JsonProperty("ke")
  private List<String> ke;

  @JsonProperty("esn")
  private Boolean esn;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "auth")
  @JsonProperty("auth")
  private List<String> auth;

  public Ikev2TransformTypes() {
  }

  public List<String> getEncr() {
    return encr;
  }

  public void setEncr(List<String> encr) {
    this.encr = encr;
  }

  public List<String> getPrf() {
    return prf;
  }

  public void setPrf(List<String> prf) {
    this.prf = prf;
  }

  public List<String> getInteg() {
    return integ;
  }

  public void setInteg(List<String> integ) {
    this.integ = integ;
  }

  public List<String> getKe() {
    return ke;
  }

  public void setKe(List<String> ke) {
    this.ke = ke;
  }

  public Boolean getEsn() {
    return esn;
  }

  public void setEsn(Boolean esn) {
    this.esn = esn;
  }

  public List<String> getAuth() {
    return auth;
  }

  public void setAuth(List<String> auth) {
    this.auth = auth;
  }
}

