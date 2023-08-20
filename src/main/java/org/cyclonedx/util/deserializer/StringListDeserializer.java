package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class StringListDeserializer
    extends JsonDeserializer<List<String>>
{
  @Override
  public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);

    if (p.getCurrentName().equalsIgnoreCase("aliases")) {
      return deserializeList(node, "alias");
    }
    else if (p.getCurrentName().equalsIgnoreCase("tags")) {
      return deserializeList(node, "tag");
    }
    else if (p.getCurrentName().equalsIgnoreCase("endpoints")) {
      return deserializeList(node, "endpoint");
    }
    else if (p.getCurrentName().equalsIgnoreCase("altIds")) {
      return deserializeList(node, "altId");
    }
    return null;
  }

  private List<String> deserializeList(JsonNode node, String itemName) {
    List<String> list = new ArrayList<>();

    if(node.has(itemName)) {
      node = node.get(itemName);
    }

    if (node != null) {
      if (node.isArray()) {
        for (JsonNode nodeObject : node) {
          list.add(nodeObject.asText());
        }
      } else {
        list.add(node.asText());
      }
    }

    return list;
  }
}