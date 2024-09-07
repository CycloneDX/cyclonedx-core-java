package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
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

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
  private final LifecycleDeserializer lifecycleDeserializer = new LifecycleDeserializer();
  private final PropertiesDeserializer propertiesDeserializer = new PropertiesDeserializer();
  private final LicenseDeserializer licenseDeserializer = new LicenseDeserializer();

  @Override
  public Metadata deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);

    Metadata metadata = new Metadata();

    ObjectMapper mapper = getMapper(jsonParser);

    // Parsing other fields in the Metadata object
    if (node.has("authors")) {
      JsonNode authorsNode = node.get("authors");
      List<OrganizationalContact> authors = deserializeOrganizationalContact(authorsNode, mapper);
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

    if (node.has("manufacturer")) {
      OrganizationalEntity manufacturer = mapper.convertValue(node.get("manufacturer"), OrganizationalEntity.class);
      metadata.setManufacturer(manufacturer);
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
      JsonParser licensesParser = node.get("licenses").traverse(jsonParser.getCodec());
      licensesParser.nextToken();
      LicenseChoice licenses = licenseDeserializer.deserialize(licensesParser, ctxt);
      metadata.setLicenses(licenses);
    }

    if (node.has("timestamp")) {
      setTimestamp(node, metadata);
    }

    if (node.has("properties")) {
      JsonParser propertiesParser = node.get("properties").traverse(jsonParser.getCodec());
      propertiesParser.nextToken();
      List<Property> properties = propertiesDeserializer.deserialize(propertiesParser, ctxt);
      metadata.setProperties(properties);
    }

    if (node.has("tools")) {
      parseTools(node.get("tools"), metadata, mapper);
    }

    return metadata;
  }

  private void parseComponents(JsonNode componentsNode, ToolInformation toolInformation, ObjectMapper mapper) {
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

  private void parseServices(JsonNode servicesNode, ToolInformation toolInformation, ObjectMapper mapper) {
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

  private void parseTools(JsonNode toolsNode, Metadata metadata, ObjectMapper mapper) {
    if (toolsNode.isArray()) {
      setToolInfo(toolsNode, metadata, mapper);
    } else if (toolsNode.has("tool")) {
      JsonNode toolNode = toolsNode.get("tool");
      if (toolNode.isArray()) {
        setToolInfo(toolNode, metadata, mapper);
      } else {
        Tool tool = mapper.convertValue(toolNode, Tool.class);
        metadata.setTools(Collections.singletonList(tool));
      }
    } else {
      ToolInformation toolInformation = new ToolInformation();
      if (toolsNode.has("components")) {
        parseComponents(toolsNode.get("components"), toolInformation, mapper);
      }
      if (toolsNode.has("services")) {
        parseServices(toolsNode.get("services"), toolInformation, mapper);
      }
      metadata.setToolChoice(toolInformation);
    }
  }

  private void setToolInfo(JsonNode node, Metadata metadata, ObjectMapper mapper){
    List<Tool> tools = mapper.convertValue(node, new TypeReference<List<Tool>>() {});
    metadata.setTools(tools);
  }


  static void deserializeAuthor(
      JsonNode node,
      final ObjectMapper mapper,
      List<OrganizationalContact> organizationalContactList)
  {
    OrganizationalContact author = mapper.convertValue(node, OrganizationalContact.class);
    organizationalContactList.add(author);
  }

  private ObjectMapper getMapper(JsonParser jsonParser) {
    if (jsonParser.getCodec() instanceof ObjectMapper) {
      return (ObjectMapper) jsonParser.getCodec();
    } else {
      return new ObjectMapper();
    }
  }

  private void setTimestamp(JsonNode node, Metadata metadata) {
    JsonNode timestampNode = node.get("timestamp");
    if (timestampNode != null && timestampNode.isTextual()) {
      try {
        Date timestamp = dateFormat.parse(timestampNode.textValue());
        metadata.setTimestamp(timestamp);
      } catch (ParseException e) {
        // Handle parsing exception
      }
    }
  }
}
