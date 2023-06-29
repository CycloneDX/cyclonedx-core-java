package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.Tool;
import org.cyclonedx.model.metadata.ToolChoice;

public class ToolChoiceDeserializer extends JsonDeserializer<ToolChoice>
{

  private final ObjectMapper jsonMapper;
  private final XmlMapper xmlMapper;

  public ToolChoiceDeserializer() {
    this.jsonMapper = new ObjectMapper();
    this.xmlMapper = new XmlMapper();
  }
  @Override
  public ToolChoice deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws
                                                                                                      IOException
  {
    ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
    JsonNode node = mapper.readTree(jsonParser);

    ToolChoice toolChoice = new ToolChoice();

    if (node.has("components")) {
      List<Component> components =
          mapper.convertValue(node.get("components"), new TypeReference<List<Component>>() { });
      toolChoice.setComponents(components);
    } else if (node.has("services")) {
      List<Service> services = mapper.convertValue(node.get("services"), new TypeReference<List<Service>>() { });
      toolChoice.setServices(services);
    }

    return toolChoice;
  }
}
