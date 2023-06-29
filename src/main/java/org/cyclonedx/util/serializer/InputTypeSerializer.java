package org.cyclonedx.util.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.model.formulation.common.EnvVariableChoice;
import org.cyclonedx.model.formulation.common.InputType;

public class InputTypeSerializer
    extends StdSerializer<InputType>
{
  private final boolean isXml;

  public InputTypeSerializer(boolean isXml) {
    this(null, isXml);
  }

  public InputTypeSerializer(Class<InputType> t, boolean isXml) {
    super(t);
    this.isXml = isXml;
  }

  @Override
  public void serialize(InputType value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    if (isXml && jsonGenerator instanceof ToXmlGenerator) {
      ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
      createInputChoice(value, xmlGenerator);
    } else {
      createInputChoice(value, jsonGenerator);
    }
  }

  private void createInputChoice(final InputType input, final JsonGenerator jsonGenerator)
      throws IOException
  {
    jsonGenerator.writeStartObject();

    if (input.getSource() != null) {
      jsonGenerator.writeFieldName("source");
      jsonGenerator.writeObject(input.getSource());
    }
    if (input.getTarget() != null) {
      jsonGenerator.writeFieldName("target");
      jsonGenerator.writeObject(input.getTarget());
    }

    if (input.getResource() != null) {
      jsonGenerator.writeFieldName("resource");
      jsonGenerator.writeObject( input.getResource());
    }
    else if (input.getParameters() != null && !input.getParameters().isEmpty()) {
      jsonGenerator.writeFieldName("parameters");
      jsonGenerator.writeObject( input.getParameters());
    }
    else if (input.getEnvironmentVars() != null && !input.getEnvironmentVars().isEmpty()) {
      jsonGenerator.writeArrayFieldStart("environmentVars");
      for (EnvVariableChoice envVarChoice : input.getEnvironmentVars()) {
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
    else if (input.getData() != null) {
      jsonGenerator.writeFieldName("data");
      jsonGenerator.writeObject( input.getData());
    }

    if (input.getProperties() != null) {
      jsonGenerator.writeFieldName("properties");
      jsonGenerator.writeObject( input.getProperties());
    }
    jsonGenerator.writeEndObject();
  }

  @Override
  public Class<InputType> handledType() {
    return InputType.class;
  }
}
