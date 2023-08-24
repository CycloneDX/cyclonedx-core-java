package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.cyclonedx.model.Licensing.LicensingType;

public class LicensingTypeDeserializer
    extends JsonDeserializer<List<LicensingType>>
{
  @Override
  public List<LicensingType> deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);
    if (node.has("licenseType")) {
      return parseLicenseTypes(node.get("licenseType"));
    }
    else {
      return parseLicenseTypes(node);
    }
  }

  private List<LicensingType> parseLicenseTypes(JsonNode node) {
    List<LicensingType> types = new ArrayList<>();

    ArrayNode nodes = (node.isArray() ? (ArrayNode) node : new ArrayNode(null).add(node));

    for (JsonNode resolvesNode : nodes) {
      LicensingType type = parseType(resolvesNode);
      types.add(type);
    }

    return types;
  }

  private LicensingType parseType(JsonNode node) {
    return LicensingType.fromString(node.asText());
  }
}
