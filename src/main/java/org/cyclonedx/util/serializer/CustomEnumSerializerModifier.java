package org.cyclonedx.util.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.cyclonedx.Version;
import org.cyclonedx.model.VersionFilter;
;
import java.util.ArrayList;
import java.util.List;

public class CustomEnumSerializerModifier extends BeanSerializerModifier {

  private final Version currentVersion;

  public CustomEnumSerializerModifier(Version currentVersion) {
    this.currentVersion = currentVersion;
  }

  @Override
  public JsonSerializer<?> modifyEnumSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, JsonSerializer<?> serializer) {
    return new VersionFilteringEnumSerializer(currentVersion);
  }

  @Override
  public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
    List<BeanPropertyWriter> newProperties = new ArrayList<>();
    for (BeanPropertyWriter writer : beanProperties) {
      if (shouldInclude(writer)) {
        newProperties.add(writer);
      }
    }
    return newProperties;
  }

  private boolean shouldInclude(BeanPropertyWriter writer) {
    // Check for VersionFilter annotation and current version here
    VersionFilter versionFilter = writer.getAnnotation(VersionFilter.class);
    if (versionFilter != null && versionFilter.value().compareTo(currentVersion) > 0) {
      return false;
    }

    // Check if the property is an Enum with VersionFilter annotation
    if (Enum.class.isAssignableFrom(writer.getType().getRawClass())) {
      try {
        Enum<?> enumValue = (Enum<?>) writer.get(null);
        VersionFilter enumVersionFilter = enumValue.getClass().getField(enumValue.name()).getAnnotation(VersionFilter.class);
        if (enumVersionFilter != null && enumVersionFilter.value().compareTo(currentVersion) > 0) {
          return false;
        }
      } catch (Exception e) {
        // Handle exception
      }
    }
    return true;
  }
}
