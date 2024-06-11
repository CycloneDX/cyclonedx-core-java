package org.cyclonedx.model.component.crypto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.cyclonedx.model.component.crypto.enums.RelatedCryptoMaterialType;
import org.cyclonedx.model.component.crypto.enums.State;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "type", "id", "state", "algorithmRef", "creationDate",
    "activationDate", "updateDate", "expirationDate", "value",
    "size", "format", "securedBy"
})
public class RelatedCryptoMaterialProperties
{
  private RelatedCryptoMaterialType type;
  private String id;
  private State state;
  private String algorithmRef;
  private String creationDate;
  private String activationDate;
  private String updateDate;
  private String expirationDate;
  private String value;
  private Integer size;
  private String format;
  private SecuredBy securedBy;

  public RelatedCryptoMaterialType getType() {
    return type;
  }

  public void setType(final RelatedCryptoMaterialType type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public State getState() {
    return state;
  }

  public void setState(final State state) {
    this.state = state;
  }

  public String getAlgorithmRef() {
    return algorithmRef;
  }

  public void setAlgorithmRef(final String algorithmRef) {
    this.algorithmRef = algorithmRef;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(final String creationDate) {
    this.creationDate = creationDate;
  }

  public String getActivationDate() {
    return activationDate;
  }

  public void setActivationDate(final String activationDate) {
    this.activationDate = activationDate;
  }

  public String getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(final String updateDate) {
    this.updateDate = updateDate;
  }

  public String getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(final String expirationDate) {
    this.expirationDate = expirationDate;
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(final Integer size) {
    this.size = size;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(final String format) {
    this.format = format;
  }

  public SecuredBy getSecuredBy() {
    return securedBy;
  }

  public void setSecuredBy(final SecuredBy securedBy) {
    this.securedBy = securedBy;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof RelatedCryptoMaterialProperties)) {
      return false;
    }
    RelatedCryptoMaterialProperties that = (RelatedCryptoMaterialProperties) object;
    return type == that.type && Objects.equals(id, that.id) && state == that.state &&
        Objects.equals(algorithmRef, that.algorithmRef) &&
        Objects.equals(creationDate, that.creationDate) &&
        Objects.equals(activationDate, that.activationDate) &&
        Objects.equals(updateDate, that.updateDate) &&
        Objects.equals(expirationDate, that.expirationDate) && Objects.equals(value, that.value) &&
        Objects.equals(size, that.size) && Objects.equals(format, that.format) &&
        Objects.equals(securedBy, that.securedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, id, state, algorithmRef, creationDate, activationDate, updateDate, expirationDate, value,
        size, format, securedBy);
  }
}
