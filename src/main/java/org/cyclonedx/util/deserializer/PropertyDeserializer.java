package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cyclonedx.model.Property;

import java.io.IOException;

public class PropertyDeserializer
    extends JsonDeserializer<Property>
{
  @Override
  public Property deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    final Property property = new Property();
    if (parser.currentToken() != JsonToken.START_OBJECT) {
      parser.skipChildren();
      return property;
    }

    for (JsonToken token = parser.nextToken(); token == JsonToken.FIELD_NAME; token = parser.nextToken()) {
      final String fieldName = parser.currentName();
      final JsonToken valueToken = parser.nextToken();

      if (!valueToken.isScalarValue()) {
        parser.skipChildren();
        continue;
      }

      switch (fieldName) {
        case "name":
          if (valueToken == JsonToken.VALUE_STRING) {
            property.setName(parser.getText());
          }
          break;
        // NB: For XML, the value is the element's text, which gets translated
        // to a field with empty name.
        case "value":
        case "":
          property.setValue(parser.getValueAsString());
          break;
        default:
          break;
      }
    }

    return property;
  }
}
