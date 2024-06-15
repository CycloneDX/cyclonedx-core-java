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
    return parseHashes(node.has("hash") ? node.get("hash") : node);
  }

  private List<Hash> parseHashes(JsonNode node) {
    if (node.isEmpty()) {
      return Collections.emptyList();
    }

    ArrayNode nodes = DeserializerUtils.getArrayNode(node, null);
    List<Hash> hashes = new ArrayList<>();

    for (JsonNode hashNode : nodes) {
      hashes.add(parseHash(hashNode));
    }
    return hashes;
  }

  private Hash parseHash(JsonNode node) {
    String alg = node.has("alg") ? node.get("alg").asText() : null;
    String value = node.has("content") ? node.get("content").asText() : node.has("") ? node.get("").asText() : null;

    return new Hash(alg, value);
  }
}