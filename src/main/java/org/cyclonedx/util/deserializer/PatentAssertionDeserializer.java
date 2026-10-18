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
import org.cyclonedx.model.OrganizationalChoice;
import org.cyclonedx.model.PatentAssertion;
import org.cyclonedx.model.PatentAssertion.AssertionType;

import java.io.IOException;
import java.util.List;

/**
 * Custom deserializer for PatentAssertion that handles the bom-ref attribute/element conflict
 * in XML. In XML, "bom-ref" appears as both an attribute on &lt;patentAssertion&gt; and as
 * child elements inside &lt;patentRefs&gt;. Jackson XML's default property resolution confuses
 * these, so this deserializer handles them manually.
 */
public class PatentAssertionDeserializer extends JsonDeserializer<PatentAssertion> {

    private final WrappedListDeserializer<String> patentRefsDeserializer =
            new WrappedListDeserializer<>("bom-ref", String.class);

    @Override
    public PatentAssertion deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final PatentAssertion pa = new PatentAssertion();
        if (p.currentToken() != JsonToken.START_OBJECT) {
            p.skipChildren();
            return pa;
        }

        for (JsonToken token = p.nextToken(); token == JsonToken.FIELD_NAME; token = p.nextToken()) {
            final String fieldName = p.currentName();
            p.nextToken();

            switch (fieldName) {
                case "bom-ref":
                    pa.setBomRef(DeserializerUtils.readText(p));
                    break;
                case "assertionType":
                    pa.setAssertionType(AssertionType.fromValue(DeserializerUtils.readText(p)));
                    break;
                case "patentRefs":
                    final List<String> refs = patentRefsDeserializer.deserialize(p, ctxt);
                    pa.setPatentRefs(refs.isEmpty() ? null : refs);
                    break;
                case "asserter":
                    pa.setAsserter(ctxt.readValue(p, OrganizationalChoice.class));
                    break;
                case "notes":
                    pa.setNotes(DeserializerUtils.readText(p));
                    break;
                default:
                    p.skipChildren();
            }
        }

        return pa;
    }
}
