package org.cyclonedx.util.serializer;

import java.io.IOException;

import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.model.Hash;

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
}
