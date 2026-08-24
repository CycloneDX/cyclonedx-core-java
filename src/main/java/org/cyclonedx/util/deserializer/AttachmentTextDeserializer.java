package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
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
  public AttachmentText deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    if (p.currentToken() != JsonToken.START_OBJECT) {
      if (p.currentToken() != JsonToken.VALUE_STRING) {
        return null;
      }

      AttachmentText attachmentText = new AttachmentText();
      attachmentText.setText(p.getValueAsString());
      return attachmentText;
    }

    AttachmentText attachmentText = new AttachmentText();
    boolean hasKnownField = false;

    while (p.nextToken() != JsonToken.END_OBJECT) {
      final String fieldName = p.currentName();
      p.nextToken();
      switch (fieldName) {
        // NB: For XML, content is the element's text, which gets translated
        // to a field with empty name.
        case "content":
        case "":
          attachmentText.setText(p.getValueAsString());
          break;
        // "content-type" for XML, "contentType" for JSON.
        case "content-type":
        case "contentType":
          if (p.currentToken() != JsonToken.VALUE_STRING) {
            continue;
          }
          attachmentText.setContentType(p.getValueAsString());
          break;
        case "encoding":
          if (p.currentToken() != JsonToken.VALUE_STRING) {
            continue;
          }
          attachmentText.setEncoding(p.getValueAsString());
          break;
        default:
          p.skipChildren();
          continue;
      }

      hasKnownField = true;
    }

    return hasKnownField ? attachmentText : null;
  }
}
