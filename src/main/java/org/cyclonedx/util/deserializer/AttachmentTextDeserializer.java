package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.cyclonedx.model.AttachmentText;

import java.io.IOException;

public class AttachmentTextDeserializer extends StdDeserializer<AttachmentText> {

  public AttachmentTextDeserializer() {
    this(null);
  }

  public AttachmentTextDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public AttachmentText deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    ObjectCodec codec = parser.getCodec();
    JsonNode node = codec.readTree(parser);

    AttachmentText attachmentText = new AttachmentText();

    JsonNode contentNode = node.get("content");
    if (contentNode != null) {
      attachmentText.setText(contentNode.asText());
    } else if (node.has("")) {
      attachmentText.setText(node.get("").asText());
    } else if (node.isTextual()) {
      attachmentText.setText(node.textValue());
    }

    JsonNode contentTypeNode = getContentTypeNode(node);
    if (contentTypeNode != null && contentTypeNode.isTextual()) {
      attachmentText.setContentType(contentTypeNode.asText());
    }

    JsonNode encodingNode = node.get("encoding");
    if (encodingNode != null && encodingNode.isTextual()) {
      attachmentText.setEncoding(encodingNode.asText());
    }

    return attachmentText;
  }

  private JsonNode getContentTypeNode(JsonNode node) {
    JsonNode contentTypeNode = node.get("content-type");
    if (contentTypeNode == null || !contentTypeNode.isTextual()) {
      contentTypeNode = node.get("contentType");
    }
    return contentTypeNode;
  }
}