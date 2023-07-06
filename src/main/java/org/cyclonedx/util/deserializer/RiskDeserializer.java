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
    String name = node.get("name").asText();
    String mitigationStrategy = node.get("mitigationStrategy").asText();

    Risk risk = new Risk();
    risk.setName(name);
    risk.setMitigationStrategy(mitigationStrategy);

    return risk;
  }
}
