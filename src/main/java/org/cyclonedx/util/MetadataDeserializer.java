package org.cyclonedx.util;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

  @Override
  public Metadata deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode node = mapper.readTree(p);

    Metadata metadata = new Metadata();

    // Parsing other fields in the Metadata object
    List<OrganizationalContact> authors = mapper.convertValue(node.get("authors"), new TypeReference<List<OrganizationalContact>>(){});
    metadata.setAuthors(authors);

    Component component = mapper.convertValue(node.get("component"), Component.class);
    metadata.setComponent(component);

    OrganizationalEntity manufacture = mapper.convertValue(node.get("manufacture"), OrganizationalEntity.class);
    metadata.setManufacture(manufacture);

    OrganizationalEntity supplier = mapper.convertValue(node.get("supplier"), OrganizationalEntity.class);
    metadata.setSupplier(supplier);

    LicenseChoice license = mapper.convertValue(node.get("license"), LicenseChoice.class);
    metadata.setLicenseChoice(license);

    /*JsonDeserializer<Date> customDateDeserializer = new CustomDateDeserializer();
    Date timestamp = customDateDeserializer.deserialize(p, ctxt);
    metadata.setTimestamp(timestamp);*/

    List<Property> properties = mapper.convertValue(node.get("properties"), new TypeReference<List<Property>>(){});
    metadata.setProperties(properties);

    JsonNode toolsNode = node.get("tools");

    // Check if the 'tools' field is an array or an object
    if (toolsNode.isArray()) {
      // If it's an array, treat it as a list of tools for the old version
      List<Tool> tools = mapper.convertValue(toolsNode, new TypeReference<List<Tool>>(){});
      metadata.setTools(tools);
    } else {
      // If it's an object, treat it as a ToolChoice for the new version
      ToolChoice toolChoice = new ToolChoice();
      if (toolsNode.has("components")) {
        List<Component> components = mapper.convertValue(toolsNode.get("components"), new TypeReference<List<Component>>(){});
        toolChoice.setComponents(components);
      }
      if (toolsNode.has("services")) {
        List<Service> services = mapper.convertValue(toolsNode.get("services"), new TypeReference<List<Service>>(){});
        toolChoice.setServices(services);
      }
      metadata.setToolChoice(toolChoice);
    }

    return metadata;
  }
}
