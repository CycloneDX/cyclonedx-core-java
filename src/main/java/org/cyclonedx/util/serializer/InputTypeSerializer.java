package org.cyclonedx.util.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
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
      createInputChoice(value, xmlGenerator, serializerProvider);
    } else {
      createInputChoice(value, jsonGenerator, serializerProvider);
    }
  }

  private void createInputChoice(final InputType input, final JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException
  {
    jsonGenerator.writeStartObject();

    if (input.getResource() != null) {
      jsonGenerator.writeFieldName("resource");
      jsonGenerator.writeObject(input.getResource());
    }
    else if (input.getParameters() != null && !input.getParameters().isEmpty()) {
      jsonGenerator.writeFieldName("parameters");
      jsonGenerator.writeObject(input.getParameters());
    }
    else if (input.getEnvironmentVars() != null) {
      new EnvironmentVarsSerializer(isXml).serialize(input.getEnvironmentVars(), jsonGenerator, serializerProvider);
    }
    else if (input.getData() != null) {
      jsonGenerator.writeFieldName("data");
      jsonGenerator.writeObject(input.getData());
    }

    writeField(jsonGenerator, "source", input.getSource());
    writeField(jsonGenerator, "target", input.getTarget());
    writeField(jsonGenerator, "properties", input.getProperties());

    jsonGenerator.writeEndObject();
  }

  @Override
  public Class<InputType> handledType() {
    return InputType.class;
  }
}
