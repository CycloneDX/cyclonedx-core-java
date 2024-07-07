package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.cyclonedx.model.vulnerability.Vulnerability;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AffectsDeserializer
    extends JsonDeserializer<List<Vulnerability.Affect>>
{
  private final AffectDeserializer affectDeserializer = new AffectDeserializer();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public List<Vulnerability.Affect> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);
    return parseAffects(node.has("target") ? node.get("target") : node, p, ctxt);
  }

  private List<Vulnerability.Affect> parseAffects(JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
    List<Vulnerability.Affect> affects = new ArrayList<>();
    ArrayNode nodes = DeserializerUtils.getArrayNode(node, objectMapper);

    for (JsonNode affectNode : nodes) {
      affects.add(parseAffect(affectNode, p, ctxt));
    }

    return affects;
  }

  private Vulnerability.Affect parseAffect(JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonParser affectParser = node.traverse(p.getCodec());
    affectParser.nextToken();
    return affectDeserializer.deserialize(affectParser, ctxt);
  }
}