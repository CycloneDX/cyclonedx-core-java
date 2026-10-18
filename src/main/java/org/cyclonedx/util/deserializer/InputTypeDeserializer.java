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
package org.cyclonedx.util.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.cyclonedx.model.formulation.common.InputType;

public class InputTypeDeserializer extends AbstractDataTypeDeserializer<InputType> {

  @Override
  protected InputType createType() {
    return new InputType();
  }

  @Override
  protected boolean readField(String fieldName, InputType inputType, JsonParser parser, DeserializationContext ctxt)
      throws IOException
  {
    if (!"parameters".equals(fieldName)) {
      return false;
    }

    inputType.setParameters(DeserializerUtils.readList(parser, ctxt, InputType.Parameter.class));
    return true;
  }
}
