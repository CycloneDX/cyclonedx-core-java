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
package org.cyclonedx.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Traffic Light Protocol (TLP) classification for data sharing and distribution control.
 *
 * @since 10.0.0
 */
public enum TlpClassification {
    @JsonProperty("CLEAR")
    CLEAR("CLEAR"),
    @JsonProperty("GREEN")
    GREEN("GREEN"),
    @JsonProperty("AMBER")
    AMBER("AMBER"),
    @JsonProperty("AMBER+STRICT")
    AMBER_STRICT("AMBER+STRICT"),
    @JsonProperty("RED")
    RED("RED");

    private final String value;

    TlpClassification(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TlpClassification fromValue(String value) {
        if (value != null) {
            for (TlpClassification tlp : values()) {
                if (tlp.value.equalsIgnoreCase(value)) {
                    return tlp;
                }
            }
        }
        return null;
    }
}
