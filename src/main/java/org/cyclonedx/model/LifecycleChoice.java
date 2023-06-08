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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class LifecycleChoice
{
    private PhaseClass phaseClass;
    private PhaseEnum phaseEnum;

    public enum PhaseEnum {
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

        PhaseEnum(String name) {
            this.name = name;
        }

        @JsonCreator
        public static PhaseEnum fromString(String value) {
            for (PhaseEnum phase : PhaseEnum.values()) {
                if (phase.name.equalsIgnoreCase(value)) {
                    return phase;
                }
            }
            throw new IllegalArgumentException("Invalid phase: " + value);
        }
    }

    public static class PhaseClass {

        private String name;

        private String description;

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public void setDescription(final String description) {
            this.description = description;
        }
    }

    @JacksonXmlElementWrapper(useWrapping = false)
    public PhaseClass getPhaseClass() {
        return phaseClass;
    }

    public void setPhaseClass(final PhaseClass phaseClass) {
        this.phaseClass = phaseClass;
    }

    @JacksonXmlElementWrapper(useWrapping = false)
    public PhaseEnum getPhaseEnum() {
        return phaseEnum;
    }

    public void setPhaseEnum(final PhaseEnum phaseEnum) {
        this.phaseEnum = phaseEnum;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LifecycleChoice)) {
            return false;
        }
        LifecycleChoice that = (LifecycleChoice) o;
        return Objects.equals(phaseClass, that.phaseClass) && phaseEnum == that.phaseEnum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phaseClass, phaseEnum);
    }
}