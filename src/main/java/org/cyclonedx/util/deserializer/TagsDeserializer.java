package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cyclonedx.model.component.Tags;

import java.io.IOException;
import java.util.List;

public class TagsDeserializer
    extends JsonDeserializer<Tags>
{
  private final WrappedListDeserializer<String> tagsDeserializer = new WrappedListDeserializer<>("tag", String.class);

  @Override
  public boolean isCachable() {
    return true;
  }

  @Override
  public Tags deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    final List<String> tags = tagsDeserializer.deserialize(parser, context);
    return tags.isEmpty() ? null : new Tags(tags);
  }
}
