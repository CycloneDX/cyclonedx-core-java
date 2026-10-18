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
package org.cyclonedx.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cyclonedx.model.LifecycleChoice;
import org.cyclonedx.model.Lifecycles;

import java.io.IOException;

public class LifecycleDeserializer
    extends JsonDeserializer<Lifecycles>
{
  private final WrappedListDeserializer<LifecycleChoice> choicesDeserializer =
      new WrappedListDeserializer<>("lifecycle", LifecycleChoice.class);

  @Override
  public Lifecycles deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    final Lifecycles lifecycles = new Lifecycles();
    lifecycles.setLifecycleChoice(choicesDeserializer.deserialize(parser, context));
    return lifecycles;
  }
}
