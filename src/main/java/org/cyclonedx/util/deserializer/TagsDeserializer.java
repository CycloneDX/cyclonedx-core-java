package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.cyclonedx.model.component.Tags;

public class TagsDeserializer
    extends JsonDeserializer<Tags>
{
  @Override
  public Tags deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);
    return parseNode(node.has("tag") ? node.get("tag") : node);
  }

  private Tags parseNode(JsonNode node) {
    List<String> list = new ArrayList<>();

    ArrayNode nodes = DeserializerUtils.getArrayNode(node, null);
    for (JsonNode tagNode : nodes) {
      list.add(tagNode.asText());
    }

    return list.isEmpty() ? null : new Tags(list);
  }
}