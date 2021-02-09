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
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.model.Attribute;
import org.cyclonedx.model.ExtensibleType;

public class ExtensibleTypesSerializer extends StdSerializer<List<ExtensibleType>>
{
  private final String XMLNS = "xmlns";

  private final String DEFAULT_VALID_NAMESPACE = "http://www.w3.org/1999/xhtml";

  public ExtensibleTypesSerializer() {
    this(null);
  }

  public ExtensibleTypesSerializer(final Class t) {
    super(t);
  }

  @Override
  public void serialize(
      final List<ExtensibleType> extensibleTypes,
      final JsonGenerator generator,
      final SerializerProvider provider) throws IOException
  {
    final ToXmlGenerator toXmlGenerator = (ToXmlGenerator) generator;
    final XMLStreamWriter staxWriter = toXmlGenerator.getStaxWriter();
    try {
      if (extensibleTypes != null && !extensibleTypes.isEmpty()) {
        for (ExtensibleType ext : extensibleTypes) {
          if (ext.getAttributes() != null && !ext.getAttributes().isEmpty()) {
            Attribute xmlNS = ext.getAttributes().stream()
                .filter(a -> a.getKey().contains(XMLNS))
                .findAny()
                .orElse(null);

            if (xmlNS != null) {
              staxWriter.writeStartElement(ext.getNamespace(), ext.getName(), xmlNS.getValue());

              for (Attribute attr : ext.getAttributes()) {
                staxWriter.writeAttribute(attr.getKey(), attr.getValue());
              }
            }
          } else {
            staxWriter.writeStartElement(ext.getNamespace(), ext.getName(), DEFAULT_VALID_NAMESPACE);
          }

          if (ext.getExtensibleTypes() != null && !ext.getExtensibleTypes().isEmpty()) {
            serialize(ext.getExtensibleTypes(), generator, provider);
          }
          if (ext.getValue() != null) {
            staxWriter.writeCharacters(ext.getValue());
          }
          staxWriter.writeEndElement();
        }
      }
    }
    catch (XMLStreamException e) {
      throw new IOException(e);
    }
  }
}
