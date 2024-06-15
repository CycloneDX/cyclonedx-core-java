package org.cyclonedx.util.serializer;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.cyclonedx.model.formulation.common.AbstractType;
import org.cyclonedx.model.formulation.common.EnvVariableChoice;


public abstract class AbstractDataTypeSerializer<T extends AbstractType> extends StdSerializer<T>
{

  public AbstractDataTypeSerializer(Class<T> t) {
    super(t);
  }

  protected void parseEnvironmentVars(final JsonGenerator jsonGenerator, final List<EnvVariableChoice> vars)
      throws IOException
  {
    jsonGenerator.writeArrayFieldStart("environmentVars");
    for (EnvVariableChoice envVarChoice : vars) {
      if (envVarChoice.getEnvironmentVar() != null) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("environmentVar", envVarChoice.getEnvironmentVar());
        jsonGenerator.writeEndObject();
      }
      else if (envVarChoice.getValue() != null) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("value", envVarChoice.getValue());
        jsonGenerator.writeEndObject();
      }
    }
    jsonGenerator.writeEndArray();
  }

  protected void writeField(JsonGenerator jsonGenerator, String fieldName, Object fieldValue) throws IOException {
    if (fieldValue != null) {
      jsonGenerator.writeFieldName(fieldName);
      jsonGenerator.writeObject(fieldValue);
    }
  }

}
