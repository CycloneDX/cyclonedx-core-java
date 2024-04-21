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
import org.cyclonedx.model.license.Expression;

public class LicenseChoiceSerializer
    extends StdSerializer<LicenseChoice>
{
  public LicenseChoiceSerializer() {
    this(LicenseChoice.class);
  }

  public LicenseChoiceSerializer(final Class<LicenseChoice> t) {
    super(t);
  }

  @Override
  public void serialize(
      final LicenseChoice licenseChoice, final JsonGenerator gen, final SerializerProvider provider)
      throws IOException
  {
    if (licenseChoice == null) {
      return;
    }

    if (gen instanceof ToXmlGenerator) {
      serializeXml(licenseChoice, gen, provider);
    }
    else {
      serializeJson(licenseChoice, gen, provider);
    }
  }

  private void serializeXml(
      final LicenseChoice licenseChoice, final JsonGenerator gen, final SerializerProvider provider)
      throws IOException
  {
      if (CollectionUtils.isNotEmpty(licenseChoice.getLicenses())) {
        serializeLicensesToJsonArray(licenseChoice, gen, provider);
      }
      else if (licenseChoice.getExpression() != null &&
          StringUtils.isNotBlank(licenseChoice.getExpression().getValue())) {
        final ToXmlGenerator toXmlGenerator = (ToXmlGenerator) gen;
        serializeExpressionToXml(licenseChoice, toXmlGenerator);
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

  private void serializeLicensesToJsonArray(
      final LicenseChoice licenseChoice, final JsonGenerator gen, final SerializerProvider provider)
      throws IOException {
    gen.writeStartArray();
    for (License license : licenseChoice.getLicenses()) {
      gen.writeStartObject();
      provider.defaultSerializeField("license", license, gen);
      gen.writeEndObject();
    }
    gen.writeEndArray();
  }

  private void serializeExpressionToXml(
      final LicenseChoice licenseChoice, final ToXmlGenerator toXmlGenerator)
      throws IOException {
    toXmlGenerator.writeStartObject();
    Expression expression = licenseChoice.getExpression();
    toXmlGenerator.writeFieldName("expression");

    toXmlGenerator.writeStartObject();

    if (StringUtils.isNotBlank(expression.getBomRef())) {
      toXmlGenerator.setNextIsAttribute(true);
      toXmlGenerator.writeFieldName("bom-ref");
      toXmlGenerator.writeString(expression.getBomRef());
      toXmlGenerator.setNextIsAttribute(false);
    }
    if (StringUtils.isNotBlank(expression.getAcknowledgement())) {
      toXmlGenerator.setNextIsAttribute(true);
      toXmlGenerator.writeFieldName("acknowledgement");
      toXmlGenerator.writeString(expression.getAcknowledgement());
      toXmlGenerator.setNextIsAttribute(false);
    }

    toXmlGenerator.setNextIsUnwrapped(true);
    toXmlGenerator.writeStringField("", expression.getValue());
    toXmlGenerator.writeEndObject();
    toXmlGenerator.writeEndObject();
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
