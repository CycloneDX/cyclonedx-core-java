package org.cyclonedx.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.cyclonedx.model.Dependency;

public class CollectionTypeSerializer extends SimpleSerializers
{
  private boolean useNamespace;

  public CollectionTypeSerializer(final boolean useNamespace) {
    this.useNamespace = useNamespace;
  }

  @Override
  public JsonSerializer<?> findCollectionSerializer(SerializationConfig config,
                                                    CollectionType type,
                                                    BeanDescription beanDescription,
                                                    TypeSerializer typeSerializer,
                                                    JsonSerializer<Object> elementValueSerializer)
  {
    if (isDependencyListType(type)) {
      return new DependencySerializer(useNamespace);
    }
    return findSerializer(config, type, beanDescription);
  }

  private boolean isDependencyListType(CollectionType type) {
    CollectionType depArrayListType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Dependency.class);
    CollectionType depListType = TypeFactory.defaultInstance().constructCollectionType(List.class, Dependency.class);
    return (type.equals(depArrayListType) || type.equals(depListType));
  }
}
