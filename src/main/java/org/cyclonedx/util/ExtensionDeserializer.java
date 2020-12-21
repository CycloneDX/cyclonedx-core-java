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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.cyclonedx.model.ExtensibleType;
import org.cyclonedx.model.Extension;
import org.cyclonedx.model.Extension.ExtensionType;
import org.cyclonedx.model.vulnerability.Rating;
import org.cyclonedx.model.vulnerability.Vulnerability1_0;
import org.cyclonedx.model.vulnerability.Vulnerability1_0.Advisory;
import org.cyclonedx.model.vulnerability.Vulnerability1_0.Cwe;
import org.cyclonedx.model.vulnerability.Vulnerability1_0.Recommendation;
import org.cyclonedx.model.vulnerability.Vulnerability1_0.Score;
import org.cyclonedx.model.vulnerability.Vulnerability1_0.ScoreSource;
import org.cyclonedx.model.vulnerability.Vulnerability1_0.Severity;
import org.cyclonedx.model.vulnerability.Vulnerability1_0.Source;

public class ExtensionDeserializer extends StdDeserializer<Extension>
{
  private static final String VULNERABILITIES = "vulnerabilities";
  private static final String VULNERABILITY = "vulnerability";

  private ObjectMapper objectMapper;

  public ExtensionDeserializer() {
    this(Extension.class);
  }

