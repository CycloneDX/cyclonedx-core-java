package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.cyclonedx.model.Hash;

public class HashesDeserializer
    extends JsonDeserializer<List<Hash>>
{
  @Override
  public List<Hash> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);
    if (node.has("hash")) {
      return parseHashes(node.get("hash"));
    }
    else {
      return parseHashes(node);
    }
  }

  private List<Hash> parseHashes(JsonNode node) {
    if (node.isEmpty()) {
      return Collections.emptyList();
    }

    List<Hash> hashes = new ArrayList<>();
    ArrayNode nodes = (node.isArray() ? (ArrayNode) node : new ArrayNode(null).add(node));

    for (JsonNode resolvesNode : nodes) {
      Hash hash = parseHash(resolvesNode);
      hashes.add(hash);
    }
    return hashes;
  }

  private Hash parseHash(JsonNode node) {
    String alg = null;
    if (node.has("alg")) {
      alg = node.get("alg").asText();
    }

    String value = null;
    JsonNode valueNode = node.get("content");
    if (valueNode != null) {
      value = valueNode.textValue();
    }
    else if (node.has("")) {
      value = node.get("").asText();
    }

    return new Hash(alg, value);
  }
}