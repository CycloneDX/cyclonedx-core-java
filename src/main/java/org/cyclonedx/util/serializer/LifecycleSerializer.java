package org.cyclonedx.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.Version;
import org.cyclonedx.model.LifecycleChoice;
import org.cyclonedx.model.Lifecycles;

import java.io.IOException;
import java.util.List;

import static org.cyclonedx.util.serializer.SerializerUtils.shouldSerializeField;

public class LifecycleSerializer
    extends StdSerializer<Lifecycles>
{
  private final boolean isXml;

  private final Version version;

  public LifecycleSerializer(boolean isXml, Version version) {
    this(null, isXml, version);
  }

  public LifecycleSerializer(Class<Lifecycles> t, boolean isXml, Version version) {
    super(t);
    this.isXml = isXml;
    this.version = version;
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
      if (choice.getPhase() != null && shouldSerializeField(choice, version, "phase")) {
        jsonGenerator.writeStringField("phase", choice.getPhase().getPhaseName());
      } else {
        if (choice.getName() != null && shouldSerializeField(choice, version, "name")) {
          jsonGenerator.writeStringField("name", choice.getName());
        }
        if (choice.getDescription() != null && shouldSerializeField(choice, version, "description")) {
          jsonGenerator.writeStringField("description", choice.getDescription());
        }
      }
      jsonGenerator.writeEndObject();
    }
  }

  @Override
  public Class<Lifecycles> handledType() {
    return Lifecycles.class;
  }
}
