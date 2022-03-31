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
  private final String REF = "ref";
  private final String NAMESPACE_URI = "http://cyclonedx.org/schema/ext/dependency-graph/1.0";
  private boolean useNamespace = false;

  public DependencySerializer(final boolean useNamespace) {
    this(null);
    this.useNamespace = useNamespace;
  }

  public DependencySerializer(final Class<List<Dependency>> t) {
    super(t);
  }

  @Override
  public void serialize(
      final List<Dependency> dependencies, final JsonGenerator generator, final SerializerProvider provider)
      throws IOException
  {
    try {
      if ((generator instanceof ToXmlGenerator)) {
        writeXMLDependenciesWithGenerator((ToXmlGenerator) generator,
            dependencies);
      }
      else {
        writeJSONDependenciesWithGenerator(generator, dependencies);
      }
    } catch (XMLStreamException | IOException ex) {
      throw new IOException(ex);
    }
  }

  private void writeJSONDependenciesWithGenerator(final JsonGenerator generator, final List<Dependency> dependencies) throws IOException {
    if (dependencies != null) {
      generator.writeStartArray();
      if (! dependencies.isEmpty()) {
        for (Dependency dependency : dependencies) {
          generator.writeStartObject();
          generator.writeStringField(REF, dependency.getRef());
          generator.writeArrayFieldStart("dependsOn");
          if (dependency.getDependencies() != null && !dependency.getDependencies().isEmpty()) {
            for (Dependency subDependency : dependency.getDependencies()) {
              generator.writeString(subDependency.getRef());
            }
          }
          generator.writeEndArray();
          generator.writeEndObject();
        }
      }
      generator.writeEndArray();
    }
  }

  private void writeXMLDependenciesWithGenerator(final ToXmlGenerator toXmlGenerator, final List<Dependency> dependencies)
      throws IOException, XMLStreamException
  {
    if (dependencies != null && !dependencies.isEmpty()) {
      processNamespace(toXmlGenerator, DEPENDENCIES);
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
    processNamespace(generator, DEPENDENCY);

    if (dependency.getDependencies() != null && !dependency.getDependencies().isEmpty()) {
      generator.writeStartArray();
    }

    generator.setNextIsAttribute(true);
    generator.setNextName(new QName(REF));
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

  private void processNamespace(final ToXmlGenerator toXmlGenerator, final String dependencies2)
      throws XMLStreamException, IOException
  {
    QName qName;

    if (useNamespace) {
      qName = new QName(NAMESPACE_URI, dependencies2, NAMESPACE_PREFIX);
      toXmlGenerator.getStaxWriter().setPrefix(qName.getPrefix(), qName.getNamespaceURI());
    } else {
      qName = new QName(dependencies2);
    }

    toXmlGenerator.setNextName(qName);
    toXmlGenerator.writeStartObject();
    toXmlGenerator.writeFieldName(qName.getLocalPart());
  }
}
