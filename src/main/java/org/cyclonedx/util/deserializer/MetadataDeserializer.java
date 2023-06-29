package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.Tool;
import org.cyclonedx.model.metadata.ToolChoice;

public class MetadataDeserializer
    extends JsonDeserializer<Metadata> {

  private final ObjectMapper mapper = new ObjectMapper();

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

  @Override
  public Metadata deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);

    Metadata metadata = new Metadata();

    // Parsing other fields in the Metadata object
    if (node.has("authors")) {
      JsonNode authorsNode = node.get("authors");
      List<OrganizationalContact> authors = deserializerOrganizationalContact(authorsNode, mapper);
      metadata.setAuthors(authors);
    }

    if(node.has("component")) {
      Component component = mapper.convertValue(node.get("component"), Component.class);
      metadata.setComponent(component);
    }

    if (node.has("manufacture")) {
      OrganizationalEntity manufacture = mapper.convertValue(node.get("manufacture"), OrganizationalEntity.class);
      metadata.setManufacture(manufacture);
    }

    if (node.has("supplier")) {
      OrganizationalEntity supplier = mapper.convertValue(node.get("supplier"), OrganizationalEntity.class);
      metadata.setSupplier(supplier);
    }

    if(node.has("license")) {
      LicenseChoice license = mapper.convertValue(node.get("license"), LicenseChoice.class);
      metadata.setLicenseChoice(license);
    }

    if (node.has("timestamp")) {
      JsonNode timestampNode = node.get("timestamp");
      if (timestampNode != null && timestampNode.isTextual()) {
        String timestampStr = timestampNode.textValue();
        try {
          Date timestamp = dateFormat.parse(timestampStr);
          metadata.setTimestamp(timestamp);
        } catch (ParseException e) {
          // Handle parsing exception
        }
      }
    }

    if(node.has("properties")) {
      JsonNode propertiesNode = node.get("properties");

      if (propertiesNode.isObject()) {
        List<Property> properties = new ArrayList<>();
        Iterator<Entry<String, JsonNode>> fields = propertiesNode.fields();
        while (fields.hasNext()) {
          Map.Entry<String, JsonNode> field = fields.next();
          if (field.getValue().isArray()) {
            for (JsonNode propertyNode : field.getValue()) {
              Property property = mapper.convertValue(propertyNode, Property.class);
              properties.add(property);
            }
          }
        }
        metadata.setProperties(properties);
      }
    }

    JsonNode toolsNode = node.get("tools");

    if (toolsNode != null) {
      // Check if the 'tools' field is an array or an object
      if (toolsNode.isArray()) {
        List<Tool> tools = mapper.convertValue(toolsNode, new TypeReference<List<Tool>>() { });
        metadata.setTools(tools);
      }
      else if (toolsNode.has("tool")) {
        Tool tool = mapper.convertValue(toolsNode.get("tool"), Tool.class);
        metadata.setTools(Collections.singletonList(tool));
      }
      else {
        ToolChoice toolChoice = new ToolChoice();
        if (toolsNode.has("components")) {
          JsonNode componentsNode = toolsNode.get("components");
          if (componentsNode.isArray()) {
            List<Component> components = mapper.convertValue(componentsNode, new TypeReference<List<Component>>() { });
            toolChoice.setComponents(components);
          }
          else if (componentsNode.isObject()) {
            Component component = mapper.convertValue(componentsNode, Component.class);
            toolChoice.setComponents( Collections.singletonList(component));
          }
        }
        if (toolsNode.has("services")) {
          JsonNode servicesNode = toolsNode.get("services");
          if (servicesNode.isArray()) {
            List<Service> services = mapper.convertValue(servicesNode, new TypeReference<List<Service>>() { });
            toolChoice.setServices(services);
          }
          else if (servicesNode.isObject()) {
            Service service = mapper.convertValue(servicesNode, Service.class);
            toolChoice.setServices(Collections.singletonList(service));
          }
        }
        metadata.setToolChoice(toolChoice);
      }
    }

    return metadata;
  }

  static List<OrganizationalContact> deserializerOrganizationalContact(final JsonNode node, final ObjectMapper mapper) {
    List<OrganizationalContact> organizationalContactList = new ArrayList<>();

    if (node.isArray()) {
      for (JsonNode authorNode : node) {
        OrganizationalContact author = mapper.convertValue(authorNode, OrganizationalContact.class);
        organizationalContactList.add(author);
      }
    } else if (node.isObject()) {
      OrganizationalContact author = mapper.convertValue(node, OrganizationalContact.class);
      organizationalContactList.add(author);
    }
    return organizationalContactList;
  }
}
