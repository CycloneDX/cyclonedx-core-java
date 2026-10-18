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
package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.cyclonedx.model.ExtensibleType;
import org.cyclonedx.model.Extension;
import org.cyclonedx.model.Extension.ExtensionType;
import org.cyclonedx.model.vulnerability.Rating;
import org.cyclonedx.model.vulnerability.Vulnerability10;
import org.cyclonedx.model.vulnerability.Vulnerability10.Advisory;
import org.cyclonedx.model.vulnerability.Vulnerability10.Cwe;
import org.cyclonedx.model.vulnerability.Vulnerability10.Recommendation;
import org.cyclonedx.model.vulnerability.Vulnerability10.Score;
import org.cyclonedx.model.vulnerability.Vulnerability10.ScoreSource;
import org.cyclonedx.model.vulnerability.Vulnerability10.Severity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.cyclonedx.util.deserializer.DeserializerUtils.readText;

public class ExtensionDeserializer extends StdDeserializer<Extension>
{
  public ExtensionDeserializer() {
    this(Extension.class);
  }

  public ExtensionDeserializer(final Class vc) {
    super(vc);
  }

  @Override
  @SuppressWarnings("deprecation")
  public Extension deserialize(
      final JsonParser p, final DeserializationContext ctxt) throws IOException
  {
    if (!(p instanceof FromXmlParser) || !Vulnerability10.VULNERABILITIES.equals(p.currentName())) {
      p.skipChildren();
      return null;
    }

    final List<ExtensibleType> vulnerabilities = new ArrayList<>();
    for (String field = firstField(p); field != null; field = p.nextFieldName()) {
      p.nextToken();
      if (Vulnerability10.NAME.equals(field)) {
        vulnerabilities.add(processVulnerability(p));
      } else {
        p.skipChildren();
      }
    }
    return vulnerabilities.isEmpty() ? null : createExtension(vulnerabilities);
  }

  @SuppressWarnings("deprecation")
  private Vulnerability10 processVulnerability(JsonParser p) throws IOException {
    final Vulnerability10 vuln = new Vulnerability10();
    for (String field = firstField(p); field != null; field = p.nextFieldName()) {
      p.nextToken();
      switch (field) {
        case Vulnerability10.REF:
          vuln.setRef(readText(p));
          break;
        case Vulnerability10.ID:
          vuln.setId(readText(p));
          break;
        case Vulnerability10.SOURCE:
          vuln.setSource(processSource(p));
          break;
        case Vulnerability10.RATINGS:
          vuln.setRatings(processRatings(p));
          break;
        case Vulnerability10.CWES:
          vuln.setCwes(processCwes(p));
          break;
        case Vulnerability10.DESCRIPTION:
          vuln.setDescription(readText(p));
          break;
        case Vulnerability10.RECOMMENDATIONS:
          vuln.setRecommendations(processRecommendations(p));
          break;
        case Vulnerability10.ADVISORIES:
          vuln.setAdvisories(processAdvisories(p));
          break;
        default:
          p.skipChildren();
          break;
      }
    }
    return vuln;
  }

  @SuppressWarnings("deprecation")
  private List<Advisory> processAdvisories(JsonParser p) throws IOException {
    final List<Advisory> advisories = new ArrayList<>();
    for (String text : readTexts(p, Vulnerability10.ADVISORY)) {
      final Advisory advisory = new Advisory();
      advisory.setText(text);
      advisories.add(advisory);
    }
    return advisories.isEmpty() ? null : advisories;
  }

  @SuppressWarnings("deprecation")
  private List<Recommendation> processRecommendations(JsonParser p) throws IOException {
    final List<Recommendation> recommendations = new ArrayList<>();
    for (String text : readTexts(p, Vulnerability10.RECOMMENDATION)) {
      final Recommendation recommendation = new Recommendation();
      recommendation.setText(text);
      recommendations.add(recommendation);
    }
    return recommendations.isEmpty() ? null : recommendations;
  }

