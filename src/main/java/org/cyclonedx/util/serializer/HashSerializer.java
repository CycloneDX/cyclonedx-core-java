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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.Version;
import org.cyclonedx.model.Hash;
import org.cyclonedx.model.Hash.Algorithm;
import org.cyclonedx.model.VersionFilter;

public class HashSerializer
    extends StdSerializer<Hash>
{
  private final Version version;

  public HashSerializer(final Version version) {
    this(Hash.class, version);
  }

  public HashSerializer(final Class<Hash> t, final Version version) {
    super(t);
    this.version = version;
  }

  @Override
  public void serialize(
      final Hash hash, final JsonGenerator gen, final SerializerProvider provider) throws IOException
  {
    if (!shouldSerializeField(hash.getAlgorithm())) {
      return;
    }

    if (gen instanceof ToXmlGenerator) {
      SerializerUtils.serializeHashXml((ToXmlGenerator) gen, hash);
    }
    else {
      serializeJson(gen, hash);
    }
  }

  private void serializeJson(final JsonGenerator gen, final Hash hash)
      throws IOException
  {
    gen.writeStartObject();
    gen.writeStringField("alg", hash.getAlgorithm());
    gen.writeStringField("content", hash.getValue());
    gen.writeEndObject();
  }

  @Override
  public Class<Hash> handledType() {
    return Hash.class;
  }

  private boolean shouldSerializeField(String value) {
    try {
      Algorithm algorithm = Algorithm.fromSpec(value);
      VersionFilter filter = algorithm.getClass().getField(algorithm.name()).getAnnotation(VersionFilter.class);
      return filter == null || filter.value().getVersion() <= version.getVersion();
    }
    catch (NoSuchFieldException e) {
      return false;
    }
  }
}
