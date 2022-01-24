/*
 * This file is part of CycloneDX Core (Java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) OWASP Foundation. All Rights Reserved.
 */
package org.cyclonedx.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @since 6.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "signers",
    "chain",
    "algorithm",
    "keyId",
    "publicKey",
    "certificatePath",
    "excludes",
    "value"
})
public class Signature
{
  public Signature() {}

  public enum Algorithm {
    @JsonProperty("RS256")
    RS256("RS256"),
    @JsonProperty("RS384")
    RS384("RS384"),
    @JsonProperty("RS512")
    RS512("RS512"),
    @JsonProperty("PS256")
    PS256("PS256"),
    @JsonProperty("PS384")
    PS384("PS384"),
    @JsonProperty("PS512")
    PS512("PS512"),
    @JsonProperty("ES256")
    ES256("ES256"),
    @JsonProperty("ES384")
    ES384("ES384"),
    @JsonProperty("ES512")
    ES512("ES512"),
    @JsonProperty("Ed25519")
    ED25519("Ed25519"),
    @JsonProperty("Ed448")
    ED448("Ed448"),
    @JsonProperty("HS256")
    HS256("HS256"),
    @JsonProperty("HS384")
    HS384("HS384"),
    @JsonProperty("HS512")
    HS512("HS512");

    private final String name;

    public String getAlgorithmName() {
      return this.name;
    }

    Algorithm(String name) {
      this.name = name;
    }

    public static Signature.Algorithm fromString(String text) {
      for (Signature.Algorithm a : Signature.Algorithm.values()) {
        if (a.name.equals(text)) {
          return a;
        }
      }
      return null;
    }
  }

  @JsonAlias("chain")
  private List<Signature> signers;
  private Algorithm algorithm;
  private String keyId;
  private PublicKey publicKey;
  private List<String> certificatePath;
  private List<String> excludes;
  private String value;

  public List<Signature> getSigners() {
    return signers;
  }

  public void setSigners(final List<Signature> signers) {
    this.signers = signers;
  }

  public Algorithm getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(final Algorithm algorithm) {
    this.algorithm = algorithm;
  }

  public String getKeyId() {
    return keyId;
  }

  public void setKeyId(final String keyId) {
    this.keyId = keyId;
  }

  public PublicKey getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(final PublicKey publicKey) {
    this.publicKey = publicKey;
  }

  public List<String> getCertificatePath() {
    return certificatePath;
  }

  public void setCertificatePath(final List<String> certificatePath) {
    this.certificatePath = certificatePath;
  }

  public List<String> getExcludes() {
    return excludes;
  }

  public void setExcludes(final List<String> excludes) {
    this.excludes = excludes;
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public static class PublicKey {

    public enum Kty {
      @JsonProperty("EC")
      EC("EC"),
      @JsonProperty("OKP")
      OKP("OKP"),
      @JsonProperty("RSA")
      RSA("RSA");

      private final String name;

      public String getKtyName() {
        return this.name;
      }

      Kty(String name) {
        this.name = name;
      }

      public static PublicKey.Kty fromString(String text) {
        for (PublicKey.Kty k : PublicKey.Kty.values()) {
          if (k.name.equals(text)) {
            return k;
          }
        }
        return null;
      }
    }

    private Kty kty;

    public enum Crv {
      @JsonProperty("P-256")
      P_256("P-256"),
      @JsonProperty("P-384")
      P_384("P-384"),
      @JsonProperty("P-521")
      P_521("P-521");

      private final String name;

      public String getCrvName() {
        return this.name;
      }

      Crv(String name) {
        this.name = name;
      }

      public static PublicKey.Crv fromString(String text) {
        for (PublicKey.Crv c : PublicKey.Crv.values()) {
          if (c.name.equals(text)) {
            return c;
          }
        }
        return null;
      }
    }

    private Crv crv;
    private String x;
    private String y;

    public Kty getKty() {
      return kty;
    }

    public void setKty(final Kty kty) {
      this.kty = kty;
    }

    public Crv getCrv() {
      return crv;
    }

    public void setCrv(final Crv crv) {
      this.crv = crv;
    }

    public String getX() {
      return x;
    }

    public void setX(final String x) {
      this.x = x;
    }

    public String getY() {
      return y;
    }

    public void setY(final String y) {
      this.y = y;
    }
  }
}
