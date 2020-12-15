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
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.cyclonedx.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;

public class LicenseChoiceSerializer extends StdSerializer<LicenseChoice>
{
  public LicenseChoiceSerializer() {
    this(LicenseChoice.class);
  }

  public LicenseChoiceSerializer(final Class t) {
    super(t);
  }

  @Override
  public void serialize(
      final LicenseChoice lc, final JsonGenerator gen, final SerializerProvider provider)
      throws IOException
  {
    gen.writeStartArray();
    if (lc != null && lc.getLicenses() != null && !lc.getLicenses().isEmpty()) {
      for (License l : lc.getLicenses()) {
        gen.writeStartObject();
        provider.defaultSerializeField("license", l, gen);
        gen.writeEndObject();
      }
    } else if (lc != null && lc.getExpression() != null) {
      gen.writeStartObject();
      gen.writeStringField("expression", lc.getExpression());
      gen.writeEndObject();
    }
    gen.writeEndArray();
  }
}
