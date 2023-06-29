package org.cyclonedx.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.model.LifecycleChoice;
import org.cyclonedx.model.Lifecycles;

import java.io.IOException;
import java.util.List;

public class LifecycleSerializer
    extends StdSerializer<Lifecycles>
{
  private final boolean isXml;

  public LifecycleSerializer(boolean isXml) {
    this(null, isXml);
  }

  public LifecycleSerializer(Class<Lifecycles> t, boolean isXml) {
    super(t);
    this.isXml = isXml;
  }

  @Override
  public void serialize(Lifecycles lifecycles, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    if (isXml && jsonGenerator instanceof ToXmlGenerator) {
      ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
      xmlGenerator.writeStartObject();
      xmlGenerator.writeFieldName("lifecycle");
      xmlGenerator.writeStartArray();
      createLifecycleChoice(lifecycles, xmlGenerator);
      xmlGenerator.writeEndArray();
      xmlGenerator.writeEndObject();
    } else {
      jsonGenerator.writeStartArray();
      createLifecycleChoice(lifecycles, jsonGenerator);
      jsonGenerator.writeEndArray();
    }
  }

  private void createLifecycleChoice(final Lifecycles lifecycles, final JsonGenerator jsonGenerator)
      throws IOException {
    List<LifecycleChoice> lifecycleChoices = lifecycles.getLifecycleChoice();
    for (LifecycleChoice choice : lifecycleChoices) {
      jsonGenerator.writeStartObject();
      if (choice.getPhase() != null) {
        jsonGenerator.writeStringField("phase", choice.getPhase().getPhaseName());
      } else {
        jsonGenerator.writeStringField("name", choice.getName());
        jsonGenerator.writeStringField("description", choice.getDescription());
      }
      jsonGenerator.writeEndObject();
    }
  }

  @Override
  public Class<Lifecycles> handledType() {
    return Lifecycles.class;
  }
}
