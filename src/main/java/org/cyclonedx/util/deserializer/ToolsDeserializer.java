package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cyclonedx.model.Tool;
import org.cyclonedx.model.ToolInformation;

public class ToolsDeserializer
    extends JsonDeserializer<Object>
{
  @Override
  public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    ObjectMapper mapper = (ObjectMapper) parser.getCodec();
    JsonNode node = mapper.readTree(parser);

    if (node.isArray() && !node.isEmpty()) {
      return mapper.treeToValue(node, new TypeReference<List<Tool>>() { });
    } else {
      if (node.isObject()) {
        if(node.has("components") || node.has("services")){
          return mapper.treeToValue(node, ToolInformation.class);
        } else if(node.has("tool") ) {
          Tool tool =  mapper.treeToValue(node, Tool.class);
          return Arrays.asList(tool);
        }
      }
    }
    return null;
  }
}

