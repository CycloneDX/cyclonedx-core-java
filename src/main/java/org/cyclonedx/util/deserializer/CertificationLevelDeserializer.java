package org.cyclonedx.util.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.cyclonedx.model.component.crypto.enums.CertificationLevel;

public class CertificationLevelDeserializer
    extends JsonDeserializer<CertificationLevel>
{
  @Override
  public CertificationLevel deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);
    JsonNode certificationLevelNode = node.has("certificationLevel") ? node.get("certificationLevel") : node;

    String certificationLevelText = certificationLevelNode.isArray() ?
        certificationLevelNode.get(0).asText() :
        certificationLevelNode.asText();

    return CertificationLevel.fromString(certificationLevelText);
  }
}