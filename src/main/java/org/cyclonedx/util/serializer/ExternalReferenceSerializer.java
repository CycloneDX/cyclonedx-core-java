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
import java.util.function.BiPredicate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.ExternalReference.Type;
import org.cyclonedx.model.Hash;
import org.cyclonedx.util.BomUtils;

public class ExternalReferenceSerializer
    extends StdSerializer<ExternalReference>
{
  public ExternalReferenceSerializer() {
    this(null);
  }

  public ExternalReferenceSerializer(final Class<ExternalReference> t) {
    super(t);
  }

  @Override
  public void serialize(
      final ExternalReference extRef, final JsonGenerator gen, final SerializerProvider provider) throws IOException
  {
    final BiPredicate<Type, String> validateExternalReference =
        (type, url) -> (type != null && url != null && BomUtils.validateUriString(url));

    if (!validateExternalReference.test(extRef.getType(), extRef.getUrl())) {
      return;
    }

    if (gen instanceof ToXmlGenerator) {
      serializeXml((ToXmlGenerator) gen, extRef);
    }
    else {
      serializeJson(gen, extRef);
    }
  }

  private void serializeXml(final ToXmlGenerator toXmlGenerator, final ExternalReference extRef) throws IOException {
    toXmlGenerator.writeStartObject();

    toXmlGenerator.setNextIsAttribute(true);
    toXmlGenerator.writeFieldName("type");
    toXmlGenerator.writeString(extRef.getType().getTypeName());
    toXmlGenerator.setNextIsAttribute(false);

    toXmlGenerator.writeStringField("url", extRef.getUrl());
    if (extRef.getComment() != null) {
      toXmlGenerator.writeStringField("comment", extRef.getComment());
    }
    if (CollectionUtils.isNotEmpty(extRef.getHashes())) {
      toXmlGenerator.writeFieldName("hashes");
      toXmlGenerator.writeStartObject();
      for (Hash hash : extRef.getHashes()) {
        toXmlGenerator.writeFieldName("hash");
        toXmlGenerator.writeStartObject();
        toXmlGenerator.setNextIsAttribute(true);
        toXmlGenerator.writeFieldName("alg");
        toXmlGenerator.writeString(hash.getAlgorithm());
        toXmlGenerator.setNextIsAttribute(false);

        toXmlGenerator.setNextIsUnwrapped(true);
        toXmlGenerator.writeStringField("", hash.getValue());

        toXmlGenerator.writeEndObject();
      }
      toXmlGenerator.writeEndObject();
    }
    toXmlGenerator.writeEndObject();
  }

  private void serializeJson(final JsonGenerator gen, final ExternalReference extRef) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("type", extRef.getType().getTypeName());
    gen.writeStringField("url", extRef.getUrl());
    if (extRef.getComment() != null) {
      gen.writeStringField("comment", extRef.getComment());
    }
    if (CollectionUtils.isNotEmpty(extRef.getHashes())) {
      gen.writeFieldName("hashes");
      gen.writeStartArray();
      for (Hash hash : extRef.getHashes()) {
        gen.writeStartObject();
        gen.writeStringField("alg", hash.getAlgorithm());
        gen.writeStringField("content", hash.getValue());
        gen.writeEndObject();
      }
      gen.writeEndArray();
    }
    gen.writeEndObject();
  }
}
