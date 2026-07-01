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
package org.cyclonedx.model.component.crypto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Abstract base class for IKEv2 transform types.
 * All IKEv2 transforms share algorithm and name fields.
 * Subclasses add type-specific fields (keyLength, group, etc.).
 *
 * @since 10.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractIkeV2Transform {

  private String name;
  private String algorithm;

  protected AbstractIkeV2Transform() {
  }

  protected AbstractIkeV2Transform(String algorithm) {
    this.algorithm = algorithm;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(final String algorithm) {
    this.algorithm = algorithm;
  }

  /**
   * Returns true if this transform was constructed from a plain string value
   * (1.6 backward compatibility format) and should be serialized as a string.
   */
  public abstract boolean isStringOnly();

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof AbstractIkeV2Transform)) {
      return false;
    }
    AbstractIkeV2Transform that = (AbstractIkeV2Transform) object;
    return Objects.equals(name, that.name) && Objects.equals(algorithm, that.algorithm);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, algorithm);
  }
}
