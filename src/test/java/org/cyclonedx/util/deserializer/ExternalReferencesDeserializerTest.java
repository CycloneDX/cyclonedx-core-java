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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.cyclonedx.model.Bom;
import org.cyclonedx.parsers.XmlParser;
import org.cyclonedx.model.ExternalReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExternalReferencesDeserializerTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"externalReferences\":[{}]}",
            "{\"externalReferences\":[null]}",
            "{\"externalReferences\":[\"nonsense\"]}",
            "{\"externalReferences\":[{\"unknownField\":123}]}"
    })
    void shouldNotEmitReferencesWithoutAnyKnownFieldsFromJson(String json) throws Exception {
        assertThat(parseJson(json)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = /* language=XML */ {
            "<bom><externalReferences><reference/></externalReferences></bom>",
            "<bom><externalReferences><reference><unknownField>123</unknownField></reference></externalReferences></bom>"
    })
    void shouldNotEmitReferencesWithoutAnyKnownFieldsFromXml(String xml) throws Exception {
        assertThat(parseXml(xml)).isEmpty();
    }

    @Test
    void shouldKeepReferencesWithKnownFieldsWhenOthersAreEmpty() throws Exception {
        final List<ExternalReference> references =
                parseJson(/* language=JSON */ """
                        {
                          "externalReferences": [
                            {},
                            { "type":"website", "url":"https://example.com" }
                          ]
                        }
                        """);

        assertThat(references).satisfiesExactly(reference -> {
            assertThat(reference.getType()).isEqualTo(ExternalReference.Type.WEBSITE);
            assertThat(reference.getUrl()).isEqualTo("https://example.com");
        });
    }

    @Test
    void shouldKeepReferencesWithOnlySomeKnownFields() throws Exception {
        final List<ExternalReference> references = parseJson(/* language=JSON */ """
                {
                  "externalReferences": [
                    { "type":"website" }
                  ]
                }
                """);

        assertThat(references).satisfiesExactly(reference -> {
            assertThat(reference.getType()).isEqualTo(ExternalReference.Type.WEBSITE);
            assertThat(reference.getUrl()).isNull();
        });
    }

    @Test
    void shouldDeserializeReferencesOfNestedXmlComponents() throws Exception {
        Bom bom = new XmlParser().parse(/* language=XML */ """
                <bom xmlns="http://cyclonedx.org/schema/bom/1.5" version="1">
                <metadata>
                  <tools>
                    <components>
                      <component type="application">
                        <name>Awesome Tool</name>
                        <version>9.1.2</version>
                        <externalReferences>
                          <reference type="website"><url>https://example.com</url></reference>
                          <reference type="vcs"><url>https://example.com/vcs</url></reference>
                        </externalReferences>
                      </component>
                    </components>
                  </tools>
                </metadata>
                </bom>
                """.getBytes(StandardCharsets.UTF_8));

        assertThat(bom.getMetadata().getToolChoice().getComponents().get(0).getExternalReferences())
                .satisfiesExactly(
                        reference -> assertThat(reference.getUrl()).isEqualTo("https://example.com"),
                        reference -> assertThat(reference.getUrl()).isEqualTo("https://example.com/vcs"));
    }

    private static List<ExternalReference> parseJson(String json) throws Exception {
        return new ObjectMapper().readValue(json, Bom.class).getExternalReferences();
    }

    private static List<ExternalReference> parseXml(String xml) throws Exception {
        return new XmlMapper().readValue(xml, Bom.class).getExternalReferences();
    }

}
