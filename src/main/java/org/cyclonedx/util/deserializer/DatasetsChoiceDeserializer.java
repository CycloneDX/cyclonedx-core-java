package org.cyclonedx.util.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cyclonedx.model.component.data.ComponentData;
import org.cyclonedx.model.component.data.DatasetChoice;

import java.io.IOException;

public class DatasetsChoiceDeserializer
    extends JsonDeserializer<DatasetChoice>
{
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public DatasetChoice deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    DatasetChoice datasetChoice = new DatasetChoice();

    if (node.has("ref")) {
      datasetChoice.setRef(node.get("ref").asText());
    } else {
      datasetChoice.setComponentData(objectMapper.treeToValue(node, ComponentData.class));
    }

    return datasetChoice;
  }
}
