package org.cyclonedx.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cyclonedx.model.component.crypto.AbstractIkeV2Transform;
import org.cyclonedx.model.component.crypto.IkeV2Enc;
import org.cyclonedx.model.component.crypto.IkeV2Ke;

import java.io.IOException;

public class IkeV2TransformSerializer extends JsonSerializer<AbstractIkeV2Transform> {

  @Override
  public void serialize(AbstractIkeV2Transform value, JsonGenerator gen, SerializerProvider provider)
      throws IOException
  {
    if (value.isStringOnly()) {
      gen.writeString(value.getAlgorithm());
      return;
    }

    gen.writeStartObject();
    if (value instanceof IkeV2Ke) {
      IkeV2Ke ke = (IkeV2Ke) value;
      if (ke.getGroup() != null) {
        gen.writeNumberField("group", ke.getGroup());
      }
    } else {
      if (value.getName() != null) {
        gen.writeStringField("name", value.getName());
      }
      if (value instanceof IkeV2Enc) {
        IkeV2Enc enc = (IkeV2Enc) value;
        if (enc.getKeyLength() != null) {
          gen.writeNumberField("keyLength", enc.getKeyLength());
        }
      }
    }
    if (value.getAlgorithm() != null) {
      gen.writeStringField("algorithm", value.getAlgorithm());
    }
    gen.writeEndObject();
  }
}
