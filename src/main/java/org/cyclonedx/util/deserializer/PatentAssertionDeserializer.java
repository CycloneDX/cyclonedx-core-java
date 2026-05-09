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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.cyclonedx.model.OrganizationalChoice;
import org.cyclonedx.model.PatentAssertion;
import org.cyclonedx.model.PatentAssertion.AssertionType;

/**
 * Custom deserializer for PatentAssertion that handles the bom-ref attribute/element conflict
 * in XML. In XML, "bom-ref" appears as both an attribute on &lt;patentAssertion&gt; and as
 * child elements inside &lt;patentRefs&gt;. Jackson XML's default property resolution confuses
 * these, so this deserializer handles them manually.
 */
public class PatentAssertionDeserializer extends JsonDeserializer<PatentAssertion> {

    @Override
    public PatentAssertion deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.readValueAsTree();
        PatentAssertion pa = new PatentAssertion();

        // bom-ref (attribute in XML, property in JSON)
        if (node.has("bom-ref")) {
            JsonNode bomRefNode = node.get("bom-ref");
            if (bomRefNode.isTextual()) {
                pa.setBomRef(bomRefNode.asText());
            }
        }

        // assertionType
        if (node.has("assertionType")) {
            pa.setAssertionType(AssertionType.fromValue(node.get("assertionType").asText()));
        }

        // patentRefs - in JSON it's an array of strings, in XML it's an object with bom-ref children
        if (node.has("patentRefs")) {
            JsonNode patentRefsNode = node.get("patentRefs");
            List<String> refs = new ArrayList<>();
            if (patentRefsNode.isArray()) {
                // JSON format: ["patent-1", "patent-2"]
                for (JsonNode ref : patentRefsNode) {
                    refs.add(ref.asText());
                }
            } else if (patentRefsNode.isObject()) {
                // XML format: {"bom-ref": "patent-1"} or {"bom-ref": ["patent-1", "patent-2"]}
                JsonNode bomRefChildren = patentRefsNode.get("bom-ref");
                if (bomRefChildren != null) {
                    if (bomRefChildren.isArray()) {
                        for (JsonNode ref : bomRefChildren) {
                            refs.add(ref.asText());
                        }
                    } else {
                        refs.add(bomRefChildren.asText());
                    }
                }
            }
            if (!refs.isEmpty()) {
                pa.setPatentRefs(refs);
            }
        }

        // asserter (OrganizationalChoice)
        if (node.has("asserter")) {
            JsonNode asserterNode = node.get("asserter");
            OrganizationalChoiceDeserializer choiceDeser = new OrganizationalChoiceDeserializer();
            JsonParser asserterParser = asserterNode.traverse(p.getCodec());
            asserterParser.nextToken();
            OrganizationalChoice asserter = choiceDeser.deserialize(asserterParser, ctxt);
            pa.setAsserter(asserter);
        }

        // notes
        if (node.has("notes")) {
            pa.setNotes(node.get("notes").asText());
        }

        return pa;
    }
}
