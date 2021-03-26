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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.model.Dependency;

public class DependencySerializer extends StdSerializer<List<Dependency>>
{
  private final String NAMESPACE_PREFIX = "dg";
  private final String DEPENDENCY = "dependency";
  private final String DEPENDENCIES = "dependencies";
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
      final List<Dependency> dependencies, final JsonGenerator generator, final SerializerProvider provider)
      throws IOException
  {
    if (generator instanceof ToXmlGenerator) {
      try {
        writeXMLDependencies((ToXmlGenerator) generator, dependencies);
      }
      catch (XMLStreamException e) {
        e.printStackTrace();
      }
    } else {
      if (dependencies != null && !dependencies.isEmpty()) {
        try {
          generator.writeStartArray();
          for (Dependency dependency : dependencies) {
            generator.writeStartObject();
            generator.writeStringField("ref", dependency.getRef());
            generator.writeArrayFieldStart("dependsOn");
            if (dependency.getDependencies() != null && !dependency.getDependencies().isEmpty()) {
              for (Dependency subDependency : dependency.getDependencies()) {
                generator.writeString(subDependency.getRef());
              }
            }
            generator.writeEndArray();
            generator.writeEndObject();
          }
          generator.writeEndArray();
        }
        catch (IOException ex) {
          throw new IOException(ex);
        }
      }
    }
  }

  private void writeXMLDependencies(final ToXmlGenerator toXmlGenerator, final List<Dependency> dependencies)
      throws IOException, XMLStreamException
  {
    writeXMLDependenciesWithGenerator(toXmlGenerator, dependencies);
  }

  private void writeXMLDependenciesWithGenerator(final ToXmlGenerator toXmlGenerator, final List<Dependency> dependencies)
      throws IOException, XMLStreamException
  {
    if (dependencies != null && !dependencies.isEmpty()) {
      QName qName;

      if (useNamespace) {
        qName = new QName(NAMESPACE_URI, DEPENDENCIES, NAMESPACE_PREFIX);
        toXmlGenerator.getStaxWriter().setPrefix(qName.getPrefix(), qName.getNamespaceURI());
      } else {
        qName = new QName(DEPENDENCIES);
      }

      toXmlGenerator.setNextName(qName);
      toXmlGenerator.writeStartObject();
      toXmlGenerator.writeFieldName(qName.getLocalPart());
      toXmlGenerator.writeStartArray();

      for (Dependency dependency : dependencies) {
        writeXMLDependency(dependency, toXmlGenerator);
      }

      toXmlGenerator.writeEndArray();
      toXmlGenerator.writeEndObject();
    }
  }

  private void writeXMLDependency(final Dependency dependency, final ToXmlGenerator generator)
      throws IOException, XMLStreamException
  {
    QName qName;
    if (useNamespace) {
      qName = new QName(NAMESPACE_URI, DEPENDENCY, NAMESPACE_PREFIX);
      generator.getStaxWriter().setPrefix(qName.getPrefix(), qName.getNamespaceURI());
    } else {
      qName = new QName(DEPENDENCY);
    }

    generator.setNextName(qName);

    generator.writeStartObject();
    generator.writeFieldName(qName.getLocalPart());

    if (dependency.getDependencies() != null && !dependency.getDependencies().isEmpty()) {
      generator.writeStartArray();
    }

    generator.setNextIsAttribute(true);
    generator.setNextName(new QName("ref"));
    generator.writeString(dependency.getRef());
    generator.setNextIsAttribute(false);

    if (dependency.getDependencies() != null && !dependency.getDependencies().isEmpty()) {
      for (Dependency subDependency : dependency.getDependencies()) {
        // You got Shay'd
        writeXMLDependency(subDependency, generator);
      }
    }

    if (dependency.getDependencies() != null && !dependency.getDependencies().isEmpty()) {
      generator.writeEndArray();
    }

    generator.writeEndObject();
  }
}
