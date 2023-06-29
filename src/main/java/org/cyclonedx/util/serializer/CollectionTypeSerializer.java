/*
 * This file is part of CycloneDX Core (Java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) OWASP Foundation. All Rights Reserved.
 */
package org.cyclonedx.util.serializer;

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
  private final boolean useNamespace;

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
      return new DependencySerializer(useNamespace, null);
    }
    return findSerializer(config, type, beanDescription);
  }

  private boolean isDependencyListType(CollectionType type) {
    CollectionType depArrayListType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Dependency.class);
    CollectionType depListType = TypeFactory.defaultInstance().constructCollectionType(List.class, Dependency.class);
    return (type.equals(depArrayListType) || type.equals(depListType));
  }
}
