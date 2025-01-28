package org.cyclonedx.util.serializer;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.formulation.common.EnvironmentVars;

public class EnvironmentVarsSerializer
    extends StdSerializer<EnvironmentVars>
{
  private final boolean isXml;

  public EnvironmentVarsSerializer(boolean isXml) {
    this(null, isXml);
  }

  public EnvironmentVarsSerializer(Class<EnvironmentVars> t, boolean isXml) {
    super(t);
    this.isXml = isXml;
  }

  @Override
  public void serialize(EnvironmentVars value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {

    List<Object> choices = value.getChoices();

    if (isXml && jsonGenerator instanceof ToXmlGenerator) {
      ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
      serializeXml(choices, xmlGenerator);
    } else {
      serializeJson(choices, jsonGenerator);
    }
  }

  private void serializeXml(List<Object> choices, ToXmlGenerator xmlGenerator) throws IOException {
    xmlGenerator.writeFieldName("environmentVars");
    if (choices.size() == 1 && choices.get(0) instanceof String) {
      xmlGenerator.writeStartObject();
      xmlGenerator.writeStringField("value", (String) choices.get(0));
      xmlGenerator.writeEndObject();
    } else {
      xmlGenerator.writeStartObject();
      for (Object choice : choices) {
        if (choice instanceof Property) {
          SerializerUtils.serializeProperty("environmentVar", (Property) choice, xmlGenerator);
        } else if (choice instanceof String) {
          xmlGenerator.writeFieldName("value");
          xmlGenerator.writeString((String) choice);
        }
      }
      xmlGenerator.writeEndObject();
    }
  }

  private void serializeJson(List<Object> choices, JsonGenerator jsonGenerator) throws IOException {
    jsonGenerator.writeFieldName("environmentVars");
    jsonGenerator.writeStartArray();
    if (choices.size() == 1 && choices.get(0) instanceof String) {
      jsonGenerator.writeString((String) choices.get(0));
    } else {

      for (Object choice : choices) {
        if (choice instanceof Property) {
          jsonGenerator.writeStartObject();
          Property property = (Property) choice;
          jsonGenerator.writeStringField("name", property.getName());
          jsonGenerator.writeStringField("value", property.getValue());
          jsonGenerator.writeEndObject();
        } else if (choice instanceof String) {
          jsonGenerator.writeString((String) choice);
        }
      }
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public Class<EnvironmentVars> handledType() {
    return EnvironmentVars.class;
  }
}