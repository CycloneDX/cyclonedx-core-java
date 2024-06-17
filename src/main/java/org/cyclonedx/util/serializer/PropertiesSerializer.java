package org.cyclonedx.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.cyclonedx.model.Property;

import java.io.IOException;
import java.util.List;

public class PropertiesSerializer
    extends JsonSerializer<List<Property>>
{
  private boolean isXml;

  public PropertiesSerializer(boolean isXml) {
    this.isXml = isXml;
  }

  public PropertiesSerializer() {
    // Default constructor
  }

  @Override
  public void serialize(List<Property> properties, JsonGenerator jsonGenerator, SerializerProvider serializers)
      throws IOException
  {
    if (CollectionUtils.isEmpty(properties)) {
      return; // Do not serialize if the list is null or empty
    }

    if (isXml) {
      serializeXml(properties, (ToXmlGenerator) jsonGenerator);
    }
    else {
      serializerJson(properties, jsonGenerator);
    }
  }

  private void serializerJson(List<Property> properties, JsonGenerator jsonGenerator) throws IOException {
    jsonGenerator.writeStartArray();
    for (Property property : properties) {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeObjectField("name", property.getName());
      jsonGenerator.writeObjectField("value", property.getValue());
      jsonGenerator.writeEndObject();
    }
    jsonGenerator.writeEndArray();
  }

  private static void serializeXml(final List<Property> properties, final ToXmlGenerator xmlGenerator)
      throws IOException
  {
    xmlGenerator.writeStartArray();
    for (Property property : properties) {
      xmlGenerator.writeStartObject("property");
      xmlGenerator.setNextIsAttribute(true);
      xmlGenerator.writeFieldName("name");
      xmlGenerator.writeString(property.getName());
      xmlGenerator.setNextIsAttribute(false);

      xmlGenerator.setNextIsUnwrapped(true);
      xmlGenerator.writeStringField("", property.getValue());
      xmlGenerator.writeEndObject();
    }
    xmlGenerator.writeEndArray();
  }

  @Override
  public Class<List<Property>> handledType() {
    return (Class<List<Property>>) (Class<?>) List.class;
  }
}
