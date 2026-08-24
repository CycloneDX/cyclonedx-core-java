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
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Hash;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HashesDeserializerTest {

    @Test
    void shouldDeserializeHashesOfNestedXmlComponents() throws Exception {
        Bom bom = new XmlParser().parse(/* language=XML */ """
                <bom xmlns="http://cyclonedx.org/schema/bom/1.5">
                <metadata>
                  <tools>
                    <components>
                      <component type="application">
                        <name>Awesome Tool</name>
                        <version>9.1.2</version>
                        <hashes>
                          <hash alg="MD5">2342c2eaf1feb9a80195dbaddf2ebaa3</hash>
                          <hash alg="SHA-1">68b78babe00a053f9e35ec6a2d9080f5b90122b0</hash>
                        </hashes>
                      </component>
                    </components>
                  </tools>
                </metadata>
                </bom>
                """.getBytes(StandardCharsets.UTF_8));

        assertThat(bom.getMetadata().getToolChoice().getComponents().get(0).getHashes()).satisfiesExactly(
                hash -> {
                    assertThat(hash.getAlgorithm()).isEqualTo("MD5");
                    assertThat(hash.getValue()).isEqualTo("2342c2eaf1feb9a80195dbaddf2ebaa3");
                },
                hash -> {
                    assertThat(hash.getAlgorithm()).isEqualTo("SHA-1");
                    assertThat(hash.getValue()).isEqualTo("68b78babe00a053f9e35ec6a2d9080f5b90122b0");
                });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"hashes\":[{}]}",
            "{\"hashes\":[null]}",
            "{\"hashes\":[\"nonsense\"]}",
            "{\"hashes\":[{\"unknownField\":123}]}"
    })
    void shouldNotEmitHashesWithoutAnyKnownFieldsFromJson(String json) throws Exception {
        assertThat(parseJson(json)).isEmpty();
    }

    @Test
    void shouldNotEmitHashesWithoutAnyKnownFieldsFromXml() throws Exception {
        assertThat(parseXml("<component><hashes><hash/></hashes></component>")).isEmpty();
    }

    @Test
    void shouldKeepHashesWithOnlySomeKnownFields() throws Exception {
        assertThat(parseJson("{\"hashes\":[{\"alg\":\"MD5\"}]}")).satisfiesExactly(hash -> {
            assertThat(hash.getAlgorithm()).isEqualTo("MD5");
            assertThat(hash.getValue()).isNull();
        });
    }

    private static List<Hash> parseJson(String json) throws Exception {
        return new ObjectMapper().readValue(json, Component.class).getHashes();
    }

    private static List<Hash> parseXml(String xml) throws Exception {
        return new XmlMapper().readValue(xml, Component.class).getHashes();
    }
}
