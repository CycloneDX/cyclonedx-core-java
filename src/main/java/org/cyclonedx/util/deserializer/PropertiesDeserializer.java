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
import org.cyclonedx.model.Property;

public class PropertiesDeserializer
    extends JsonDeserializer<List<Property>>
{
  private final PropertyDeserializer propertyDeserializer = new PropertyDeserializer();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public List<Property> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);
    return parseProperties(node.has("property") ? node.get("property") : node, p, ctxt);
  }

  private List<Property> parseProperties(JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
    List<Property> properties = new ArrayList<>();
    ArrayNode nodes = DeserializerUtils.getArrayNode(node, objectMapper);

    for (JsonNode propertyNode : nodes) {
      properties.add(parseProperty(propertyNode, p, ctxt));
    }

    return properties;
  }

  private Property parseProperty(JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonParser propertyParser = node.traverse(p.getCodec());
    propertyParser.nextToken();
    return propertyDeserializer.deserialize(propertyParser, ctxt);
  }
}