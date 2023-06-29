package org.cyclonedx.util.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.model.formulation.common.EnvVariableChoice;
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
      xmlGenerator.writeStartObject();
      xmlGenerator.writeFieldName("input");
      createOutputChoice(value, xmlGenerator);
      xmlGenerator.writeEndObject();
    } else {
      createOutputChoice(value, jsonGenerator);
    }
  }

  private void createOutputChoice(final OutputType output, final JsonGenerator jsonGenerator)
      throws IOException
  {
    jsonGenerator.writeStartObject();

    if (output.getSource() != null) {
      jsonGenerator.writeFieldName("source");
      jsonGenerator.writeObject(output.getSource());
    }
    if (output.getTarget() != null) {
      jsonGenerator.writeFieldName("target");
      jsonGenerator.writeObject(output.getTarget());
    }

    if (output.getResource() != null) {
      jsonGenerator.writeFieldName("resource");
      jsonGenerator.writeObject( output.getResource());
    }
    else if (output.getEnvironmentVars() != null && !output.getEnvironmentVars().isEmpty()) {
      jsonGenerator.writeArrayFieldStart("environmentVars");
      for (EnvVariableChoice envVarChoice : output.getEnvironmentVars()) {
        if (envVarChoice.getEnvironmentVar() != null) {
          jsonGenerator.writeStartObject();
          jsonGenerator.writeObjectField("environmentVar", envVarChoice.getEnvironmentVar());
          jsonGenerator.writeEndObject();
        } else if (envVarChoice.getValue() != null) {
          jsonGenerator.writeStartObject();
          jsonGenerator.writeObjectField("value", envVarChoice.getValue());
          jsonGenerator.writeEndObject();
        }
      }
      jsonGenerator.writeEndArray();
    }
    else if (output.getData() != null) {
      jsonGenerator.writeFieldName("data");
      jsonGenerator.writeObject( output.getData());
    }

    if (output.getProperties() != null) {
      jsonGenerator.writeFieldName("properties");
      jsonGenerator.writeObject( output.getProperties());
    }
    jsonGenerator.writeEndObject();
  }

  @Override
  public Class<OutputType> handledType() {
    return OutputType.class;
  }
}
