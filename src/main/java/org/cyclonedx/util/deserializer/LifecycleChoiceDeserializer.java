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
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cyclonedx.model.LifecycleChoice;
import org.cyclonedx.model.LifecycleChoice.Phase;

import java.io.IOException;

public class LifecycleChoiceDeserializer extends JsonDeserializer<LifecycleChoice> {

    @Override
    public boolean isCachable() {
        return true;
    }

    @Override
    public LifecycleChoice deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.currentToken() != JsonToken.START_OBJECT) {
            parser.skipChildren();
            return null;
        }

        String phase = null;
        String name = null;
        String description = null;

        for (JsonToken token = parser.nextToken(); token == JsonToken.FIELD_NAME; token = parser.nextToken()) {
            final String fieldName = parser.currentName();
            parser.nextToken();

            switch (fieldName) {
                case "phase":
                    phase = DeserializerUtils.readText(parser);
                    break;
                case "name":
                    name = DeserializerUtils.readText(parser);
                    break;
                case "description":
                    description = DeserializerUtils.readText(parser);
                    break;
                default:
                    parser.skipChildren();
            }
        }

        final LifecycleChoice choice = new LifecycleChoice();
        if (phase != null) {
            choice.setPhase(Phase.fromString(phase));
        } else if (name != null) {
            choice.setName(name);
            choice.setDescription(description);
        } else {
            return null;
        }

        return choice;
    }
}
