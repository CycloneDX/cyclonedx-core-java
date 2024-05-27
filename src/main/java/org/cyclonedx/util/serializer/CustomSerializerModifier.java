package org.cyclonedx.util.serializer;

import java.util.List;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.cyclonedx.Version;
import org.cyclonedx.model.Property;

public class CustomSerializerModifier extends BeanSerializerModifier
{

  private final boolean isXml;
  private final Version version;

  public CustomSerializerModifier(boolean isXml, Version version) {
    this.isXml = isXml;
    this.version = version;
  }

  @Override
  public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
    for (BeanPropertyWriter writer : beanProperties) {
      if (writer.getAnnotation(CustomPropertyListSerializer.class) != null) {
        writer.assignSerializer(new PropertiesSerializer(beanDesc.getBeanClass(),isXml, version));
      }
    }
    return beanProperties;
  }
}
