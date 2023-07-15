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
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.ExternalReference.Type;
import org.cyclonedx.model.Hash;
import org.cyclonedx.util.BomUtils;

public class ExternalReferenceSerializer extends StdSerializer<ExternalReference>
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
    final BiPredicate<Type, String> validateExternalReference = (type, url) -> (type != null && url != null && BomUtils.validateUriString(url));
    if (gen instanceof ToXmlGenerator) {
      final ToXmlGenerator toXmlGenerator = (ToXmlGenerator) gen;
      final XMLStreamWriter staxWriter = toXmlGenerator.getStaxWriter();

      if (validateExternalReference.test(extRef.getType(), extRef.getUrl())) {
        try {
          staxWriter.writeStartElement("reference");
          staxWriter.writeAttribute("type", extRef.getType().getTypeName());
          staxWriter.writeStartElement("url");
          staxWriter.writeCharacters(extRef.getUrl());
          staxWriter.writeEndElement();
          if (extRef.getComment() != null) {
            staxWriter.writeStartElement("comment");
            staxWriter.writeCharacters(extRef.getComment());
            staxWriter.writeEndElement();
          }
          if (extRef.getHashes() != null && !extRef.getHashes().isEmpty()) {
            staxWriter.writeStartElement("hashes");
            for (Hash hash : extRef.getHashes()) {
              if (hash != null) {
                staxWriter.writeStartElement("hash");
                staxWriter.writeAttribute("alg", hash.getAlgorithm());
                staxWriter.writeCharacters(hash.getValue());
                staxWriter.writeEndElement();
              }
            }
            staxWriter.writeEndElement();
          }
          staxWriter.writeEndElement();
        }
        catch (XMLStreamException ex) {
          throw new IOException(ex);
        }
      }
    } else if (validateExternalReference.test(extRef.getType(), extRef.getUrl())) {
      gen.writeStartObject();
      gen.writeStringField("type", extRef.getType().getTypeName());
      gen.writeStringField("url", extRef.getUrl());
      if (extRef.getComment() != null) {
        gen.writeStringField("comment", extRef.getComment());
      }
      if (extRef.getHashes() != null && !extRef.getHashes().isEmpty()) {
        gen.writePOJOField("hashes", extRef.getHashes());
      }
      gen.writeEndObject();
    }
  }
}
