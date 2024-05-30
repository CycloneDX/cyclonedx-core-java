package org.cyclonedx.util.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.cyclonedx.model.Property;

public class PropertyDeserializer
    extends JsonDeserializer<Property>
{
  @Override
  public Property deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    ObjectCodec codec = parser.getCodec();
    JsonNode node = codec.readTree(parser);

    Property property = new Property();

    JsonNode valueNode = node.get("value");
    if (valueNode != null) {
      property.setValue(valueNode.asText());
    } else if (node.has("")) {
      property.setValue(node.get("").asText());
    }

    JsonNode nameNode = node.get("name");
    if (nameNode != null && nameNode.isTextual()) {
      property.setName(nameNode.asText());
    }

    return property;
  }
}