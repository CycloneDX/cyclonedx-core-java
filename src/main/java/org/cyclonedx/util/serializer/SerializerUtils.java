package org.cyclonedx.util.serializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.commons.lang3.StringUtils;
import org.cyclonedx.Version;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Hash;
import org.cyclonedx.model.Hash.Algorithm;
import org.cyclonedx.model.Property;
import org.cyclonedx.model.VersionFilter;

public class SerializerUtils
{
  private static final Logger LOGGER = Logger.getLogger(SerializerUtils.class.getName());

  public static void serializeHashXml(final ToXmlGenerator toXmlGenerator, final Hash hash) throws IOException {
    toXmlGenerator.writeStartObject();
    toXmlGenerator.setNextIsAttribute(true);
    toXmlGenerator.writeFieldName("alg");
    toXmlGenerator.writeString(hash.getAlgorithm());
    toXmlGenerator.setNextIsAttribute(false);
    toXmlGenerator.setNextIsUnwrapped(true);
    toXmlGenerator.writeStringField("", hash.getValue());
    toXmlGenerator.writeEndObject();
  }

  public static void serializeHashJson(final JsonGenerator gen, final Hash hash)
      throws IOException
  {
    gen.writeStartObject();
    gen.writeStringField("alg", hash.getAlgorithm());
    gen.writeStringField("content", hash.getValue());
    gen.writeEndObject();
  }

  public static boolean shouldSerializeField(Object obj, Version version, String fieldName) {
    if (version == null) {
      return true;
    }
    VersionFilter filter = findVersionFilter(obj.getClass(), fieldName);
    if (filter != null && filter.value().getVersion() > version.getVersion()) {
      if (LOGGER.isLoggable(Level.FINE)) {
        LOGGER.fine(String.format(
            "Skipping field '%s' on %s: introduced in version %s but generating for version %s",
            fieldName, obj.getClass().getSimpleName(),
            filter.value().getVersionString(), version.getVersionString()));
      }
      return false;
    }
    return true;
  }

  private static VersionFilter findVersionFilter(Class<?> clazz, String fieldName) {
    // Walk up the class hierarchy to find the field (getDeclaredField only checks the immediate class)
    Class<?> current = clazz;
    while (current != null && current != Object.class) {
      try {
        Field field = current.getDeclaredField(fieldName);
        return field.getAnnotation(VersionFilter.class);
      } catch (NoSuchFieldException e) {
        current = current.getSuperclass();
      }
    }
    return null;
  }

  /**
   * Checks whether an enum constant should be serialized for the target version by inspecting
   * the {@link VersionFilter} annotation on the enum constant's field.
   *
   * @param enumValue the enum constant to check
   * @param version the target BOM version being generated
   * @return true if the enum value is valid for the target version, false otherwise
   */
  public static boolean shouldSerializeEnumValue(Enum<?> enumValue, Version version) {
    try {
      VersionFilter filter = enumValue.getClass()
          .getField(enumValue.name())
          .getAnnotation(VersionFilter.class);
      if (filter != null && filter.value().getVersion() > version.getVersion()) {
        if (LOGGER.isLoggable(Level.FINE)) {
          LOGGER.fine(String.format(
              "Skipping %s.%s: introduced in version %s but generating for version %s",
              enumValue.getClass().getSimpleName(), enumValue.name(),
              filter.value().getVersionString(), version.getVersionString()));
        }
        return false;
      }
      return true;
    } catch (NoSuchFieldException e) {
      return true;
    }
  }

  /**
   * Filters a list of ExternalReferences, removing entries whose Type has a
   * {@link VersionFilter} above the target version.
   */
  public static List<ExternalReference> filterExternalReferencesByVersion(
      List<ExternalReference> refs, Version version) {
    if (refs == null) return null;
    List<ExternalReference> filtered = new ArrayList<>();
    for (ExternalReference ref : refs) {
      if (ref.getType() == null || shouldSerializeEnumValue(ref.getType(), version)) {
        filtered.add(ref);
      }
    }
    return filtered.isEmpty() ? null : filtered;
  }

  /**
   * Filters a list of Hashes, removing entries whose Algorithm has a
   * {@link VersionFilter} above the target version.
   */
  public static List<Hash> filterHashesByVersion(List<Hash> hashes, Version version) {
    if (hashes == null) return null;
    List<Hash> filtered = new ArrayList<>();
    for (Hash hash : hashes) {
      if (hash.getAlgorithm() == null) {
        filtered.add(hash);
        continue;
      }
      try {
        Algorithm algorithm = Algorithm.fromSpec(hash.getAlgorithm());
        if (shouldSerializeEnumValue(algorithm, version)) {
          filtered.add(hash);
        }
      } catch (IllegalArgumentException e) {
        // Unknown algorithm - include it
        filtered.add(hash);
      }
    }
    return filtered.isEmpty() ? null : filtered;
  }

  public static void serializeProperty(String propertyName,  Property prop, ToXmlGenerator xmlGenerator) throws IOException {
    if (StringUtils.isNotBlank(propertyName)) {
      xmlGenerator.writeFieldName(propertyName);
    }
    xmlGenerator.writeStartObject();
    xmlGenerator.setNextIsAttribute(true);
    xmlGenerator.writeFieldName("name");
    xmlGenerator.writeString(prop.getName());
    xmlGenerator.setNextIsAttribute(false);

    xmlGenerator.setNextIsUnwrapped(true);
    xmlGenerator.writeStringField("", prop.getValue());
    xmlGenerator.writeEndObject();
  }

  public static void writeField(JsonGenerator jsonGenerator, String fieldName, Object fieldValue) throws IOException {
    if (fieldValue != null) {
      jsonGenerator.writeFieldName(fieldName);
      jsonGenerator.writeObject(fieldValue);
    }
  }
}
