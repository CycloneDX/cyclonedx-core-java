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
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.cyclonedx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.util.Objects;

@SuppressWarnings("unused")
public class Hash {

    public enum Algorithm {
        MD5("MD5"),
        SHA1("SHA-1"),
        SHA_256("SHA-256"),
        SHA_384("SHA-384"),
        SHA_512("SHA-512"),
        SHA3_256("SHA3-256"),
        SHA3_384("SHA3-384"),
        SHA3_512("SHA3-512"),
        BLAKE2b_256("BLAKE2b-256"),
        BLAKE2b_384("BLAKE2b-384"),
        BLAKE2b_512("BLAKE2b-512"),
        BLAKE3("BLAKE3");

        private String spec;

        Algorithm(String spec) {
            this.spec = spec;
        }

        public String getSpec() {
            return spec;
        }
    }

    @JacksonXmlProperty(localName = "alg", isAttribute = true)
    @JsonProperty("alg")
    private String algorithm;

    @JacksonXmlText
    @JsonProperty("content")
    private String value;

    public Hash() { }

    public Hash(Algorithm algorithm, String value) {
        this.algorithm = algorithm.getSpec();
        this.value = value;
    }

    public Hash(String algorithm, String value) {
        this.algorithm = algorithm;
        this.value = value;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hash hash = (Hash) o;
        return Objects.equals(algorithm, hash.algorithm) &&
                Objects.equals(value, hash.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithm, value);
    }
}
