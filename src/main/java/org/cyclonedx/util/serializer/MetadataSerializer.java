package org.cyclonedx.util.serializer;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.cyclonedx.Version;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.component.evidence.Occurrence;
import org.cyclonedx.model.metadata.ToolInformation;

import static org.cyclonedx.util.serializer.SerializerUtils.shouldSerializeField;

public class MetadataSerializer
    extends StdSerializer<Metadata>
{
  private final boolean isXml;

  private final Version version;

  public MetadataSerializer(final boolean isXml, final Version version) {
    this(null, isXml, version);
  }

  public MetadataSerializer(final Class<Metadata> t, final boolean isXml, final Version version) {
    super(t);
    this.isXml = isXml;
    this.version = version;
  }

  @Override
  public void serialize(Metadata output, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException
  {
    if (isXml && jsonGenerator instanceof ToXmlGenerator) {
      ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
      createMetadataInfo(output, xmlGenerator, serializerProvider);
    }
    else {
      createMetadataInfo(output, jsonGenerator, serializerProvider);
    }
  }

  private void createMetadataInfo(
      final Metadata metadata,
      final JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider)
      throws IOException
  {
    jsonGenerator.writeStartObject();

    if (metadata.getTimestamp() != null && shouldSerializeField(metadata, version, "timestamp")) {
      jsonGenerator.writeFieldName("timestamp");
      new CustomDateSerializer().serialize(metadata.getTimestamp(), jsonGenerator, serializerProvider);
    }

    if (metadata.getLifecycles() != null && shouldSerializeField(metadata, version, "lifecycles")) {
      jsonGenerator.writeFieldName("lifecycles");
      new LifecycleSerializer(isXml).serialize(metadata.getLifecycles(), jsonGenerator, serializerProvider);
    }

    //Tools
    parseTools(metadata, jsonGenerator);

    if (metadata.getAuthors() != null && shouldSerializeField(metadata, version, "author")) {
      if (isXml) {
        ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
        writeArrayFieldXML(metadata.getAuthors(), xmlGenerator, "author");
      }
      else {
        jsonGenerator.writeObjectField("authors", metadata.getAuthors());
      }
    }

    if (metadata.getComponent() != null && shouldSerializeField(metadata, version, "component")) {
      jsonGenerator.writeObjectField("component", metadata.getComponent());
    }

    if (metadata.getManufacturer() != null && shouldSerializeField(metadata, version, "manufacturer")) {
      jsonGenerator.writeObjectField("manufacturer", metadata.getManufacturer());
    }

    if (metadata.getManufacture() != null && shouldSerializeField(metadata, version, "manufacture")) {
      jsonGenerator.writeObjectField("manufacture", metadata.getManufacture());
    }

    if (metadata.getSupplier() != null && shouldSerializeField(metadata, version, "supplier")) {
      jsonGenerator.writeObjectField("supplier", metadata.getSupplier());
    }

    if (metadata.getLicenses() != null && shouldSerializeField(metadata, version, "licenses")) {
      jsonGenerator.writeFieldName("licenses");
      new LicenseChoiceSerializer(isXml, version).serialize(metadata.getLicenses(), jsonGenerator, serializerProvider);
    }

    if (CollectionUtils.isNotEmpty(metadata.getProperties()) && shouldSerializeField(metadata, version, "properties")) {
      if (isXml) {
        ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
        xmlGenerator.writeFieldName("properties");
        xmlGenerator.writeStartObject();

        for (Property property : metadata.getProperties()) {
          xmlGenerator.writeObjectField("property", property);
        }
        xmlGenerator.writeEndObject();
      }
      else {
        jsonGenerator.writeObjectField("properties", metadata.getProperties());
      }
    }

    jsonGenerator.writeEndObject();
  }

  private void parseTools(Metadata metadata, JsonGenerator jsonGenerator) throws IOException {
    if (metadata.getTools() != null) {
      if (isXml && jsonGenerator instanceof ToXmlGenerator) {
        writeArrayFieldXML(metadata.getTools(), (ToXmlGenerator) jsonGenerator, "tool");
      }
      else {
        writeArrayFieldJSON(jsonGenerator, "tools", metadata.getTools());
      }
    }
    else if (version.getVersion() >= Version.VERSION_15.getVersion()) {
      ToolInformation choice = metadata.getToolChoice();
      if (choice != null) {
        jsonGenerator.writeFieldName("tools");
        jsonGenerator.writeStartObject();
        if (isXml && jsonGenerator instanceof ToXmlGenerator) {
          if (choice.getComponents() != null) {
            writeArrayFieldXML(choice.getComponents(), (ToXmlGenerator) jsonGenerator, "component");
          }
          if (choice.getServices() != null) {
            writeArrayFieldXML(choice.getServices(), (ToXmlGenerator) jsonGenerator, "service");
          }
        }
        else {
          if (choice.getComponents() != null) {
            writeArrayFieldJSON(jsonGenerator, "components", choice.getComponents());
          }
          if (choice.getServices() != null) {
            writeArrayFieldJSON(jsonGenerator, "services", choice.getServices());
          }
        }
        jsonGenerator.writeEndObject();
      }
    }
  }

  private <T> void writeArrayFieldJSON(JsonGenerator jsonGenerator, String fieldName, List<T> items)
      throws IOException
  {
    if (items != null) {
      jsonGenerator.writeArrayFieldStart(fieldName);
      for (T item : items) {
        jsonGenerator.writeObject(item);
      }
      jsonGenerator.writeEndArray();
    }
  }

  private <T> void writeArrayFieldXML(List<T> items, ToXmlGenerator xmlGenerator, String fieldName) throws IOException {
    if (CollectionUtils.isNotEmpty(items)) {
      xmlGenerator.writeFieldName(fieldName + "s");
      xmlGenerator.writeStartObject();
      for (T item : items) {
        xmlGenerator.writeFieldName(fieldName);
        xmlGenerator.writeObject(item);
      }
      xmlGenerator.writeEndObject();
    }
  }

  @Override
  public Class<Metadata> handledType() {
    return Metadata.class;
  }
}
