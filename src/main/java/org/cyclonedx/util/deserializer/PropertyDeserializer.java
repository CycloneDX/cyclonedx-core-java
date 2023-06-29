package org.cyclonedx.util.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.cyclonedx.model.AttachmentText;
import org.cyclonedx.model.Property;

public class PropertyDeserializer
    extends StdDeserializer<Property> {

  public PropertyDeserializer() {
    this(null);
  }

  public PropertyDeserializer(Class<?> vc) {
    super(vc);
  }

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
      property.setValue( node.get("").asText());
    }

    JsonNode contentTypeNode = node.get("name");
    if (contentTypeNode != null && contentTypeNode.isTextual()) {
      property.setName(contentTypeNode.textValue());
    }
    return property;
  }
}