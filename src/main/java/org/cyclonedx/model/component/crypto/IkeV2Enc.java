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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.cyclonedx.util.serializer.IkeV2TransformSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "keyLength", "algorithm"})
@JsonSerialize(using = IkeV2TransformSerializer.class)
public class IkeV2Enc extends AbstractIkeV2Transform {

  private Integer keyLength;

  public IkeV2Enc() {
  }

  @JsonCreator
  public IkeV2Enc(String algorithm) {
    super(algorithm);
  }

  public Integer getKeyLength() {
    return keyLength;
  }

  public void setKeyLength(final Integer keyLength) {
    this.keyLength = keyLength;
  }

  @Override
  public boolean isStringOnly() {
    return getName() == null && keyLength == null && getAlgorithm() != null;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof IkeV2Enc)) {
      return false;
    }
    if (!super.equals(object)) {
      return false;
    }
    IkeV2Enc that = (IkeV2Enc) object;
    return Objects.equals(keyLength, that.keyLength);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), keyLength);
  }
}
