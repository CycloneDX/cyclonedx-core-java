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

    JsonNode textNode = node.get("value");
    if (textNode != null) {
      property.setValue(textNode.textValue());
    }
    else if (node.has("")) {
      property.setValue(node.get("").asText());
    }

    JsonNode contentTypeNode = node.get("name");
    if (contentTypeNode != null && contentTypeNode.isTextual()) {
      property.setName(contentTypeNode.textValue());
    }
    return property;
  }
}