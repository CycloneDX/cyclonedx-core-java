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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.license.Expression;

public class LicenseChoiceSerializer
    extends StdSerializer<LicenseChoice>
{
  private boolean isXml;

  public LicenseChoiceSerializer(final boolean isXml) {
    this(LicenseChoice.class, isXml);
    this.isXml = isXml;
  }

  public LicenseChoiceSerializer(final Class<LicenseChoice> t, boolean isXml) {
    super(t);
    this.isXml = isXml;
  }

  @Override
  public void serialize(
      final LicenseChoice licenseChoice, final JsonGenerator gen, final SerializerProvider provider)
      throws IOException
  {
    if (licenseChoice == null) {
      return;
    }

    if (isXml && gen instanceof ToXmlGenerator) {
      ToXmlGenerator toXmlGenerator = (ToXmlGenerator) gen;
      serializeXml(toXmlGenerator, licenseChoice);
    }
    else {
      serializeJson(licenseChoice, gen, provider);
    }
  }

    private void serializeXml(ToXmlGenerator toXmlGenerator, LicenseChoice lc) throws IOException {
    if (CollectionUtils.isNotEmpty(lc.getLicenses())) {
      toXmlGenerator.writeStartObject();
      toXmlGenerator.writeFieldName("license");
      toXmlGenerator.writeStartArray();
      for (License l : lc.getLicenses()) {
        serializeXmlAttributes(toXmlGenerator, l.getBomRef(), l.getAcknowledgement());

        if (StringUtils.isNotBlank(l.getId())) {
          toXmlGenerator.writeStringField("id", l.getId());
        }
        else if (StringUtils.isNotBlank(l.getName())) {
          toXmlGenerator.writeStringField("name", l.getName());
        }

        if (l.getLicensing() != null) {
          toXmlGenerator.writeObjectField("licensing", l.getLicensing());
        }

        if (l.getAttachmentText() != null) {
          toXmlGenerator.writeObjectField("text", l.getAttachmentText());
        }

        if (StringUtils.isNotBlank(l.getUrl())) {
          toXmlGenerator.writeStringField("url", l.getUrl());
        }

        if (CollectionUtils.isNotEmpty(l.getProperties())) {
          toXmlGenerator.writeFieldName("properties");
          toXmlGenerator.writeStartObject();

          for (Property property : l.getProperties()) {
            toXmlGenerator.writeObjectField("property", property);
          }
          toXmlGenerator.writeEndObject();
        }

        toXmlGenerator.writeEndObject();
      }
      toXmlGenerator.writeEndArray();
      toXmlGenerator.writeEndObject();
    }
    else if (lc.getExpression() != null) {
      serializeExpressionToXml(lc, toXmlGenerator);
    }
  }

  private void serializeXmlAttributes(
      final ToXmlGenerator toXmlGenerator,
      final String bomRef,
      final String acknowledgement) throws IOException
  {
    toXmlGenerator.writeStartObject();

    if (StringUtils.isNotBlank(bomRef)) {
      toXmlGenerator.setNextIsAttribute(true);
      toXmlGenerator.writeFieldName("bom-ref");
      toXmlGenerator.writeString(bomRef);
      toXmlGenerator.setNextIsAttribute(false);
    }
    if (StringUtils.isNotBlank(acknowledgement)) {
      toXmlGenerator.setNextIsAttribute(true);
      toXmlGenerator.writeFieldName("acknowledgement");
      toXmlGenerator.writeString(acknowledgement);
      toXmlGenerator.setNextIsAttribute(false);
    }
  }

  private void serializeJson(
      final LicenseChoice licenseChoice, final JsonGenerator gen, final SerializerProvider provider)
      throws IOException
  {
    if (CollectionUtils.isNotEmpty(licenseChoice.getLicenses())) {
      serializeLicensesToJsonArray(licenseChoice, gen, provider);
    }
    else if (licenseChoice.getExpression() != null &&
        StringUtils.isNotBlank(licenseChoice.getExpression().getValue())) {
      serializeExpressionToJson(licenseChoice, gen);
    }
  }

  private void serializeExpressionToXml(
      final LicenseChoice licenseChoice, final ToXmlGenerator toXmlGenerator)
      throws IOException
  {
    toXmlGenerator.writeStartObject();
    Expression expression = licenseChoice.getExpression();
    toXmlGenerator.writeFieldName("expression");
    serializeXmlAttributes(toXmlGenerator, expression.getBomRef(), expression.getAcknowledgement());
    toXmlGenerator.setNextIsUnwrapped(true);
    toXmlGenerator.writeStringField("", expression.getValue());
    toXmlGenerator.writeEndObject();
    toXmlGenerator.writeEndObject();
  }

  private void serializeLicensesToJsonArray(
      final LicenseChoice licenseChoice, final JsonGenerator gen, final SerializerProvider provider)
      throws IOException
  {
    gen.writeStartArray();
    for (License license : licenseChoice.getLicenses()) {
      gen.writeStartObject();
      provider.defaultSerializeField("license", license, gen);
      gen.writeEndObject();
    }
    gen.writeEndArray();
  }

  private void serializeExpressionToJson(final LicenseChoice licenseChoice, final JsonGenerator gen)
      throws IOException {
    Expression expression = licenseChoice.getExpression();
    gen.writeStartArray();
    gen.writeStartObject();
    gen.writeStringField("expression", expression.getValue());
    if (StringUtils.isNotBlank(expression.getAcknowledgement())) {
      gen.writeStringField("acknowledgement", expression.getAcknowledgement());
    }
    if (StringUtils.isNotBlank(expression.getBomRef())) {
      gen.writeStringField("bom-ref", expression.getBomRef());
    }
    gen.writeEndObject();
    gen.writeEndArray();
  }
}
