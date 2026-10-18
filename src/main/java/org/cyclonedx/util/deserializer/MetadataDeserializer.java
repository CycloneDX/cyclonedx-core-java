package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.DistributionConstraints;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.Tool;
import org.cyclonedx.model.metadata.ToolInformation;
import org.cyclonedx.util.TimestampUtils;

public class MetadataDeserializer
    extends JsonDeserializer<Metadata> {

  private final LifecycleDeserializer lifecycleDeserializer = new LifecycleDeserializer();
  private final PropertiesDeserializer propertiesDeserializer = new PropertiesDeserializer();
  private final LicenseChoiceDeserializer licenseDeserializer = new LicenseChoiceDeserializer();

  @Override
  public Metadata deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
    final Metadata metadata = new Metadata();

    JsonToken token = parser.currentToken();
    if (token == JsonToken.START_OBJECT) {
      token = parser.nextToken();
    }

    for (; token == JsonToken.FIELD_NAME; token = parser.nextToken()) {
      final String fieldName = parser.currentName();
      parser.nextToken();

      switch (fieldName) {
        case "timestamp":
          if (parser.currentToken() == JsonToken.VALUE_STRING) {
            metadata.setTimestamp(TimestampUtils.parseTimestamp(parser.getText()));
          }
          break;
        case "lifecycles":
          metadata.setLifecycles(lifecycleDeserializer.deserialize(parser, ctxt));
          break;
        case "tools":
          readTools(parser, ctxt, metadata);
          break;
        case "authors":
          metadata.setAuthors(readList(parser, ctxt, OrganizationalContact.class));
          break;
        case "component":
          metadata.setComponent(ctxt.readValue(parser, Component.class));
          break;
        case "manufacturer":
          metadata.setManufacturer(ctxt.readValue(parser, OrganizationalEntity.class));
          break;
        case "manufacture":
          metadata.setManufacture(ctxt.readValue(parser, OrganizationalEntity.class));
          break;
        case "supplier":
          metadata.setSupplier(ctxt.readValue(parser, OrganizationalEntity.class));
          break;
        case "licenses":
          metadata.setLicenses(licenseDeserializer.deserialize(parser, ctxt));
          break;
        case "properties":
          metadata.setProperties(propertiesDeserializer.deserialize(parser, ctxt));
          break;
        case "distributionConstraints":
          metadata.setDistributionConstraints(ctxt.readValue(parser, DistributionConstraints.class));
          break;
        default:
          parser.skipChildren();
      }
    }

    return metadata;
  }

  static List<OrganizationalContact> deserializeOrganizationalContact(JsonNode node, final ObjectMapper mapper) {
    List<OrganizationalContact> organizationalContactList = new ArrayList<>();

    if (node.has("author")) {
      node = node.get("author");
    }

    if (node.isArray()) {
      for (JsonNode authorNode : node) {
        deserializeAuthor(authorNode, mapper, organizationalContactList);
      }
    }
    else if (node.isObject()) {
      deserializeAuthor(node, mapper, organizationalContactList);
    }
    return organizationalContactList;
  }

  static void deserializeAuthor(
      JsonNode node,
      final ObjectMapper mapper,
      List<OrganizationalContact> organizationalContactList)
  {
    OrganizationalContact author = mapper.convertValue(node, OrganizationalContact.class);
    organizationalContactList.add(author);
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  static class ToolsChoice {

    @SuppressWarnings("deprecation")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<Tool> tool;

    @JacksonXmlElementWrapper(localName = "components")
    @JacksonXmlProperty(localName = "component")
    public List<Component> components;

    @JacksonXmlElementWrapper(localName = "services")
    @JacksonXmlProperty(localName = "service")
    public List<Service> services;
  }

  @SuppressWarnings("deprecation")
  private static void readTools(JsonParser parser, DeserializationContext ctxt, Metadata metadata) throws IOException {
    if (parser.currentToken() == JsonToken.START_ARRAY) {
      metadata.setTools(readList(parser, ctxt, Tool.class));
      return;
    }

    final ToolsChoice tools = ctxt.readValue(parser, ToolsChoice.class);
    if (tools.components != null || tools.services != null) {
      final ToolInformation toolInformation = new ToolInformation();
      toolInformation.setComponents(tools.components);
      toolInformation.setServices(tools.services);
      metadata.setToolChoice(toolInformation);
    } else {
      metadata.setTools(tools.tool);
    }
  }

  private static <T> List<T> readList(JsonParser parser, DeserializationContext ctxt, Class<T> type) throws IOException {
    return ctxt.readValue(parser, ctxt.getTypeFactory().constructCollectionType(ArrayList.class, type));
  }
}
