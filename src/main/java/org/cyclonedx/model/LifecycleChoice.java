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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LifecycleChoice
{
    @JsonProperty("phase")
    @JacksonXmlProperty(localName = "phase")
    private Phase phase;

    @JsonProperty("name")
    @JacksonXmlProperty(localName = "phase")
    private String name;

    @JsonProperty("description")
    @JacksonXmlProperty(localName = "phase")
    private String description;

    public enum Phase
    {
        @JsonProperty("design")
        DESIGN("design"),
        @JsonProperty("pre-build")
        PRE_BUILD("pre-build"),
        @JsonProperty("build")
        BUILD("build"),
        @JsonProperty("post-build")
        POST_BUILD("post-build"),
        @JsonProperty("operations")
        OPERATIONS("operations"),
        @JsonProperty("discovery")
        DISCOVERY("discovery"),
        @JsonProperty("decommission")
        DECOMMISSION("decommission");

        private final String name;

        public String getPhaseName() {
            return this.name;
        }

        Phase(String name) {
            this.name = name;
        }

        @JsonCreator
        public static Phase fromString(String value) {
            for (Phase phase : Phase.values()) {
                if (phase.name.equalsIgnoreCase(value)) {
                    return phase;
                }
            }
            throw new IllegalArgumentException("Invalid phase: " + value);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(final Phase phase) {
        this.phase = phase;
    }
}
