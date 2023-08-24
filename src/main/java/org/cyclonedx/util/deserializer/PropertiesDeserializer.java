package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.cyclonedx.model.Property;

public class PropertiesDeserializer
    extends JsonDeserializer<List<Property>>
{
  private final PropertyDeserializer propertyDeserializer = new PropertyDeserializer();

  @Override
  public List<Property> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);

    if (node.has("property")) {
      return parseProperties(node.get("property"), p, ctxt);
    }
    else {
      return parseProperties(node, p, ctxt);
    }
  }

  private List<Property> parseProperties(JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
    List<Property> properties = new ArrayList<>();
    ArrayNode nodes = (node.isArray() ? (ArrayNode) node : new ArrayNode(null).add(node));
    for (JsonNode resolvesNode : nodes) {
      Property type = parseProperty(resolvesNode, p, ctxt);
      properties.add(type);
    }
    return properties;
  }

  private Property parseProperty(JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonParser propertyParser = node.traverse(p.getCodec());
    propertyParser.nextToken();
    return propertyDeserializer.deserialize(propertyParser, ctxt);
  }
}