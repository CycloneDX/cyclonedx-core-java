package org.cyclonedx.model.component.crypto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.cyclonedx.model.component.crypto.enums.AssetType;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
    "assetType",
    "algorithmProperties",
    "certificateProperties",
    "relatedCryptoMaterialProperties",
    "protocolProperties",
    "oid"
})
public class CryptoProperties
{

  private AssetType assetType;

  private AlgorithmProperties algorithmProperties;

  private CertificateProperties certificateProperties;

  private RelatedCryptoMaterialProperties relatedCryptoMaterialProperties;

  private ProtocolProperties protocolProperties;

  private String oid;

  public AssetType getAssetType() {
    return assetType;
  }

  public void setAssetType(final AssetType assetType) {
    this.assetType = assetType;
  }

  public AlgorithmProperties getAlgorithmProperties() {
    return algorithmProperties;
  }

  public void setAlgorithmProperties(final AlgorithmProperties algorithmProperties) {
    this.algorithmProperties = algorithmProperties;
  }

  public CertificateProperties getCertificateProperties() {
    return certificateProperties;
  }

  public void setCertificateProperties(final CertificateProperties certificateProperties) {
    this.certificateProperties = certificateProperties;
  }

  public RelatedCryptoMaterialProperties getRelatedCryptoMaterialProperties() {
    return relatedCryptoMaterialProperties;
  }

  public void setRelatedCryptoMaterialProperties(final RelatedCryptoMaterialProperties relatedCryptoMaterialProperties) {
    this.relatedCryptoMaterialProperties = relatedCryptoMaterialProperties;
  }

  public ProtocolProperties getProtocolProperties() {
    return protocolProperties;
  }

  public void setProtocolProperties(final ProtocolProperties protocolProperties) {
    this.protocolProperties = protocolProperties;
  }

  public String getOid() {
    return oid;
  }

  public void setOid(final String oid) {
    this.oid = oid;
  }
}
