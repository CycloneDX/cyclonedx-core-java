package org.cyclonedx.util.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.cyclonedx.Version;
import org.cyclonedx.model.formulation.common.InputType;

import static org.cyclonedx.util.serializer.SerializerUtils.shouldSerializeField;

public class InputTypeSerializer
    extends StdSerializer<InputType>
{
  private final boolean isXml;

  private final Version version;

  public InputTypeSerializer(boolean isXml, Version version) {
    this(null, isXml, version);
  }

  public InputTypeSerializer(Class<InputType> t, boolean isXml, Version version) {
    super(t);
    this.isXml = isXml;
    this.version = version;
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

    if (input.getResource() != null && shouldSerializeField(input, version, "resource")) {
      jsonGenerator.writeFieldName("resource");
      jsonGenerator.writeObject(input.getResource());
    }
    else if (CollectionUtils.isNotEmpty(input.getParameters()) && shouldSerializeField(input, version, "parameters")) {
      jsonGenerator.writeFieldName("parameters");
      jsonGenerator.writeObject(input.getParameters());
    }
    else if (input.getEnvironmentVars() != null && shouldSerializeField(input, version, "environmentVars")) {
      new EnvironmentVarsSerializer(isXml, version).serialize(input.getEnvironmentVars(), jsonGenerator, serializerProvider);
    }
    else if (input.getData() != null && shouldSerializeField(input, version, "data")) {
      jsonGenerator.writeFieldName("data");
      jsonGenerator.writeObject(input.getData());
    }

    if (input.getSource() != null && shouldSerializeField(input, version, "source")) {
      SerializerUtils.writeField(jsonGenerator, "source", input.getSource());
    }
    if (input.getTarget() != null && shouldSerializeField(input, version, "target")) {
      SerializerUtils.writeField(jsonGenerator, "target", input.getTarget());
    }
    if (input.getProperties() != null && shouldSerializeField(input, version, "properties")) {
      SerializerUtils.writeField(jsonGenerator, "properties", input.getProperties());
    }

    jsonGenerator.writeEndObject();
  }

  @Override
  public Class<InputType> handledType() {
    return InputType.class;
  }
}
