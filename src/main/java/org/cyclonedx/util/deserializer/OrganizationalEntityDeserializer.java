package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;

public class OrganizationalEntityDeserializer
    extends JsonDeserializer<OrganizationalEntity>
{

  private final ObjectMapper mapper = new ObjectMapper();
  @Override
  public OrganizationalEntity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    ObjectCodec codec = jsonParser.getCodec();
    JsonNode node = codec.readTree(jsonParser);

    String bomRef = node.has("bom-ref") ? node.get("bom-ref").asText() : null;
    String name = node.has("name") ? node.get("name").asText() : null;

    List<String> url = new ArrayList<>();
    if (node.has("url")) {
      JsonNode urlNode = node.get("url");
      if (urlNode.isArray()) {
        for (JsonNode urlElement : urlNode) {
          url.add(urlElement.asText());
        }
      }
      else if (urlNode.isTextual()) {
        url.add(urlNode.asText());
      }
    }

    OrganizationalEntity entity = new OrganizationalEntity();
    entity.setBomRef(bomRef);
    entity.setName(name);
    entity.setUrls(url);
    JsonNode contactNode = node.get("contact");

    if (contactNode != null) {
      List<OrganizationalContact> contacts =
          MetadataDeserializer.deserializerOrganizationalContact(contactNode, mapper);
      entity.setContacts(contacts);
    }
    return entity;
  }
}