  public ExtensionDeserializer(final Class vc) {
    super(vc);
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public Extension deserialize(
      final JsonParser p, final DeserializationContext ctxt) throws IOException
  {
    if (p.currentName().equals(VULNERABILITIES)) {
      if (p instanceof FromXmlParser) {
        return processVulnerabilities(p);
      }
    }
    return null;
  }

  private Extension processVulnerabilities(final JsonParser p) throws IOException {
    TreeNode on =  p.readValueAsTree();
    JsonNode vn = (JsonNode) on.get(VULNERABILITY);
    List<ExtensibleType> extList = new ArrayList<>();
    if (vn.isArray() && !vn.isEmpty()) {
      for (JsonNode jn : vn) {
        Vulnerability1_0 vuln = processVulnerability(jn);
        extList.add(vuln);
      }
    } else {
      Vulnerability1_0 vuln = processVulnerability(vn);
      extList.add(vuln);
    }
    if (!extList.isEmpty()) {
      return createAndReturnExtension(ExtensionType.VULNERABILITIES, extList);
    }
    return null;
  }

  private Vulnerability1_0 processVulnerability(final JsonNode vulnJson) {
    Vulnerability1_0 vuln = new Vulnerability1_0();
    for (Iterator<String> it = vulnJson.fieldNames(); it.hasNext(); ) {
      String field = it.next();
      switch (field) {
        case "ref":
          vuln.setRef(vulnJson.get(field).textValue());
          break;
        case "id":
          vuln.setId(vulnJson.get(field).textValue());
          break;
        case "source":
          vuln.setSource(processSource(vulnJson.get(field)));
          break;
        case "ratings":
          vuln.setRatings(processRatings(vulnJson.get(field)));
          break;
        case "cwes":
          vuln.setCwes(processCwes(vulnJson.get(field)));
          break;
        case "description":
          vuln.setDescription(vulnJson.get(field).textValue());
          break;
        case "recommendations":
          vuln.setRecommendations(processRecommendations(vulnJson.get(field)));
          break;
        case "advisories":
          vuln.setAdvisories(processAdvisories(vulnJson.get(field)));
          break;
        default:
          // Can't do anything so don't do anything
          break;
      }
    }
    return vuln;
  }

  private List<Advisory> processAdvisories(final JsonNode advisories) {
    List<Advisory> advisoryList = new ArrayList<>();
    JsonNode adv = advisories.get("advisory");
    if (adv.isArray() && !adv.isEmpty()) {
      for (JsonNode a : adv) {
        advisoryList.add(processAdvisory(a));
      }
    } else {
      advisoryList.add(processAdvisory(adv));
    }
    return advisoryList.isEmpty() ? null : advisoryList;
  }

  private Advisory processAdvisory(final JsonNode advisory) {
    Advisory adv = new Advisory();
    adv.setText(advisory.textValue());
    return adv;
  }

  private List<Recommendation> processRecommendations(final JsonNode recommendations) {
    List<Recommendation> recommendationsList = new ArrayList<>();
    JsonNode rec = recommendations.get("recommendation");
    if (rec.isArray() && !rec.isEmpty()) {
      for (JsonNode r : rec) {
        recommendationsList.add(processRecommendation(r));
      }
    } else {
      recommendationsList.add(processRecommendation(rec));
    }
    return recommendationsList.isEmpty() ? null : recommendationsList;
  }

  private Recommendation processRecommendation(final JsonNode recommendation) {
    Recommendation rec = new Recommendation();
    rec.setText(recommendation.textValue());
    return rec;
  }

  private List<Cwe> processCwes(final JsonNode cwes) {
    List<Cwe> cweList = new ArrayList<>();
    JsonNode cwe = cwes.get("cwe");
    if (cwe.isArray() && !cwe.isEmpty()) {
      for (JsonNode c : cwe) {
        cweList.add(processCwe(c));
      }
    } else {
      processCwe(cwe);
    }
    return cweList.isEmpty() ? null : cweList;
  }

  private Cwe processCwe(final JsonNode cwe) {
    Cwe c = new Cwe();
    c.setText(Integer.valueOf(cwe.textValue()));
    return c;
  }

  private Vulnerability1_0.Source processSource(final JsonNode source) {
    Source sauce = new Source();
    sauce.setName(getAsString("name", source));
    if (source.get("url") != null) {
      try {
        sauce.setUrl(new URL(source.get("url").textValue()));
      }
      catch (MalformedURLException e) {
        // Should we throw an exception? Is this worth stopping things over?
        e.printStackTrace();
      }
    }
    return sauce;
  }

  private List<Rating> processRatings(final JsonNode ratings) {
    List<Rating> ratingsList = new ArrayList<>();
    if (ratings.isArray() && !ratings.isEmpty()) {
      for (JsonNode rating : ratings) {
        ratingsList.add(processRating(rating));
      }
    } else {
      ratingsList.add(processRating(ratings));
    }
    return ratingsList.isEmpty() ? null : ratingsList;
  }

  private Rating processRating(final JsonNode ratingNode) {
    Rating rating = new Rating();
    JsonNode r = ratingNode.get("rating");
    if (r.get("score") != null) {
      Score score = new Score();
      JsonNode s = r.get("score");
      score.setBase(getAsDouble("base", s));
      score.setImpact(getAsDouble("impact", s));
      score.setExploitability(getAsDouble("exploitability", s));
      rating.setScore(score);
    }
    rating.setSeverity(Severity.fromString(getAsString("severity", r)));
    rating.setMethod(ScoreSource.fromString(getAsString("method", r)));
    rating.setVector(getAsString("vector", r));

    return rating;
  }

  private Extension createAndReturnExtension(final ExtensionType extType, final List<ExtensibleType> list) {
    if (extType == ExtensionType.VULNERABILITIES) {
      Extension ext = new Extension(extType, list);

      ext.setNamespaceURI("http://cyclonedx.org/schema/ext/vulnerability/1.0");
      ext.setPrefix("v");

      return ext;
    }
    return null;
  }

  private Double getAsDouble(final String fieldName, final JsonNode node) {
    if (node.get(fieldName) != null) {
      return node.get(fieldName).asDouble();
    }
    return null;
  }

  private String getAsString(final String fieldName, final JsonNode node) {
    if (node.get(fieldName) != null) {
      return node.get(fieldName).textValue();
    }
    return null;
  }
}
