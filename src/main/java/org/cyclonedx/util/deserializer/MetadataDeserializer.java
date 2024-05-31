package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
import org.cyclonedx.model.ToolInformation;

public class MetadataDeserializer
    extends JsonDeserializer<Metadata> {

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

  private final LifecycleDeserializer lifecycleDeserializer = new LifecycleDeserializer();

  private final PropertiesDeserializer propertiesDeserializer = new PropertiesDeserializer();

  private final LicenseDeserializer licenseDeserializer = new LicenseDeserializer();

  private final ToolsDeserializer toolsDeserializer = new ToolsDeserializer();

  @Override
  public Metadata deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);

    Metadata metadata = new Metadata();

    ObjectMapper mapper;
    if (jsonParser.getCodec() instanceof ObjectMapper) {
      mapper = (ObjectMapper) jsonParser.getCodec();
    } else {
      mapper = new ObjectMapper();
    }

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
      JsonParser toolsParser = node.get("tools").traverse(jsonParser.getCodec());
      toolsParser.nextToken();
      Object tools = toolsDeserializer.deserialize(toolsParser, ctxt);

      if(tools instanceof ToolInformation) {
        metadata.setTools((ToolInformation) tools);
      }
      else {
        metadata.setDeprecatedTools((List<Tool>) tools);
      }
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
