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
import org.cyclonedx.model.ExternalReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExternalReferencesDeserializer extends JsonDeserializer<List<ExternalReference>> {

    private final HashesDeserializer hashesDeserializer = new HashesDeserializer();
    private final PropertiesDeserializer propertiesDeserializer = new PropertiesDeserializer();

    @Override
    public List<ExternalReference> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() == JsonToken.START_ARRAY) {
            return readReferenceArray(p, ctxt);
        }
        if (p.currentToken() == JsonToken.START_OBJECT) {
            return readExternalReferencesElement(p, ctxt);
        }

        return new ArrayList<>();
    }

    /**
     * Reads references from a JSON array:
     * <pre>{@code
     *   [
     *     { "url": "...", "type": "..." },
     *     { "url": "...", "type": "..." }
     *   ]
     * }</pre>
     */
    private List<ExternalReference> readReferenceArray(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<ExternalReference> references = new ArrayList<>();

        while (p.nextToken() != JsonToken.END_ARRAY) {
            final ExternalReference reference = readExternalReference(p, ctxt);
            if (reference != null) {
                references.add(reference);
            }
        }

        return references;
    }

    /**
     * Reads references from an XML {@code <externalReferences>} element:
     * <pre>{@code
     *   <externalReferences>
     *     <reference type="..."><url>...</url></reference>
     *     <reference type="..."><url>...</url></reference>
     *   </externalReferences>
     * }</pre>
     * <p>
     * Note that a deserializer higher up the chain may have materialized the tree already,
     * in which case the structure handed to this method looks like this:
     * <pre>{@code
     *   {
     *     "reference": [
     *       { "type": "...", "url": "..." },
     *       { "type": "...", "url": "..." }
     *     ]
     *   }
     * }</pre>
     */
    private List<ExternalReference> readExternalReferencesElement(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        List<ExternalReference> references = new ArrayList<>();

        while (p.nextToken() == JsonToken.FIELD_NAME) {
            final String fieldName = p.currentName();
            p.nextToken();

            if (!"reference".equals(fieldName)) {
                p.skipChildren();
                continue;
            }

            if (p.currentToken() == JsonToken.START_ARRAY) {
                references.addAll(readReferenceArray(p, ctxt));
            } else {
                final ExternalReference reference = readExternalReference(p, ctxt);
                if (reference != null) {
                    references.add(reference);
                }
            }
        }

        return references;
    }

    private ExternalReference readExternalReference(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() != JsonToken.START_OBJECT) {
            p.skipChildren();
            return null;
        }

        ExternalReference reference = new ExternalReference();
        boolean hasKnownField = false;

        while (p.nextToken() == JsonToken.FIELD_NAME) {
            final String fieldName = p.currentName();
            final JsonToken valueToken = p.nextToken();

            // Only "hashes" and "properties" have structured values.
            if (!valueToken.isScalarValue()
                    && !"hashes".equals(fieldName)
                    && !"properties".equals(fieldName)) {
                p.skipChildren();
                continue;
            }

            switch (fieldName) {
                case "url":
                    reference.setUrl(p.getValueAsString());
                    break;
                case "type":
                    reference.setType(ExternalReference.Type.fromString(p.getValueAsString()));
                    break;
                case "comment":
                    reference.setComment(p.getValueAsString());
                    break;
                case "hashes":
                    reference.setHashes(hashesDeserializer.deserialize(p, ctxt));
                    break;
                case "properties":
                    reference.setProperties(propertiesDeserializer.deserialize(p, ctxt));
                    break;
                default:
                    continue;
            }

            hasKnownField = true;
        }

        return hasKnownField ? reference : null;
    }

}
