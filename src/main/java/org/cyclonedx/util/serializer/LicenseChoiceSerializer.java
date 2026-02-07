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
import org.cyclonedx.Version;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.LicenseItem;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.license.Acknowledgement;
import org.cyclonedx.model.license.Expression;
import org.cyclonedx.model.license.ExpressionDetailed;
import org.cyclonedx.model.license.ExpressionDetail;

import static org.cyclonedx.util.serializer.SerializerUtils.shouldSerializeField;

public class LicenseChoiceSerializer
    extends StdSerializer<LicenseChoice>
{
  private final boolean isXml;

  private final Version version;

  public LicenseChoiceSerializer(final boolean isXml, final Version version) {
    this(LicenseChoice.class, isXml, version);
  }

  public LicenseChoiceSerializer(final Class<LicenseChoice> t, boolean isXml, final Version version) {
    super(t);
    this.isXml = isXml;
    this.version = version;
  }

  @Override
  public void serialize(
      final LicenseChoice licenseChoice, final JsonGenerator gen, final SerializerProvider provider)
      throws IOException
  {
    if (licenseChoice == null) {
      return;
    }

    // Note: We don't throw an exception for version incompatibility.
    // If a 1.7 BOM with mixed license types is being serialized to an earlier version,
    // we'll serialize what we can. The schema validation will catch any issues.

    if (isXml && gen instanceof ToXmlGenerator) {
      ToXmlGenerator toXmlGenerator = (ToXmlGenerator) gen;
      serializeXml(toXmlGenerator, licenseChoice, provider);
    }
    else {
      serializeJson(licenseChoice, gen, provider);
    }
  }

  private void serializeXml(ToXmlGenerator toXmlGenerator, LicenseChoice lc, final SerializerProvider provider)
      throws IOException
  {
    if (CollectionUtils.isEmpty(lc.getItems())) {
      toXmlGenerator.writeStartArray();
      toXmlGenerator.writeEndArray();
      return;
    }

    toXmlGenerator.writeStartObject();

    for (LicenseItem item : lc.getItems()) {
      if (item.getLicense() != null) {
        serializeLicenseToXml(toXmlGenerator, item.getLicense(), provider);
      } else if (item.getExpression() != null) {
        serializeExpressionToXml(toXmlGenerator, item.getExpression());
      } else if (item.getExpressionDetailed() != null) {
        serializeExpressionDetailedToXml(toXmlGenerator, item.getExpressionDetailed(), provider);
      }
    }

    toXmlGenerator.writeEndObject();
  }

  private void serializeLicenseToXml(ToXmlGenerator toXmlGenerator, License l, final SerializerProvider provider)
      throws IOException
  {
    toXmlGenerator.writeFieldName("license");
    toXmlGenerator.writeStartObject();
    serializeXmlAttributes(toXmlGenerator, l.getBomRef(), l.getAcknowledgement(), l);

    if (StringUtils.isNotBlank(l.getId())) {
      toXmlGenerator.writeStringField("id", l.getId());
    }
    else if (StringUtils.isNotBlank(l.getName())) {
      toXmlGenerator.writeStringField("name", l.getName());
    }

    if (l.getLicensing() != null && shouldSerializeField(l, version,"licensing")) {
      toXmlGenerator.writeObjectField("licensing", l.getLicensing());
    }

    if (l.getAttachmentText() != null) {
      toXmlGenerator.writeObjectField("text", l.getAttachmentText());
    }

    if (StringUtils.isNotBlank(l.getUrl())) {
      toXmlGenerator.writeStringField("url", l.getUrl());
    }

    if (CollectionUtils.isNotEmpty(l.getProperties()) && shouldSerializeField(l, version, "properties")) {
      toXmlGenerator.writeFieldName("properties");
      toXmlGenerator.writeStartObject();

      for (Property property : l.getProperties()) {
        toXmlGenerator.writeObjectField("property", property);
      }
      toXmlGenerator.writeEndObject();
    }

    //It might have extensible types
    if(CollectionUtils.isNotEmpty(l.getExtensibleTypes())) {
      new ExtensibleTypesSerializer().serialize(l.getExtensibleTypes(), toXmlGenerator, provider);
    }

    toXmlGenerator.writeEndObject();
  }

  private void serializeXmlAttributes(
      final ToXmlGenerator toXmlGenerator,
      final String bomRef,
      final Acknowledgement acknowledgement,
      final Object object) throws IOException
  {
    if (StringUtils.isNotBlank(bomRef) && shouldSerializeField(object, version, "bomRef")) {
      toXmlGenerator.setNextIsAttribute(true);
      toXmlGenerator.writeFieldName("bom-ref");
      toXmlGenerator.writeString(bomRef);
      toXmlGenerator.setNextIsAttribute(false);
    }
    if (acknowledgement != null && shouldSerializeField(object, version, "acknowledgement")) {
      toXmlGenerator.setNextIsAttribute(true);
      toXmlGenerator.writeFieldName("acknowledgement");
      toXmlGenerator.writeString(acknowledgement.getValue());
      toXmlGenerator.setNextIsAttribute(false);
    }
  }

  private void serializeJson(
      final LicenseChoice licenseChoice, final JsonGenerator gen, final SerializerProvider provider)
      throws IOException
  {
    if (CollectionUtils.isEmpty(licenseChoice.getItems())) {
      gen.writeStartArray();
      gen.writeEndArray();
      return;
    }

    gen.writeStartArray();
    for (LicenseItem item : licenseChoice.getItems()) {
      gen.writeStartObject();
      if (item.getLicense() != null) {
        provider.defaultSerializeField("license", item.getLicense(), gen);
      } else if (item.getExpression() != null) {
        serializeExpressionToJson(item.getExpression(), gen);
      } else if (item.getExpressionDetailed() != null) {
        serializeExpressionDetailedToJson(item.getExpressionDetailed(), gen, provider);
      }
      gen.writeEndObject();
    }
    gen.writeEndArray();
  }

  private void serializeExpressionToXml(
      final ToXmlGenerator toXmlGenerator, final Expression expression)
      throws IOException
  {
    toXmlGenerator.writeFieldName("expression");
    toXmlGenerator.writeStartObject();
    serializeXmlAttributes(toXmlGenerator, expression.getBomRef(), expression.getAcknowledgement(), expression);
    toXmlGenerator.setNextIsUnwrapped(true);
    toXmlGenerator.writeStringField("", expression.getValue());
    toXmlGenerator.writeEndObject();
  }

  private void serializeExpressionDetailedToXml(
      final ToXmlGenerator toXmlGenerator,
      final ExpressionDetailed expressionDetailed,
      final SerializerProvider provider)
      throws IOException
  {
    if (version.getVersion() < 1.7) {
      return; // ExpressionDetailed is only for 1.7+
    }

    toXmlGenerator.writeFieldName("expression-detailed");
    toXmlGenerator.writeStartObject();

    // Write expression as an attribute (required)
    if (StringUtils.isNotBlank(expressionDetailed.getExpression())) {
      toXmlGenerator.setNextIsAttribute(true);
      toXmlGenerator.writeFieldName("expression");
      toXmlGenerator.writeString(expressionDetailed.getExpression());
      toXmlGenerator.setNextIsAttribute(false);
    }

    // Write other attributes (bom-ref, acknowledgement)
    serializeXmlAttributes(toXmlGenerator, expressionDetailed.getBomRef(), expressionDetailed.getAcknowledgement(), expressionDetailed);

    if (CollectionUtils.isNotEmpty(expressionDetailed.getExpressionDetails())) {
      for (ExpressionDetail detail : expressionDetailed.getExpressionDetails()) {
        toXmlGenerator.writeObjectField("details", detail);
      }
    }

    if (expressionDetailed.getLicensing() != null && shouldSerializeField(expressionDetailed, version, "licensing")) {
      toXmlGenerator.writeObjectField("licensing", expressionDetailed.getLicensing());
    }

    if (CollectionUtils.isNotEmpty(expressionDetailed.getProperties()) && shouldSerializeField(expressionDetailed, version, "properties")) {
      toXmlGenerator.writeFieldName("properties");
      toXmlGenerator.writeStartObject();
      for (Property property : expressionDetailed.getProperties()) {
        toXmlGenerator.writeObjectField("property", property);
      }
      toXmlGenerator.writeEndObject();
    }

    toXmlGenerator.writeEndObject();
  }

  private void serializeExpressionToJson(final Expression expression, final JsonGenerator gen)
      throws IOException {
    gen.writeStringField("expression", expression.getValue());
    if (expression.getAcknowledgement() != null && shouldSerializeField(expression, version, "acknowledgement")) {
      gen.writeStringField("acknowledgement", expression.getAcknowledgement().getValue());
    }
    if (StringUtils.isNotBlank(expression.getBomRef()) && shouldSerializeField(expression, version, "bomRef")) {
      gen.writeStringField("bom-ref", expression.getBomRef());
    }
  }

  private void serializeExpressionDetailedToJson(
      final ExpressionDetailed expressionDetailed, final JsonGenerator gen, final SerializerProvider provider)
      throws IOException {
    if (version.getVersion() < 1.7) {
      return; // ExpressionDetailed is only for 1.7+
    }

    // Flatten the expressionDetailed fields into the license item object
    if (StringUtils.isNotBlank(expressionDetailed.getBomRef())) {
      gen.writeStringField("bom-ref", expressionDetailed.getBomRef());
    }
    if (expressionDetailed.getAcknowledgement() != null) {
      gen.writeObjectField("acknowledgement", expressionDetailed.getAcknowledgement());
    }
    if (StringUtils.isNotBlank(expressionDetailed.getExpression())) {
      gen.writeStringField("expression", expressionDetailed.getExpression());
    }
    if (CollectionUtils.isNotEmpty(expressionDetailed.getExpressionDetails())) {
      gen.writeObjectField("expressionDetails", expressionDetailed.getExpressionDetails());
    }
    if (expressionDetailed.getLicensing() != null) {
      gen.writeObjectField("licensing", expressionDetailed.getLicensing());
    }
    if (CollectionUtils.isNotEmpty(expressionDetailed.getProperties())) {
      gen.writeObjectField("properties", expressionDetailed.getProperties());
    }
  }
}
