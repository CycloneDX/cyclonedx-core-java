package org.cyclonedx.util.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cyclonedx.model.attestation.affirmation.Signatory;

public class SignatorySerializer
    extends StdSerializer<Signatory>
{
  private final boolean isXml;

  public SignatorySerializer(boolean isXml) {
    this(null, isXml);
  }

  public SignatorySerializer(Class<Signatory> t, boolean isXml) {
    super(t);
    this.isXml = isXml;
  }

  @Override
  public void serialize(Signatory value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    if (isXml && jsonGenerator instanceof ToXmlGenerator) {
      ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
      serializeXml(xmlGenerator, value, serializerProvider);
    } else {
      serializeJson(jsonGenerator, value);
    }
  }

  private void serializeXml(final ToXmlGenerator gen, final Signatory signatory, final SerializerProvider provider)
      throws IOException
  {
    //It might have extensible types (signature)
    if (CollectionUtils.isNotEmpty(signatory.getExtensibleTypes())) {
      gen.writeStartObject();

      if (StringUtils.isNotBlank(signatory.getName())) {
        gen.writeStringField("name", signatory.getName());
      }

      if (StringUtils.isNotBlank(signatory.getRole())) {
        gen.writeStringField("role", signatory.getRole());
      }

      new ExtensibleTypesSerializer().serialize(signatory.getExtensibleTypes(), gen, provider);
      gen.writeEndObject();
    }
  }

  private void serializeJson(final JsonGenerator gen, final Signatory signatory)
      throws IOException
  {
    boolean shouldSerialize = false;

    if (signatory.getSignature() != null && !isXml) {
      shouldSerialize = true;
    }
    else if (signatory.getExternalReference() != null && signatory.getOrganization() != null) {
      shouldSerialize = true;
    }

    // Only serialize if the required values are set
    if (shouldSerialize) {
      gen.writeStartObject();

      if (StringUtils.isNotBlank(signatory.getName())) {
        gen.writeStringField("name", signatory.getName());
      }

      if (StringUtils.isNotBlank(signatory.getRole())) {
        gen.writeStringField("role", signatory.getRole());
      }

      if (signatory.getSignature() != null) {
        gen.writeObjectField("signature", signatory.getSignature());
      }
      else if (signatory.getExternalReference() != null && signatory.getOrganization() != null) {
        gen.writeObjectField("organization", signatory.getOrganization());
        gen.writeObjectField("externalReference", signatory.getExternalReference());
      }
      gen.writeEndObject();
    }
  }

  @Override
  public Class<Signatory> handledType() {
    return Signatory.class;
  }
}
