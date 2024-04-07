package org.cyclonedx.model.component.crypto.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AssetType {
    @JsonProperty("algorithm")
    ALGORITHM("algorithm"),
    @JsonProperty("certificate")
    CERTIFICATE("certificate"),
    @JsonProperty("protocol")
    PROTOCOL("protocol"),
    @JsonProperty("related-crypto-material")
    RELATED_CRYPTO_MATERIAL("related-crypto-material");

    private final String name;

    AssetType(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
}
