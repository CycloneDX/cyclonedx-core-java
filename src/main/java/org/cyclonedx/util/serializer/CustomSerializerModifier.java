package org.cyclonedx.util.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.cyclonedx.Version;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.VersionFilter;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomSerializerModifier
    extends BeanSerializerModifier
{
  private static final Logger LOGGER = Logger.getLogger(CustomSerializerModifier.class.getName());

  private final Version version;

  private final boolean isXml;

  public CustomSerializerModifier(boolean isXml, Version version) {
    this.version = version;
    this.isXml = isXml;
  }

  @Override
  public List<BeanPropertyWriter> changeProperties(
      SerializationConfig config,
      BeanDescription beanDesc,
      List<BeanPropertyWriter> beanProperties)
  {
    // Automatically remove fields annotated with @VersionFilter whose version
    // is above the target generation version. This applies to ALL model classes,
    // ensuring version-specific fields are never serialized for older BOM versions.
    Iterator<BeanPropertyWriter> iterator = beanProperties.iterator();
    while (iterator.hasNext()) {
      BeanPropertyWriter writer = iterator.next();
      VersionFilter filter = writer.getAnnotation(VersionFilter.class);
      if (filter != null && filter.value().getVersion() > version.getVersion()) {
        if (LOGGER.isLoggable(Level.FINE)) {
          LOGGER.fine(String.format(
              "Removing field '%s' on %s: introduced in version %s but generating for version %s",
              writer.getName(), beanDesc.getBeanClass().getSimpleName(),
              filter.value().getVersionString(), version.getVersionString()));
        }
        iterator.remove();
      }
    }

    // Special case: Bom.properties has different version thresholds for XML (1.3) and JSON (1.5).
    // This cannot be expressed with a single @VersionFilter annotation.
    if (Bom.class.isAssignableFrom(beanDesc.getBeanClass())) {
      iterator = beanProperties.iterator();
      while (iterator.hasNext()) {
        BeanPropertyWriter writer = iterator.next();
        if (isValidAttribute(writer)) {
          if (shouldSerializeProperties(version)) {
            JsonSerializer<?> serializer = new PropertiesSerializer(isXml);
            writer.assignSerializer((JsonSerializer<Object>) serializer);
          }
          else {
            // Remove the properties field from the list of properties
            iterator.remove();
          }
        }
      }
    }
    return beanProperties;
  }

  private boolean shouldSerializeProperties(Version version) {
    // Check the version and decide if properties should be serialized
    return (isXml && version.getVersion() >= Version.VERSION_13.getVersion())
        || (!isXml && version.getVersion() >= Version.VERSION_15.getVersion());
  }

  private boolean isValidAttribute(BeanPropertyWriter writer) {
    if (isXml) {
      return "properties".equals(writer.getWrapperName().getSimpleName());
    }
    else {
      return "properties".equals(writer.getName());
    }
  }
}
