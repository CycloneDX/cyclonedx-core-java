package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.cyclonedx.model.AttachmentText;
import org.cyclonedx.model.ReleaseNotes.Notes;

public class NotesDeserializer
    extends JsonDeserializer<List<Notes>>
{

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public List<Notes> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);
    return parseNode(node.has("note") ? node.get("note") : node);
  }

  private List<Notes> parseNode(JsonNode node) throws JsonProcessingException {
    if (node.isEmpty()) {
      return Collections.emptyList();
    }

    List<Notes> list = new ArrayList<>();
    ArrayNode nodes = DeserializerUtils.getArrayNode(node, mapper);

    for (JsonNode noteNode : nodes) {
      list.add(parseNotes(noteNode));
    }
    return list;
  }

  private Notes parseNotes(JsonNode node) throws JsonProcessingException {
    Notes notes = new Notes();

    if (node.has("locale")) {
      notes.setLocale(node.get("locale").asText());
    }

    if (node.has("text")) {
      JsonNode dataNode = node.get("text");
      AttachmentText data = mapper.treeToValue(dataNode, AttachmentText.class);
      notes.setText(data);
    }
    return notes;
  }
}