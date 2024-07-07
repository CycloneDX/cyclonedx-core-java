package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cyclonedx.model.vulnerability.Vulnerability;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class AffectDeserializer
    extends JsonDeserializer<Vulnerability.Affect>
{
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public Vulnerability.Affect deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    ObjectCodec codec = parser.getCodec();
    JsonNode node = codec.readTree(parser);

    Vulnerability.Affect affect = new Vulnerability.Affect();

    JsonNode refNode = node.get("ref");
    if (refNode != null) {
      affect.setRef(refNode.asText());
    }

    JsonNode versionsNode = node.get("versions");
    if (versionsNode != null) {
      if (versionsNode.isArray()) {
        List<Vulnerability.Version> versions = mapper.convertValue(node.get("versions"), new TypeReference<List<Vulnerability.Version>>() {});
        affect.setVersions(versions);
      } else if (versionsNode.has("version")) {
        JsonNode versionNode = versionsNode.get("version");
        if (versionNode.isArray()) {
          List<Vulnerability.Version> versions = mapper.convertValue(versionNode, new TypeReference<List<Vulnerability.Version>>() {});
          affect.setVersions(versions);
        } else {
          affect.setVersions(Collections.singletonList(
                  mapper.convertValue(versionNode, Vulnerability.Version.class)
          ));
        }
      }
    }

    return affect;
  }
}
