package org.cyclonedx.util.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.cyclonedx.Version;
import org.cyclonedx.model.Copyright;
import org.cyclonedx.model.Evidence;

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
      serializeJson(xmlGenerator, value, serializerProvider);
    } else {
      serializeJson(jsonGenerator, value, serializerProvider);
    }
  }

  private void serializeJson(final JsonGenerator gen, final Evidence evidence, SerializerProvider serializerProvider) throws IOException {
    gen.writeStartObject();
    if (CollectionUtils.isNotEmpty(evidence.getIdentities())) {
      if (version.getVersion() >= Version.VERSION_16.getVersion()) {
        gen.writeObjectField("identity", evidence.getIdentities());
      }
      else {
        gen.writeObjectField("identity", evidence.getIdentities().get(0));
      }
    }

    if (CollectionUtils.isNotEmpty(evidence.getOccurrences())) {
      gen.writeObjectField("occurrences", evidence.getOccurrences());
    }

    if (evidence.getCallstack() != null) {
      gen.writeObjectField("callstack", evidence.getCallstack());
    }

    if (evidence.getLicenseChoice() != null) {
      gen.writeFieldName("licenses");
      new LicenseChoiceSerializer().serialize(evidence.getLicenseChoice(), gen, serializerProvider);
    }

    if (CollectionUtils.isNotEmpty(evidence.getCopyright())) {
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

  @Override
  public Class<Evidence> handledType() {
    return Evidence.class;
  }
}
