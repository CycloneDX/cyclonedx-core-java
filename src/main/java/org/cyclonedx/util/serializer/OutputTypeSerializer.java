package org.cyclonedx.util.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.model.formulation.common.OutputType;

public class OutputTypeSerializer
    extends StdSerializer<OutputType>
{
  private final boolean isXml;

  public OutputTypeSerializer(boolean isXml) {
    this(null, isXml);
  }

  public OutputTypeSerializer(Class<OutputType> t, boolean isXml) {
    super(t);
    this.isXml = isXml;
  }

  @Override
  public void serialize(OutputType value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    if (isXml && jsonGenerator instanceof ToXmlGenerator) {
      ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
      createOutputChoiceXml(value, xmlGenerator, serializerProvider);
    } else {
      createOutputChoiceJson(value, jsonGenerator, serializerProvider);
    }
  }

  private void createOutputChoiceJson(final OutputType output, final JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException
  {
    jsonGenerator.writeStartObject();

    if (output.getResource() != null) {
      jsonGenerator.writeFieldName("resource");
      jsonGenerator.writeObject( output.getResource());
    }
    else if (output.getEnvironmentVars() != null) {
      new EnvironmentVarsSerializer(isXml).serialize(output.getEnvironmentVars(), jsonGenerator, serializerProvider);
    }
    else if (output.getData() != null) {
      jsonGenerator.writeFieldName("data");
      jsonGenerator.writeObject( output.getData());
    }

    /*writeField(jsonGenerator, "type", output.getType());
    writeField(jsonGenerator, "source", output.getSource());
    writeField(jsonGenerator, "target", output.getTarget());
    writeField(jsonGenerator, "properties", output.getProperties());*/
    jsonGenerator.writeEndObject();
  }

  private void createOutputChoiceXml(final OutputType output, final ToXmlGenerator xmlGenerator, SerializerProvider serializerProvider)
      throws IOException
  {
    xmlGenerator.writeStartObject();

    if (output.getResource() != null) {
      xmlGenerator.writeFieldName("resource");
      xmlGenerator.writeObject( output.getResource());
    }
    else if (output.getEnvironmentVars() != null) {
      new EnvironmentVarsSerializer(isXml).serialize(output.getEnvironmentVars(), xmlGenerator, serializerProvider);
    }
    else if (output.getData() != null) {
      xmlGenerator.writeFieldName("data");
      xmlGenerator.writeObject( output.getData());
    }

    if (output.getType() != null) {
      xmlGenerator.writeFieldName("type");
      xmlGenerator.writeObject(output.getType());
    }
    if (output.getSource() != null) {
      xmlGenerator.writeFieldName("source");
      xmlGenerator.writeObject(output.getSource());
    }
    if (output.getTarget() != null) {
      xmlGenerator.writeFieldName("target");
      xmlGenerator.writeObject(output.getTarget());
    }
    if (output.getProperties() != null) {
      xmlGenerator.writeFieldName("properties");
      xmlGenerator.writeObject( output.getProperties());
    }
    xmlGenerator.writeEndObject();
  }

  protected void writeField(JsonGenerator jsonGenerator, String fieldName, Object fieldValue) throws IOException {
    if (fieldValue != null) {
      jsonGenerator.writeFieldName(fieldName);
      jsonGenerator.writeObject(fieldValue);
    }
  }

  private void createOutputChoiceXml(final OutputType output, final ToXmlGenerator xmlGenerator, SerializerProvider serializerProvider)
      throws IOException
  {
    xmlGenerator.writeStartObject();

    if (output.getResource() != null) {
      xmlGenerator.writeFieldName("resource");
      xmlGenerator.writeObject( output.getResource());
    }
    else if (output.getEnvironmentVars() != null) {
      new EnvironmentVarsSerializer(isXml).serialize(output.getEnvironmentVars(), xmlGenerator, serializerProvider);
    }
    else if (output.getData() != null) {
      xmlGenerator.writeFieldName("data");
      xmlGenerator.writeObject( output.getData());
    }

    if (output.getType() != null) {
      xmlGenerator.writeFieldName("type");
      xmlGenerator.writeObject(output.getType());
    }
    if (output.getSource() != null) {
      xmlGenerator.writeFieldName("source");
      xmlGenerator.writeObject(output.getSource());
    }
    if (output.getTarget() != null) {
      xmlGenerator.writeFieldName("target");
      xmlGenerator.writeObject(output.getTarget());
    }
    if (output.getProperties() != null) {
      xmlGenerator.writeFieldName("properties");
      xmlGenerator.writeObject( output.getProperties());
    }
    xmlGenerator.writeEndObject();
  }

  @Override
  public Class<OutputType> handledType() {
    return OutputType.class;
  }
}
