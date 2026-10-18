package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.organization.PostalAddress;

public class OrganizationalEntityDeserializer
    extends JsonDeserializer<OrganizationalEntity>
{
  @Override
  public OrganizationalEntity deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
    final OrganizationalEntity entity = new OrganizationalEntity();
    if (parser.currentToken() != JsonToken.START_OBJECT) {
      parser.skipChildren();
      return entity;
    }

    final List<String> urls = new ArrayList<>();
    List<OrganizationalContact> contacts = null;

    for (JsonToken token = parser.nextToken(); token == JsonToken.FIELD_NAME; token = parser.nextToken()) {
      final String fieldName = parser.currentName();
      parser.nextToken();

      switch (fieldName) {
        case "bom-ref":
          entity.setBomRef(DeserializerUtils.readText(parser));
          break;
        case "name":
          entity.setName(DeserializerUtils.readText(parser));
          break;
        case "address":
          entity.setAddress(ctxt.readValue(parser, PostalAddress.class));
          break;
        case "url":
          DeserializerUtils.readItemsInto(parser, ctxt, String.class, urls);
          break;
        case "contact":
          if (contacts == null) {
            contacts = new ArrayList<>();
          }
          DeserializerUtils.readItemsInto(parser, ctxt, OrganizationalContact.class, contacts);
          break;
        default:
          parser.skipChildren();
      }
    }

    entity.setUrls(urls.isEmpty() ? null : urls);
    entity.setContacts(contacts);
    return entity;
  }
}
