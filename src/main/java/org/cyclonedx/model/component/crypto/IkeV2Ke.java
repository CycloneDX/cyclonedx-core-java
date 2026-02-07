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
@JsonPropertyOrder({"group", "algorithm"})
@JsonSerialize(using = IkeV2TransformSerializer.class)
public class IkeV2Ke extends AbstractIkeV2Transform {

  private Integer group;

  public IkeV2Ke() {
  }

  @JsonCreator
  public IkeV2Ke(String algorithm) {
    super(algorithm);
  }

  public Integer getGroup() {
    return group;
  }

  public void setGroup(final Integer group) {
    this.group = group;
  }

  @Override
  public boolean isStringOnly() {
    return getName() == null && group == null && getAlgorithm() != null;
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof IkeV2Ke)) {
      return false;
    }
    if (!super.equals(object)) {
      return false;
    }
    IkeV2Ke that = (IkeV2Ke) object;
    return Objects.equals(group, that.group);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), group);
  }
}
