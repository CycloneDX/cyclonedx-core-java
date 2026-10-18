package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.List;

public class StringListDeserializer
    extends JsonDeserializer<List<String>>
{
  @Override
  public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    final String itemName = itemName(p.currentName());
    if (itemName == null) {
      p.skipChildren();
      return null;
    }
    return new WrappedListDeserializer<>(itemName, String.class).deserialize(p, ctxt);
  }

  private static String itemName(String fieldName) {
    if ("aliases".equalsIgnoreCase(fieldName)) {
      return "alias";
    } else if ("endpoints".equalsIgnoreCase(fieldName)) {
      return "endpoint";
    } else if ("altIds".equalsIgnoreCase(fieldName)) {
      return "altId";
    }
    return null;
  }
}
