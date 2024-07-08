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
import org.cyclonedx.model.Tool;

public class ToolsDeserializer
        extends JsonDeserializer<List<Tool>>
{
  private final ToolDeserializer toolDeserializer = new ToolDeserializer();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public List<Tool> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    return parseTools(node.has("tool") ? node.get("tool") : node, jsonParser, ctxt);
  }

  private List<Tool> parseTools(JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
    List<Tool> tools = new ArrayList<>();
    ArrayNode nodes = DeserializerUtils.getArrayNode(node, objectMapper);
    for (JsonNode toolNode : nodes) {
      tools.add(parseTool(toolNode, p, ctxt));
    }
    return tools;
  }

  private Tool parseTool(JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonParser toolParser = node.traverse(p.getCodec());
    toolParser.nextToken();
    return toolDeserializer.deserialize(toolParser, ctxt);
  }
}
