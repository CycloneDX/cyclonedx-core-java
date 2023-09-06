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
import org.cyclonedx.model.ReleaseNotes.Resolves;
import org.cyclonedx.model.Source;

public class ResolvesDeserializer
    extends JsonDeserializer<List<Resolves>>
{
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public List<Resolves> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);

    if (node.has("issue")) {
      return parseResolvesNode(node.get("issue"));
    }
    else {
      return parseResolvesNode(node);
    }
  }

  private List<Resolves> parseResolvesNode(JsonNode node) {
    List<Resolves> resolvesList = new ArrayList<>();
    ArrayNode nodes = (node.isArray() ? (ArrayNode) node : new ArrayNode(null).add(node));

    for (JsonNode resolvesNode : nodes) {
      Resolves resolves = parseResolves(resolvesNode);
      resolvesList.add(resolves);
    }
    return resolvesList;
  }

  private Resolves parseResolves(JsonNode node) {
    Resolves resolves = new Resolves();

    if (node.has("type")) {
      resolves.setType(Resolves.Type.valueOf(node.get("type").asText().toUpperCase()));
    }

    if (node.has("id")) {
      resolves.setId(node.get("id").asText());
    }

    if (node.has("name")) {
      resolves.setName(node.get("name").asText());
    }

    if (node.has("description")) {
      resolves.setDescription(node.get("description").asText());
    }

    if (node.has("source")) {
      Source source = mapper.convertValue(node.get("source"), Source.class);
      resolves.setSource(source);
    }

    if (node.has("references")) {
      JsonNode referencesNode = node.get("references");
      List<String> references = new ArrayList<>();
      if (referencesNode.isArray()) {
        for (JsonNode refNode : referencesNode) {
          references.add(refNode.asText());
        }
      }
      else {
        references.add(referencesNode.get("url").asText());
      }
      resolves.setReferences(references);
    }
    return resolves;
  }
}