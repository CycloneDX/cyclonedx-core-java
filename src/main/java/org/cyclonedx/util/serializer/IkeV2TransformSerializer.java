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
package org.cyclonedx.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cyclonedx.model.component.crypto.AbstractIkeV2Transform;
import org.cyclonedx.model.component.crypto.IkeV2Enc;
import org.cyclonedx.model.component.crypto.IkeV2Ke;

import java.io.IOException;

public class IkeV2TransformSerializer extends JsonSerializer<AbstractIkeV2Transform> {

  @Override
  public void serialize(AbstractIkeV2Transform value, JsonGenerator gen, SerializerProvider provider)
      throws IOException
  {
    if (value.isStringOnly()) {
      gen.writeString(value.getAlgorithm());
      return;
    }

    gen.writeStartObject();
    if (value instanceof IkeV2Ke) {
      IkeV2Ke ke = (IkeV2Ke) value;
      if (ke.getGroup() != null) {
        gen.writeNumberField("group", ke.getGroup());
      }
    } else {
      if (value.getName() != null) {
        gen.writeStringField("name", value.getName());
      }
      if (value instanceof IkeV2Enc) {
        IkeV2Enc enc = (IkeV2Enc) value;
        if (enc.getKeyLength() != null) {
          gen.writeNumberField("keyLength", enc.getKeyLength());
        }
      }
    }
    if (value.getAlgorithm() != null) {
      gen.writeStringField("algorithm", value.getAlgorithm());
    }
    gen.writeEndObject();
  }
}
