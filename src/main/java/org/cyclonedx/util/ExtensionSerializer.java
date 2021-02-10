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
import org.cyclonedx.model.vulnerability.Vulnerability10;
import org.cyclonedx.model.vulnerability.Vulnerability10.Advisory;
import org.cyclonedx.model.vulnerability.Vulnerability10.Cwe;
import org.cyclonedx.model.vulnerability.Vulnerability10.Recommendation;

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
          throw new IOException(e);
        }
      }
    }
  }

  private void serializeVulnerabilities(final ToXmlGenerator gen, final Extension vulns)
      throws XMLStreamException
  {
    final XMLStreamWriter staxWriter = gen.getStaxWriter();
    staxWriter.writeStartElement(Vulnerability10.PREFIX, "vulnerabilities", Vulnerability10.NAMESPACE_URI);

    for (ExtensibleType ext : vulns.getExtensions()) {
      final Vulnerability10 vuln = (Vulnerability10) ext;
      staxWriter.writeStartElement(Vulnerability10.PREFIX, Vulnerability10.NAME, Vulnerability10.NAMESPACE_URI);
      staxWriter.writeAttribute("ref", vuln.getRef());

      generateTextNode(staxWriter, "id", vuln.getId(), Vulnerability10.NAMESPACE_URI, Vulnerability10.PREFIX);
      processRatings(vulns, staxWriter, vuln);
      processCwes(vulns, staxWriter, vuln);
      generateTextNode(staxWriter, "description", vuln.getDescription(), Vulnerability10.NAMESPACE_URI, Vulnerability10.PREFIX);
      processRecommendations(vulns, staxWriter, vuln);
      processAdvisories(vulns, staxWriter, vuln);

      staxWriter.writeEndElement();
    }

    staxWriter.writeEndElement();
  }

  private void processAdvisories(final Extension vulns, final XMLStreamWriter staxWriter, final Vulnerability10 vuln)
      throws XMLStreamException
  {
    if (vuln.getAdvisories() != null && !vuln.getAdvisories().isEmpty()) {
      staxWriter.writeStartElement(Vulnerability10.PREFIX, "advisories", Vulnerability10.NAMESPACE_URI);
      for (Advisory a : vuln.getAdvisories()) {
        generateTextNode(staxWriter, "advisory", a.getText(), Vulnerability10.NAMESPACE_URI, Vulnerability10.PREFIX);
      }
      staxWriter.writeEndElement();
    }
  }

  private void processRecommendations(final Extension vulns, final XMLStreamWriter staxWriter, final Vulnerability10 vuln)
      throws XMLStreamException
  {
    if (vuln.getRecommendations() != null && !vuln.getRecommendations().isEmpty()) {
      staxWriter.writeStartElement(Vulnerability10.PREFIX, "recommendations", Vulnerability10.NAMESPACE_URI);
      for (Recommendation r : vuln.getRecommendations()) {
        generateTextNode(staxWriter, "recommendation", r.getText(), Vulnerability10.NAMESPACE_URI, Vulnerability10.PREFIX);
      }
      staxWriter.writeEndElement();
    }
  }

  private void processCwes(final Extension vulns, final XMLStreamWriter staxWriter, final Vulnerability10 vuln)
      throws XMLStreamException
  {
    if (vuln.getCwes() != null && !vuln.getCwes().isEmpty()) {
      staxWriter.writeStartElement(Vulnerability10.PREFIX, "cwes", Vulnerability10.NAMESPACE_URI);
      for (Cwe c : vuln.getCwes()) {
        generateTextNode(staxWriter, "cwe", c.getText().toString(), Vulnerability10.NAMESPACE_URI, Vulnerability10.PREFIX);
      }
      staxWriter.writeEndElement();
    }
  }

  private void processRatings(final Extension vulns, final XMLStreamWriter staxWriter, final Vulnerability10 vuln)
      throws XMLStreamException
  {
    if (vuln.getRatings() != null && !vuln.getRatings().isEmpty()) {
      staxWriter.writeStartElement(Vulnerability10.PREFIX, "ratings", Vulnerability10.NAMESPACE_URI);
      for (Rating r : vuln.getRatings()) {
        staxWriter.writeStartElement(Vulnerability10.PREFIX, "rating", Vulnerability10.NAMESPACE_URI);
        if (r.getScore() != null) {
          staxWriter.writeStartElement(Vulnerability10.PREFIX, "score", Vulnerability10.NAMESPACE_URI);
          generateTextNode(staxWriter, "base", r.getScore().getBase().toString(), Vulnerability10.NAMESPACE_URI, Vulnerability10.PREFIX);
          generateTextNode(staxWriter, "impact", r.getScore().getImpact().toString(), Vulnerability10.NAMESPACE_URI, Vulnerability10.PREFIX);
          generateTextNode(staxWriter, "exploitability", r.getScore().getExploitability().toString(), Vulnerability10.NAMESPACE_URI, vulns
              .getPrefix());
          staxWriter.writeEndElement();
        }
        generateTextNode(staxWriter, "severity", r.getSeverity().getSeverityName(), Vulnerability10.NAMESPACE_URI,Vulnerability10.PREFIX);
        generateTextNode(staxWriter, "method", r.getMethod().getScoreSourceName(), Vulnerability10.NAMESPACE_URI, Vulnerability10.PREFIX);
        generateTextNode(staxWriter, "vector", r.getVector(), Vulnerability10.NAMESPACE_URI, Vulnerability10.PREFIX);
        staxWriter.writeEndElement();
      }
      staxWriter.writeEndElement();
    }
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
