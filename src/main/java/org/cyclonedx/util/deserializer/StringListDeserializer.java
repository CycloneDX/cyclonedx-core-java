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
    String currentName = p.getCurrentName();

    if ("aliases".equalsIgnoreCase(currentName)) {
      return deserializeList(node, "alias");
    } else if ("endpoints".equalsIgnoreCase(currentName)) {
      return deserializeList(node, "endpoint");
    } else if ("altIds".equalsIgnoreCase(currentName)) {
      return deserializeList(node, "altId");
    }
    return null;
  }

  private List<String> deserializeList(JsonNode node, String itemName) {
    List<String> list = new ArrayList<>();

    JsonNode itemsNode = node.has(itemName) ? node.get(itemName) : node;

    if (itemsNode != null) {
      if (itemsNode.isArray()) {
        for (JsonNode itemNode : itemsNode) {
          list.add(itemNode.asText());
        }
      } else {
        list.add(itemsNode.asText());
      }
    }

    return list;
  }
}