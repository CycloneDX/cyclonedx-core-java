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
import org.cyclonedx.model.Hash;

import java.io.IOException;

public class HashDeserializer extends JsonDeserializer<Hash> {

    @Override
    public boolean isCachable() {
        return true;
    }

    @Override
    public Hash deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() != JsonToken.START_OBJECT) {
            p.skipChildren();
            return null;
        }

        String algorithm = null;
        String value = null;
        boolean hasKnownField = false;

        while (p.nextToken() == JsonToken.FIELD_NAME) {
            final String fieldName = p.currentName();
            final JsonToken valueToken = p.nextToken();

            // None of the known fields have structured values.
            if (!valueToken.isScalarValue()) {
                p.skipChildren();
                continue;
            }

            switch (fieldName) {
                case "alg":
                    algorithm = p.getValueAsString();
                    break;
                // NB: For XML, content is the element's text, which gets translated
                // to a field with empty name.
                case "content":
                case "":
                    value = p.getValueAsString();
                    break;
                default:
                    continue;
            }

            hasKnownField = true;
        }

        return hasKnownField ? new Hash(algorithm, value) : null;
    }
}
