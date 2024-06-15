package org.cyclonedx.util.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.cyclonedx.model.component.modelCard.consideration.Risk;

public class RiskDeserializer extends JsonDeserializer<Risk> {
  @Override
  public Risk deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);

    Risk risk = new Risk();
    if (node.has("name")) {
      risk.setName(node.get("name").asText());
    }
    if (node.has("mitigationStrategy")) {
      risk.setMitigationStrategy(node.get("mitigationStrategy").asText());
    }

    return risk;
  }
}
