package org.cyclonedx.util.serializer;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.Version;
import org.cyclonedx.model.Hash;
import org.cyclonedx.model.VersionFilter;

public class SerializerUtils
{
  public static void serializeHashXml(final ToXmlGenerator toXmlGenerator, final Hash hash) throws IOException {
    toXmlGenerator.writeStartObject();
    toXmlGenerator.setNextIsAttribute(true);
    toXmlGenerator.writeFieldName("alg");
    toXmlGenerator.writeString(hash.getAlgorithm());
    toXmlGenerator.setNextIsAttribute(false);
    toXmlGenerator.setNextIsUnwrapped(true);
    toXmlGenerator.writeStringField("", hash.getValue());
    toXmlGenerator.writeEndObject();
  }

  public static void serializeHashJson(final JsonGenerator gen, final Hash hash)
      throws IOException
  {
    gen.writeStartObject();
    gen.writeStringField("alg", hash.getAlgorithm());
    gen.writeStringField("content", hash.getValue());
    gen.writeEndObject();
  }

  public static boolean shouldSerializeField(Object obj, Version version, String fieldName) {
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
