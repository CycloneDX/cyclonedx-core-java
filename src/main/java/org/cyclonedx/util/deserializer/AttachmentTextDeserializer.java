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

    JsonNode textNode = node.get("content");
    if (textNode != null) {
      attachmentText.setText(textNode.textValue());
    }
    else if (node.has("")) {
      attachmentText.setText( node.get("").asText());
    }

    JsonNode contentTypeNode = node.get("content-type");
    if (contentTypeNode == null || !contentTypeNode.isTextual()) {
      contentTypeNode = node.get("contentType");
    }
    if (contentTypeNode != null && contentTypeNode.isTextual()) {
      attachmentText.setContentType(contentTypeNode.textValue());
    }

    JsonNode encodingNode = node.get("encoding");
    if (encodingNode != null && encodingNode.isTextual()) {
      attachmentText.setEncoding(encodingNode.textValue());
    }

    return attachmentText;
  }
}