package org.cyclonedx.model.component.crypto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.component.crypto.enums.ProtocolType;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"type", "version", "cipherSuites", "ikev2TransformTypes", "cryptoRefArray"})
public class ProtocolProperties
{
  private ProtocolType type;

  private String version;

  private List<CipherSuite> cipherSuites;

  @JsonProperty("ikev2TransformTypes")
  private Ikev2TransformTypes ikev2TransformTypes;

  private List<String> cryptoRefArray;

  public ProtocolType getType() {
    return type;
  }

  public void setType(final ProtocolType type) {
    this.type = type;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(final String version) {
    this.version = version;
  }

  @JacksonXmlElementWrapper(localName = "cipherSuites")
  @JacksonXmlProperty(localName = "cipherSuite")
  @JsonProperty("cipherSuites")
  public List<CipherSuite> getCipherSuites() {
    return cipherSuites;
  }

  public void setCipherSuites(final List<CipherSuite> cipherSuites) {
    this.cipherSuites = cipherSuites;
  }

  public Ikev2TransformTypes getIkev2TransformTypes() {
    return ikev2TransformTypes;
  }

  public void setIkev2TransformTypes(final Ikev2TransformTypes ikev2TransformTypes) {
    this.ikev2TransformTypes = ikev2TransformTypes;
  }

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "cryptoRef")
  @JsonProperty("cryptoRefArray")
  public List<String> getCryptoRefArray() {
    return cryptoRefArray;
  }

  public void setCryptoRefArray(final List<String> cryptoRefArray) {
    this.cryptoRefArray = cryptoRefArray;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof ProtocolProperties)) {
      return false;
    }
    ProtocolProperties that = (ProtocolProperties) object;
    return type == that.type && Objects.equals(version, that.version) &&
        Objects.equals(cipherSuites, that.cipherSuites) &&
        Objects.equals(ikev2TransformTypes, that.ikev2TransformTypes) &&
        Objects.equals(cryptoRefArray, that.cryptoRefArray);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, version, cipherSuites, ikev2TransformTypes, cryptoRefArray);
  }
}
