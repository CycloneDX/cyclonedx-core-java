package org.cyclonedx.util.deserializer;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.DistributionConstraints;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;

public class MetadataDeserializer
    extends JsonDeserializer<Metadata> {

  private final LifecycleDeserializer lifecycleDeserializer = new LifecycleDeserializer();
  private final PropertiesDeserializer propertiesDeserializer = new PropertiesDeserializer();
  private final LicenseChoiceDeserializer licenseDeserializer = new LicenseChoiceDeserializer();

  @Override
  public Metadata deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
    final Metadata metadata = new Metadata();
    metadata.setTimestamp(null); // Clear default value. Don't fabricate data that isn't there!

    JsonToken token = parser.currentToken();
    if (token == JsonToken.START_OBJECT) {
      token = parser.nextToken();
    }

    for (; token == JsonToken.FIELD_NAME; token = parser.nextToken()) {
      final String fieldName = parser.currentName();
      parser.nextToken();

      switch (fieldName) {
        case "timestamp":
          metadata.setTimestamp(ctxt.readValue(parser, Date.class));
          break;
        case "lifecycles":
          metadata.setLifecycles(lifecycleDeserializer.deserialize(parser, ctxt));
          break;
        case "tools":
          final ToolsChoice tools = ToolsChoice.read(parser, ctxt);
          metadata.setTools(tools.legacyTools());
          metadata.setToolChoice(tools.toolInformation());
          break;
        case "authors":
          metadata.setAuthors(DeserializerUtils.readList(parser, ctxt, OrganizationalContact.class));
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
}
