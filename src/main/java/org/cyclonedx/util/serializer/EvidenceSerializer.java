package org.cyclonedx.util.serializer;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.cyclonedx.Version;
import org.cyclonedx.model.Copyright;
import org.cyclonedx.model.Evidence;
import org.cyclonedx.model.VersionFilter;
import org.cyclonedx.model.component.evidence.Identity;
import org.cyclonedx.model.component.evidence.Occurrence;

public class EvidenceSerializer
    extends StdSerializer<Evidence>
{
  private final boolean isXml;

  private final Version version;

  public EvidenceSerializer(boolean isXml, Version version) {
    this(null, isXml, version);
  }

  public EvidenceSerializer(Class<Evidence> t, boolean isXml, Version version) {
    super(t);
    this.isXml = isXml;
    this.version = version;
  }

  @Override
  public void serialize(Evidence value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    if (isXml && jsonGenerator instanceof ToXmlGenerator) {
      ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
      serializeXml(xmlGenerator, value, serializerProvider);
    } else {
      serializeJson(jsonGenerator, value, serializerProvider);
    }
  }

  private void serializeXml(final ToXmlGenerator xmlGenerator, final Evidence evidence, SerializerProvider serializerProvider) throws IOException {
    xmlGenerator.writeStartObject();
    if (CollectionUtils.isNotEmpty(evidence.getIdentities()) && shouldSerializeField(evidence, "identities")) {
      if (version.getVersion() >= Version.VERSION_16.getVersion()) {
        xmlGenerator.writeFieldName("identity");
        xmlGenerator.writeStartArray();
        for (Identity identity : evidence.getIdentities()) {
          xmlGenerator.writeObject(identity);
        }
        xmlGenerator.writeEndArray();
      }
      else {
        xmlGenerator.writeObjectField("identity", evidence.getIdentities().get(0));
      }
    }

    if (CollectionUtils.isNotEmpty(evidence.getOccurrences()) && shouldSerializeField(evidence, "occurrences")) {
      xmlGenerator.writeFieldName("occurrences");
      xmlGenerator.writeStartObject(); // Start the occurrences object
      for (Occurrence occurrence : evidence.getOccurrences()) {
        xmlGenerator.writeFieldName("occurrence");
        xmlGenerator.writeObject(occurrence);
      }
      xmlGenerator.writeEndObject(); // End the occurrences object
    }

    serializeCommonInfo(xmlGenerator, evidence, serializerProvider);

    if (CollectionUtils.isNotEmpty(evidence.getCopyright()) && shouldSerializeField(evidence, "copyright")) {
      xmlGenerator.writeFieldName("copyright");
      xmlGenerator.writeStartObject();
      for (Copyright item : evidence.getCopyright()) {
        xmlGenerator.writeStringField("text", item.getText());
      }
      xmlGenerator.writeEndObject();
    }
    xmlGenerator.writeEndObject();
  }

  private void serializeJson(final JsonGenerator gen, final Evidence evidence, SerializerProvider serializerProvider) throws IOException {
    gen.writeStartObject();
    if (CollectionUtils.isNotEmpty(evidence.getIdentities()) && shouldSerializeField(evidence, "identities")) {
      if (version.getVersion() >= Version.VERSION_16.getVersion()) {
        gen.writeObjectField("identity", evidence.getIdentities());
      }
      else {
        gen.writeObjectField("identity", evidence.getIdentities().get(0));
      }
    }

    if (CollectionUtils.isNotEmpty(evidence.getOccurrences()) && shouldSerializeField(evidence, "occurrences")) {
      gen.writeObjectField("occurrences", evidence.getOccurrences());
    }

    serializeCommonInfo(gen, evidence, serializerProvider);

    if (CollectionUtils.isNotEmpty(evidence.getCopyright()) && shouldSerializeField(evidence, "copyright")) {
      gen.writeFieldName("copyright");
      gen.writeStartArray();
      for (Copyright item : evidence.getCopyright()) {
        gen.writeStartObject();
        gen.writeStringField("text", item.getText());
        gen.writeEndObject();
      }
      gen.writeEndArray();
    }
    gen.writeEndObject();
  }

  private void serializeCommonInfo(
      final JsonGenerator gen,
      final Evidence evidence,
      final SerializerProvider serializerProvider) throws IOException
  {
    if (evidence.getCallstack() != null && shouldSerializeField(evidence, "callstack")) {
      gen.writeObjectField("callstack", evidence.getCallstack());
    }

    if (evidence.getLicenses() != null && shouldSerializeField(evidence, "licenses")) {
      gen.writeFieldName("licenses");
      new LicenseChoiceSerializer(isXml, version).serialize(evidence.getLicenses(), gen, serializerProvider);
    }
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

  @Override
  public Class<Evidence> handledType() {
    return Evidence.class;
  }
}
