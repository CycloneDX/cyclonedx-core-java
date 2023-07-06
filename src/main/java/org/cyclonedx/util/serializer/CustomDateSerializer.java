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

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@SuppressWarnings("unused")
public class CustomDateSerializer extends StdSerializer<Date>
{
  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset

  public CustomDateSerializer() {
    this(null);
  }

  public CustomDateSerializer(final Class<Date> t) {
    super(t);
  }

  @Override
  public void serialize(
      final Date date, final JsonGenerator generator, final SerializerProvider provider)
      throws IOException
  {
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    generator.writeString(dateFormat.format(date));
  }
}
