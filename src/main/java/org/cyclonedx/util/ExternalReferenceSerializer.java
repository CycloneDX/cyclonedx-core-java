package org.cyclonedx.util;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.model.ExternalReference;

public class ExternalReferenceSerializer extends StdSerializer<ExternalReference>
{
  public ExternalReferenceSerializer() {
    this(null);
  }

  public ExternalReferenceSerializer(final Class t) {
    super(t);
  }

  @Override
  public void serialize(
      final ExternalReference extRef, final JsonGenerator gen, final SerializerProvider provider) throws IOException
  {
    final ToXmlGenerator toXmlGenerator = (ToXmlGenerator) gen;
    final XMLStreamWriter staxWriter = toXmlGenerator.getStaxWriter();
    if (extRef.getType() != null && extRef.getUrl() != null && BomUtils.validateUrlString(extRef.getUrl())) {
      try {
        staxWriter.writeStartElement("reference");
        staxWriter.writeAttribute("type", extRef.getType().getTypeName());
        staxWriter.writeStartElement("url");
        staxWriter.writeCharacters(extRef.getUrl());
        staxWriter.writeEndElement();
        if (extRef.getComment() != null) {
          staxWriter.writeStartElement("comment");
          staxWriter.writeCharacters(extRef.getComment());
          staxWriter.writeEndElement();
        }
        staxWriter.writeEndElement();
      } catch (XMLStreamException ex) {
        throw new IOException(ex);
      }
    }
  }
}
