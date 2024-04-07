package org.cyclonedx.model.component.crypto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.cyclonedx.model.component.crypto.enums.ProtocolType;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"type", "version", "cipherSuites", "ikev2TransformTypes", "cryptoRefArray"})
public class ProtocolProperties
{
  private ProtocolType type;

  private String version;

  private List<CipherSuite> cipherSuites;

  private Map<String, CryptoRef> ikev2TransformTypes;

  private CryptoRef cryptoRefArray;

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

  public List<CipherSuite> getCipherSuites() {
    return cipherSuites;
  }

  public void setCipherSuites(final List<CipherSuite> cipherSuites) {
    this.cipherSuites = cipherSuites;
  }

  public Map<String, CryptoRef> getIkev2TransformTypes() {
    return ikev2TransformTypes;
  }

  public void setIkev2TransformTypes(final Map<String, CryptoRef> ikev2TransformTypes) {
    this.ikev2TransformTypes = ikev2TransformTypes;
  }

  public CryptoRef getCryptoRefArray() {
    return cryptoRefArray;
  }

  public void setCryptoRefArray(final CryptoRef cryptoRefArray) {
    this.cryptoRefArray = cryptoRefArray;
  }
}
