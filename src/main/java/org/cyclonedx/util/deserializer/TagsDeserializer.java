package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.cyclonedx.model.component.Tags;

public class TagsDeserializer
    extends JsonDeserializer<Tags>
{

  private final ObjectMapper mapper = new ObjectMapper();
  @Override
  public Tags deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);

    if(node.has("tag")) {
      return parseNode(node.get("tag"));
    } else {
      return parseNode(node);
    }
  }

  private Tags parseNode(JsonNode node) {
    List<String> list = new ArrayList<>();

    ArrayNode nodes = (node.isArray() ? (ArrayNode) node : new ArrayNode(null).add(node));
    for (JsonNode tagNode : nodes) {
      list.add(tagNode.asText());
    }

    if(!list.isEmpty()) {
      return new Tags(list);
    }
    return null;
  }
}