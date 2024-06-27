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
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyclonedx.Version;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.VersionFilter;
import org.cyclonedx.model.license.Expression;

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
    if (CollectionUtils.isNotEmpty(lc.getLicenses())) {
      toXmlGenerator.writeStartObject();
      toXmlGenerator.writeFieldName("license");
      toXmlGenerator.writeStartArray();
      for (License l : lc.getLicenses()) {
        serializeXmlAttributes(toXmlGenerator, l.getBomRef(), l.getAcknowledgement(), l);

        if (StringUtils.isNotBlank(l.getId())) {
          toXmlGenerator.writeStringField("id", l.getId());
        }
        else if (StringUtils.isNotBlank(l.getName())) {
          toXmlGenerator.writeStringField("name", l.getName());
        }

        if (l.getLicensing() != null && shouldSerializeField(l, "licensing")) {
          toXmlGenerator.writeObjectField("licensing", l.getLicensing());
        }

        if (l.getAttachmentText() != null) {
          toXmlGenerator.writeObjectField("text", l.getAttachmentText());
        }

        if (StringUtils.isNotBlank(l.getUrl())) {
          toXmlGenerator.writeStringField("url", l.getUrl());
        }

        if (CollectionUtils.isNotEmpty(l.getProperties()) && shouldSerializeField(l, "properties")) {
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
      toXmlGenerator.writeEndArray();
      toXmlGenerator.writeEndObject();
    }
    else if (lc.getExpression() != null) {
      serializeExpressionToXml(lc, toXmlGenerator);
    } else {
      toXmlGenerator.writeStartArray();
      toXmlGenerator.writeEndArray();
    }
  }

  private void serializeXmlAttributes(
      final ToXmlGenerator toXmlGenerator,
      final String bomRef,
      final String acknowledgement,
      final Object object) throws IOException
  {
    toXmlGenerator.writeStartObject();

    if (StringUtils.isNotBlank(bomRef) && shouldSerializeField(object, "bomRef")) {
      toXmlGenerator.setNextIsAttribute(true);
      toXmlGenerator.writeFieldName("bom-ref");
      toXmlGenerator.writeString(bomRef);
      toXmlGenerator.setNextIsAttribute(false);
    }
    if (StringUtils.isNotBlank(acknowledgement) && shouldSerializeField(object, "acknowledgement")) {
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
    } else {
      gen.writeStartArray();
      gen.writeEndArray();
  }
  }

  private void serializeExpressionToXml(
      final LicenseChoice licenseChoice, final ToXmlGenerator toXmlGenerator)
      throws IOException
  {
    toXmlGenerator.writeStartObject();
    Expression expression = licenseChoice.getExpression();
    toXmlGenerator.writeFieldName("expression");
    serializeXmlAttributes(toXmlGenerator, expression.getBomRef(), expression.getAcknowledgement(), expression);
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
    if (StringUtils.isNotBlank(expression.getAcknowledgement()) &&
        shouldSerializeField(expression, "acknowledgement")) {
      gen.writeStringField("acknowledgement", expression.getAcknowledgement());
    }
    if (StringUtils.isNotBlank(expression.getBomRef()) && shouldSerializeField(expression, "bomRef")) {
      gen.writeStringField("bom-ref", expression.getBomRef());
    }
    gen.writeEndObject();
    gen.writeEndArray();
  }

  private boolean shouldSerialize(Object obj) {
    for (Field field : obj.getClass().getDeclaredFields()) {
      VersionFilter filter = field.getAnnotation(VersionFilter.class);
      if (filter != null && filter.value().getVersion() > version.getVersion()) {
        return false;
      }
    }
    return true;
  }

  private boolean shouldSerializeField(Object obj, String fieldName) {
    try {
      Field field = obj.getClass().getDeclaredField(fieldName);
      VersionFilter filter = field.getAnnotation(VersionFilter.class);
      return filter == null || filter.value().getVersion() <= version.getVersion();
    } catch (NoSuchFieldException e) {
      // If the field does not exist, assume it should be serialized
      return true;
    }
  }
}