  @SuppressWarnings("deprecation")
  private List<Cwe> processCwes(JsonParser p) throws IOException {
    final List<Cwe> cwes = new ArrayList<>();
    for (String text : readTexts(p, Vulnerability10.CWE)) {
      final Cwe cwe = new Cwe();
      try {
        cwe.setText(Integer.valueOf(text));
      } catch (NumberFormatException e) {
        // Not a CWE ID; leave unset.
      }
      cwes.add(cwe);
    }
    return cwes.isEmpty() ? null : cwes;
  }

  @SuppressWarnings("deprecation")
  private Vulnerability10.Source processSource(JsonParser p) throws IOException {
    final Vulnerability10.Source source = new Vulnerability10.Source();
    for (String field = firstField(p); field != null; field = p.nextFieldName()) {
      p.nextToken();
      if (Vulnerability10.SOURCE_NAME.equals(field)) {
        source.setName(readText(p));
      } else if (Vulnerability10.URL.equals(field)) {
        final String url = readText(p);
        try {
          source.setUrl(url == null ? null : new URL(url));
        }
        catch (MalformedURLException e) {
          // Should we throw an exception? Is this worth stopping things over?
          e.printStackTrace();
        }
      } else {
        p.skipChildren();
      }
    }
    return source;
  }

  @SuppressWarnings("deprecation")
  private List<Rating> processRatings(JsonParser p) throws IOException {
    final List<Rating> ratings = new ArrayList<>();
    for (String field = firstField(p); field != null; field = p.nextFieldName()) {
      p.nextToken();
      if (Vulnerability10.RATING.equals(field)) {
        ratings.add(processRating(p));
      } else {
        p.skipChildren();
      }
    }
    return ratings.isEmpty() ? null : ratings;
  }

  @SuppressWarnings("deprecation")
  private Rating processRating(JsonParser p) throws IOException {
    final Rating rating = new Rating();
    for (String field = firstField(p); field != null; field = p.nextFieldName()) {
      p.nextToken();
      switch (field) {
        case Vulnerability10.SCORE:
          rating.setScore(processScore(p));
          break;
        case Vulnerability10.SEVERITY:
          rating.setSeverity(Severity.fromString(readText(p)));
          break;
        case Vulnerability10.METHOD:
          rating.setMethod(ScoreSource.fromString(readText(p)));
          break;
        case Vulnerability10.VECTOR:
          rating.setVector(readText(p));
          break;
        default:
          p.skipChildren();
          break;
      }
    }
    return rating;
  }

  @SuppressWarnings("deprecation")
  private Score processScore(JsonParser p) throws IOException {
    final Score score = new Score();
    for (String field = firstField(p); field != null; field = p.nextFieldName()) {
      p.nextToken();
      switch (field) {
        case Vulnerability10.BASE:
          score.setBase(readDouble(p));
          break;
        case Vulnerability10.IMPACT:
          score.setImpact(readDouble(p));
          break;
        case Vulnerability10.EXPLOITABILITY:
          score.setExploitability(readDouble(p));
          break;
        default:
          p.skipChildren();
          break;
      }
    }
    return score;
  }

  @SuppressWarnings("deprecation")
  private Extension createExtension(List<ExtensibleType> list) {
    final Extension ext = new Extension(ExtensionType.VULNERABILITIES, list);
    ext.setNamespaceURI(Vulnerability10.NAMESPACE_URI);
    ext.setPrefix(Vulnerability10.PREFIX);
    return ext;
  }

  private static List<String> readTexts(JsonParser p, String itemName) throws IOException {
    final List<String> texts = new ArrayList<>();
    for (String field = firstField(p); field != null; field = p.nextFieldName()) {
      p.nextToken();
      if (itemName.equals(field)) {
        texts.add(readText(p));
      } else {
        p.skipChildren();
      }
    }
    return texts;
  }

  private static Double readDouble(JsonParser p) throws IOException {
    final String text = readText(p);
    return text == null ? null : p.getValueAsDouble();
  }

  private static String firstField(JsonParser p) throws IOException {
    if (p.currentToken() != JsonToken.START_OBJECT) {
      p.skipChildren();
      return null;
    }
    return p.nextFieldName();
  }
}
