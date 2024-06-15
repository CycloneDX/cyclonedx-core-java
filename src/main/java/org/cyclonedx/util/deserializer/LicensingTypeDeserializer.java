package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    return parseLicenseTypes(node.has("licenseType") ? node.get("licenseType") : node);
  }

  private List<LicensingType> parseLicenseTypes(JsonNode node) {
    List<LicensingType> types = new ArrayList<>();
    ArrayNode nodes = DeserializerUtils.getArrayNode(node, null);

    if (nodes.isEmpty()) {
      return Collections.emptyList();
    }

    for (JsonNode typeNode : nodes) {
      types.add(parseType(typeNode));
    }

    return types;
  }

  private LicensingType parseType(JsonNode node) {
    return LicensingType.fromString(node.asText());
  }
}
