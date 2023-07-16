package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.cyclonedx.model.Property;

public class PropertiesDeserializer
    extends JsonDeserializer<List<Property>>
{
  private final PropertyDeserializer propertyDeserializer = new PropertyDeserializer();

  @Override
  public List<Property> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);
    List<Property> propertiesList = new ArrayList<>();

    JsonNode propertyNodeItem = node.get("property");
    if (propertyNodeItem.isArray()) {
      for (JsonNode propertyNode : propertyNodeItem) {
        propertiesList.add(parseProperty(propertyNode, p, ctxt));
      }
    } else {
      propertiesList.add(parseProperty(propertyNodeItem, p, ctxt));
    }
    return propertiesList;
  }

  private Property parseProperty(JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonParser propertyParser = node.traverse(p.getCodec());
    propertyParser.nextToken();
    return propertyDeserializer.deserialize(propertyParser, ctxt);
  }
}