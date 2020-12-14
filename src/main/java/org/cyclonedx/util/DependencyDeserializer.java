package org.cyclonedx.util;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.cyclonedx.model.Dependency;

public class DependencyDeserializer extends StdDeserializer<List<Dependency>>
{
  public DependencyDeserializer() {
    this(null);
  }

  public DependencyDeserializer(final Class<?> vc) {
    super(vc);
  }

  @Override
  public List<Dependency> deserialize(
      final JsonParser parser, final DeserializationContext context)
      throws IOException
  {
    Dependency[] dependencies = parser.readValueAs(Dependency[].class);
    if (dependencies != null && dependencies.length > 0) {
      return dependencies[0].getDependencies();
    }

    return null;
  }
}
