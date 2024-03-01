package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RelatedCryptoMaterialType
{
  @JsonProperty("private-key")
  PRIVATE_KEY("private-key", "The type for the related cryptographic material: Private Key"),
  @JsonProperty("public-key")
  PUBLIC_KEY("public-key", "The type for the related cryptographic material: Public Key"),
  @JsonProperty("secret-key")
  SECRET_KEY("secret-key", "The type for the related cryptographic material: Secret Key"),
  @JsonProperty("key")
  KEY("key", "The type for the related cryptographic material: Key"),
  @JsonProperty("ciphertext")
  CIPHERTEXT("ciphertext", "The type for the related cryptographic material: Ciphertext"),
  @JsonProperty("signature")
  SIGNATURE("signature", "The type for the related cryptographic material: Signature"),
  @JsonProperty("digest")
  DIGEST("digest", "The type for the related cryptographic material: Digest"),
  @JsonProperty("initialization-vector")
  INITIALIZATION_VECTOR("initialization-vector", "The type for the related cryptographic material: Initialization Vector"),
  @JsonProperty("nonce")
  NONCE("nonce", "The type for the related cryptographic material: Nonce"),
  @JsonProperty("seed")
  SEED("seed", "The type for the related cryptographic material: Seed"),
  @JsonProperty("salt")
  SALT("salt", "The type for the related cryptographic material: Salt"),
  @JsonProperty("shared-secret")
  SHARED_SECRET("shared-secret", "The type for the related cryptographic material: Shared Secret"),
  @JsonProperty("tag")
  TAG("tag", "The type for the related cryptographic material: Tag"),
  @JsonProperty("additional-data")
  ADDITIONAL_DATA("additional-data", "The type for the related cryptographic material: Additional Data"),
  @JsonProperty("password")
  PASSWORD("password", "The type for the related cryptographic material: Password"),
  @JsonProperty("credential")
  CREDENTIAL("credential", "The type for the related cryptographic material: Credential"),
  @JsonProperty("token")
  TOKEN("token", "The type for the related cryptographic material: Token"),
  @JsonProperty("other")
  OTHER("other", "The type for the related cryptographic material: Other"),
  @JsonProperty("unknown")
  UNKNOWN("unknown", "The type for the related cryptographic material: Unknown");

  private final String name;
  private final String description;

  RelatedCryptoMaterialType(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
