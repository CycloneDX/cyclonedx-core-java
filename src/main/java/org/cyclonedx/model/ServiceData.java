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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import java.util.Objects;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
@JsonPropertyOrder({
    "classification",
    "flow"
})
public class ServiceData {

    public enum Flow {
        @JsonProperty("inbound")
        INBOUND("inbound"),
        @JsonProperty("outbound")
        OUTBOUND("outbound"),
        @JsonProperty("bi-directional")
        BI_DIRECTIONAL("bi-directional"),
        @JsonProperty("unknown")
        UNKNOWN("unknown");

        private final String name;

        public String getFlowName() {
            return this.name;
        }

        Flow(String name) {
            this.name = name;
        }
    }

    @JacksonXmlProperty(localName = "flow", isAttribute = true)
    @JsonProperty("flow")
    private Flow flow;

    @JacksonXmlText
    @JsonProperty("classification")
    private String classification;

    public ServiceData() { }

    public ServiceData(Flow flow, String classification) {
        this.flow = flow;
        this.classification = classification;
    }

    public ServiceData(String flow, String classification) {
        this.flow = Flow.valueOf(flow);
        this.classification = classification;
    }

    public Flow getFlow() {
        return flow;
    }

    public String getClassification() {
        return classification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceData serviceData = (ServiceData) o;
        return Objects.equals(flow, serviceData.flow) &&
                Objects.equals(classification, serviceData.classification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flow, classification);
    }
}
