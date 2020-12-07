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
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.cyclonedx.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.cyclonedx.model.Dependency;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DependencyDeserializer
    implements JsonDeserializer<List<Dependency>>
{
  @Override
  public List<Dependency> deserialize(
      final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException
  {
    JsonArray parsedArray = jsonElement.getAsJsonArray();
    if (parsedArray != null) {
      final List<Dependency> dependencies = new ArrayList<>();
      for (JsonElement el : parsedArray) {
        final String ref = el.getAsJsonPrimitive().getAsString();
        dependencies.add(new Dependency(ref));
      }
      return dependencies;
    }
    return null;
  }
}
