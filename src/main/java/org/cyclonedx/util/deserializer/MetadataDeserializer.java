package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.Lifecycles;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.Tool;
import org.cyclonedx.model.metadata.ToolInformation;

public class MetadataDeserializer
    extends JsonDeserializer<Metadata> {

  private final ObjectMapper mapper = new ObjectMapper();

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

  private final LifecycleDeserializer lifecycleDeserializer = new LifecycleDeserializer();

  private final PropertiesDeserializer propertiesDeserializer = new PropertiesDeserializer();

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

    if (node.has("lifecycles")) {
      JsonParser lifecycleParser = node.get("lifecycles").traverse(jsonParser.getCodec());
      lifecycleParser.nextToken();
      Lifecycles lifecycles = lifecycleDeserializer.deserialize(lifecycleParser, ctxt);
      metadata.setLifecycles(lifecycles);
    }

    if (node.has("supplier")) {
      OrganizationalEntity supplier = mapper.convertValue(node.get("supplier"), OrganizationalEntity.class);
      metadata.setSupplier(supplier);
    }

    if(node.has("licenses")) {
      LicenseChoice license = mapper.convertValue(node.get("licenses"), LicenseChoice.class);
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

    if (node.has("properties")) {
      JsonParser propertiesParser = node.get("properties").traverse(jsonParser.getCodec());
      propertiesParser.nextToken();
      List<Property> properties = propertiesDeserializer.deserialize(propertiesParser, ctxt);
      metadata.setProperties(properties);
    }

    JsonNode toolsNode = node.get("tools");

    if (toolsNode != null) {
      // Check if the 'tools' field is an array or an object
      if (toolsNode.isArray()) {
        List<Tool> tools = mapper.convertValue(toolsNode, new TypeReference<List<Tool>>() { });
        metadata.setTools(tools);
      }
      else if (toolsNode.has("tool")) {
        final JsonNode toolNode = toolsNode.get("tool");
        // When deserializing XML BOMs, and multiple tools are provided, Jackson's internal
        // representation looks like this:
        //   {"tool": [{"name": "foo"}, {"name": "bar"}]}
        // If only a single tool is provided, it looks like this:
        //   {"tool": {"name": "foo"}}
        if (toolNode.isArray()) {
          List<Tool> tools = mapper.convertValue(toolsNode.get("tool"), new TypeReference<List<Tool>>() { });
          metadata.setTools(tools);
        } else {
          Tool tool = mapper.convertValue(toolsNode.get("tool"), Tool.class);
          metadata.setTools(Collections.singletonList(tool));
        }
      }
      else {
        ToolInformation toolInformation = new ToolInformation();
        if (toolsNode.has("components")) {
          parseComponents(toolsNode.get("components"), toolInformation);
        }
        if (toolsNode.has("services")) {
          parseServices(toolsNode.get("services"), toolInformation);
        }
        metadata.setToolChoice(toolInformation);
      }
    }

    return metadata;
  }

  private void parseComponents(JsonNode componentsNode, ToolInformation toolInformation) {
    if (componentsNode != null) {
      if (componentsNode.isArray()) {
        List<Component> components = mapper.convertValue(componentsNode, new TypeReference<List<Component>>() {});
        toolInformation.setComponents(components);
      } else if (componentsNode.isObject()) {
        Component component = mapper.convertValue(componentsNode, Component.class);
        toolInformation.setComponents(Collections.singletonList(component));
      }
    }
  }

  private void parseServices(JsonNode servicesNode, ToolInformation toolInformation) {
    if (servicesNode != null) {
      if (servicesNode.isArray()) {
        List<Service> services = mapper.convertValue(servicesNode, new TypeReference<List<Service>>() {});
        toolInformation.setServices(services);
      } else if (servicesNode.isObject()) {
        Service service = mapper.convertValue(servicesNode, Service.class);
        toolInformation.setServices(Collections.singletonList(service));
      }
    }
  }

  static List<OrganizationalContact> deserializerOrganizationalContact(JsonNode node, final ObjectMapper mapper) {
    List<OrganizationalContact> organizationalContactList = new ArrayList<>();

    if (node.has("author")) {
      node = node.get("author");
    }

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
