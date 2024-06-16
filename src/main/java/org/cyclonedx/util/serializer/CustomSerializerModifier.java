package org.cyclonedx.util.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.cyclonedx.Version;
import org.cyclonedx.model.Bom;

import java.util.Iterator;
import java.util.List;

public class CustomSerializerModifier
    extends BeanSerializerModifier
{
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
    //Properties were introduced in 1.3 for XML and 1.5 for JSON
    //Meaning that we should only serialize properties if the version is 1.3 or higher for XML
    //and 1.5 or higher for JSON
    //This is to ensure backwards compatibility with older versions of the schema
    if (Bom.class.isAssignableFrom(beanDesc.getBeanClass())) {
      Iterator<BeanPropertyWriter> iterator = beanProperties.iterator();
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