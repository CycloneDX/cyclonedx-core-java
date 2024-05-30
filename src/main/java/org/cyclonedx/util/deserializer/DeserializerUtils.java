package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class DeserializerUtils
{
  public static ArrayNode getArrayNode(JsonNode nodes, final ObjectMapper objectMapper) {
    if (objectMapper != null) {
      return nodes.isArray() ? (ArrayNode) nodes : new ArrayNode(objectMapper.getNodeFactory()).add(nodes);
    }
    else {
      return nodes.isArray() ? (ArrayNode) nodes : new ArrayNode(null).add(nodes);
    }
  }
}
