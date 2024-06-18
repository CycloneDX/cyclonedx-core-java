package org.cyclonedx.util.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.cyclonedx.Version;
import org.cyclonedx.model.VersionFilter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;

import java.io.IOException;

public class CustomEnumSerializerModifier extends BeanSerializerModifier {

  private final Version currentVersion;

  public CustomEnumSerializerModifier(Version currentVersion) {
    this.currentVersion = currentVersion;
  }

  @Override
  public JsonSerializer<?> modifyEnumSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, JsonSerializer<?> serializer) {
    return new VersionFilteringEnumSerializer((EnumSerializer) serializer, currentVersion);
  }

  public static class VersionFilteringEnumSerializer extends JsonSerializer<Enum<?>> {

    private final EnumSerializer defaultSerializer;
    private final Version currentVersion;

    public VersionFilteringEnumSerializer(EnumSerializer defaultSerializer, Version currentVersion) {
      this.defaultSerializer = defaultSerializer;
      this.currentVersion = currentVersion;
    }

    @Override
    public void serialize(Enum<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      try {
        VersionFilter versionFilter = value.getClass().getField(value.name()).getAnnotation(VersionFilter.class);
        if (versionFilter == null || versionFilter.value().compareTo(currentVersion) <= 0) {
          defaultSerializer.serialize(value, gen, serializers);
        }
      }catch (NoSuchFieldException e) {
        defaultSerializer.serialize(value, gen, serializers);
      }
    }
  }
}
