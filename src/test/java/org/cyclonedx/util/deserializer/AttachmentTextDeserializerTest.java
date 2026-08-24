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
import org.cyclonedx.model.Component;
import org.cyclonedx.model.License;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AttachmentTextDeserializerTest {

    @Test
    void shouldSkipUnknownFields() throws Exception {
        final License license = parseLicense(/* language=JSON */ """
                {
                  "licenses": [
                    {
                      "license": {
                        "name": "Custom",
                        "text": {
                          "contentType": "text/plain",
                          "unknownField": { "nested": [ 1, 2, 3 ] },
                          "encoding": "base64",
                          "content": "abc"
                        },
                        "url": "https://example.com"
                      }
                    }
                  ]
                }
                """);

        assertThat(license.getAttachmentText().getContentType()).isEqualTo("text/plain");
        assertThat(license.getAttachmentText().getEncoding()).isEqualTo("base64");
        assertThat(license.getAttachmentText().getText()).isEqualTo("abc");
        assertThat(license.getUrl()).isEqualTo("https://example.com");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"licenses\":[{\"license\":{\"name\":\"C\",\"text\":{}}}]}",
            "{\"licenses\":[{\"license\":{\"name\":\"C\",\"text\":{\"unknownField\":123}}}]}",
            "{\"licenses\":[{\"license\":{\"name\":\"C\",\"text\":123}}]}"
    })
    void shouldNotEmitAttachmentTextWithoutAnyKnownFields(String json) throws Exception {
        assertThat(parseLicense(json).getAttachmentText()).isNull();
    }

    @Test
    void shouldEmitAttachmentTextForEmptyXmlElement() throws Exception {
        final License license = parseXmlLicense(/* language=XML */ """
                <component>
                  <licenses>
                    <license>
                      <name>C</name>
                      <text/>
                    </license>
                  </licenses>
                </component>
                """);

        assertThat(license.getAttachmentText()).isNotNull();
        assertThat(license.getAttachmentText().getText()).isEmpty();
    }

    private static License parseLicense(String json) throws Exception {
        return new ObjectMapper().readValue(json, Component.class).getLicenses().getLicenses().get(0);
    }

    private static License parseXmlLicense(String xml) throws Exception {
        return new XmlMapper().readValue(xml, Component.class).getLicenses().getLicenses().get(0);
    }
}
