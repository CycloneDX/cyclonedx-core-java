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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.model.ExtensibleType;
import org.cyclonedx.model.Extension;
import org.cyclonedx.model.Extension.ExtensionType;
import org.cyclonedx.model.vulnerability.Rating;
import org.cyclonedx.model.vulnerability.Vulnerability1_0;
import org.cyclonedx.model.vulnerability.Vulnerability1_0.Advisory;
import org.cyclonedx.model.vulnerability.Vulnerability1_0.Cwe;
import org.cyclonedx.model.vulnerability.Vulnerability1_0.Recommendation;

public class ExtensionSerializer
    extends StdSerializer<Extension>
{
  public ExtensionSerializer() {
    this(null);
  }

  public ExtensionSerializer(final Class t) {
    super(t);
  }

  @Override
  public void serialize(
      final Extension value, final JsonGenerator gen, final SerializerProvider provider) throws IOException
  {
    if (value.getExtensionType().equals(ExtensionType.VULNERABILITIES)) {
      if (gen instanceof ToXmlGenerator) {
        try {
          serializeVulnerabilities((ToXmlGenerator) gen, value);
        }
        catch (XMLStreamException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void serializeVulnerabilities(final ToXmlGenerator gen, final Extension vulns)
      throws XMLStreamException
  {
    final XMLStreamWriter staxWriter = gen.getStaxWriter();
    staxWriter.writeStartElement(
        vulns.getPrefix(),
        "vulnerabilities",
        vulns.getNamespaceURI());
    for (ExtensibleType ext : vulns.getExtensions()) {
      final Vulnerability1_0 vuln = (Vulnerability1_0) ext;
      staxWriter.writeStartElement(vulns.getPrefix(), "vulnerability", vulns.getNamespaceURI());
      staxWriter.writeAttribute("ref", vuln.getRef());
      generateTextNode(staxWriter, "id", vuln.getId(), vulns.getNamespaceURI(), vulns.getPrefix());
      if (vuln.getRatings() != null && !vuln.getRatings().isEmpty()) {
        staxWriter.writeStartElement(vulns.getPrefix(), "ratings", vulns.getNamespaceURI());
        for (Rating r : vuln.getRatings()) {
          staxWriter.writeStartElement(vulns.getPrefix(), "rating", vulns.getNamespaceURI());
          if (r.getScore() != null) {
            staxWriter.writeStartElement(vulns.getPrefix(), "score", vulns.getNamespaceURI());
            generateTextNode(staxWriter, "base", r.getScore().getBase().toString(), vulns.getNamespaceURI(), vulns.getPrefix());
            generateTextNode(staxWriter, "impact", r.getScore().getImpact().toString(), vulns.getNamespaceURI(), vulns.getPrefix());
            generateTextNode(staxWriter, "exploitability", r.getScore().getExploitability().toString(), vulns.getNamespaceURI(), vulns.getPrefix());
            staxWriter.writeEndElement();
          }
          generateTextNode(staxWriter, "severity", r.getSeverity().getSeverityName(), vulns.getNamespaceURI(), vulns.getPrefix());
          generateTextNode(staxWriter, "method", r.getMethod().getScoreSourceName(), vulns.getNamespaceURI(), vulns.getPrefix());
          generateTextNode(staxWriter, "vector", r.getVector(), vulns.getNamespaceURI(), vulns.getPrefix());
          staxWriter.writeEndElement();
        }
        staxWriter.writeEndElement();
      }
      if (vuln.getCwes() != null && !vuln.getCwes().isEmpty()) {
        staxWriter.writeStartElement(vulns.getPrefix(), "cwes", vulns.getNamespaceURI());
        for (Cwe c : vuln.getCwes()) {
          generateTextNode(staxWriter, "cwe", c.getText().toString(), vulns.getNamespaceURI(), vulns.getPrefix());
        }
        staxWriter.writeEndElement();
      }
      generateTextNode(staxWriter, "description", vuln.getDescription(), vulns.getNamespaceURI(), vulns.getPrefix());
      if (vuln.getRecommendations() != null && !vuln.getRecommendations().isEmpty()) {
        staxWriter.writeStartElement(vulns.getPrefix(), "recommendations", vulns.getNamespaceURI());
        for (Recommendation r : vuln.getRecommendations()) {
          generateTextNode(staxWriter, "recommendation", r.getText(), vulns.getNamespaceURI(), vulns.getPrefix());
        }
        staxWriter.writeEndElement();
      }
      if (vuln.getAdvisories() != null && !vuln.getAdvisories().isEmpty()) {
        staxWriter.writeStartElement(vulns.getPrefix(), "advisories", vulns.getNamespaceURI());
        for (Advisory a : vuln.getAdvisories()) {
          generateTextNode(staxWriter, "advisory", a.getText(), vulns.getNamespaceURI(), vulns.getPrefix());
        }
        staxWriter.writeEndElement();
      }
      staxWriter.writeEndElement();
    }
    staxWriter.writeEndElement();
  }


  private void generateTextNode(final XMLStreamWriter writer,
                                final String fieldName,
                                final String value,
                                final String namespaceUri,
                                final String prefix) throws XMLStreamException
  {
    writer.writeStartElement(prefix, fieldName, namespaceUri);
    writer.writeCharacters(value);
    writer.writeEndElement();
  }
}
