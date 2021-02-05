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
import org.cyclonedx.model.Dependency;

public class DependencySerializer extends StdSerializer<List<Dependency>>
{
  private final String NAMESPACE_PREFIX = "dg";
  private final String NAMESPACE_URI = "http://cyclonedx.org/schema/ext/dependency-graph/1.0";
  private boolean useNamespace = false;

  public DependencySerializer(final boolean useNamespace) {
    this(null);
    this.useNamespace = useNamespace;
  }

  public DependencySerializer(final Class t) {
    super(t);
  }

  @Override
  public void serialize(
      final List<Dependency> depList, final JsonGenerator generator, final SerializerProvider provider)
      throws IOException
  {
    final ToXmlGenerator toXmlGenerator = (ToXmlGenerator) generator;
    final XMLStreamWriter staxWriter = toXmlGenerator.getStaxWriter();
    try {
      if (depList != null && !depList.isEmpty()) {
        if (useNamespace) {
          staxWriter.writeStartElement(NAMESPACE_PREFIX, "dependencies", NAMESPACE_URI);
        } else {
          staxWriter.writeStartElement("dependencies");
        }
        for (Dependency d : depList) {
          writeDependency(d, staxWriter);
        }
      }
    }
    catch (XMLStreamException ex) {
      throw new IOException(ex);
    }
  }

  private void writeDependency(final Dependency dependency, final XMLStreamWriter writer) throws XMLStreamException {
    if (useNamespace) {
      writer.writeStartElement(NAMESPACE_PREFIX, "dependency", NAMESPACE_URI);
    } else {
      writer.writeStartElement( "dependency");
    }
    writer.writeAttribute("ref", dependency.getRef());
    if (dependency.getDependencies() != null && !dependency.getDependencies().isEmpty()) {
      for (Dependency dep : dependency.getDependencies()) {
        writeDependency(dep, writer);
      }
    }
    writer.writeEndElement();
  }
}
