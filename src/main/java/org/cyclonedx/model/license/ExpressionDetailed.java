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
package org.cyclonedx.model.license;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.Licensing;
import org.cyclonedx.model.Property;

import java.util.List;
import java.util.Objects;

/**
 * A detailed license expression with additional metadata for parts of the expression.
 *
 * @since 10.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({
    "expression", "expressionDetails", "acknowledgement", "bomRef", "licensing", "properties"
})
public class ExpressionDetailed extends ExtensibleElement {

    private String expression;

    @JacksonXmlElementWrapper(localName = "expressionDetails")
    @JacksonXmlProperty(localName = "expressionDetail")
    private List<ExpressionDetail> expressionDetails;

    @JacksonXmlProperty(isAttribute = true)
    private Acknowledgement acknowledgement;

    @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
    @JsonProperty("bom-ref")
    private String bomRef;

    private Licensing licensing;

    @JacksonXmlElementWrapper(localName = "properties")
    @JacksonXmlProperty(localName = "property")
    private List<Property> properties;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public List<ExpressionDetail> getExpressionDetails() {
        return expressionDetails;
    }

    public void setExpressionDetails(List<ExpressionDetail> expressionDetails) {
        this.expressionDetails = expressionDetails;
    }

    public Acknowledgement getAcknowledgement() {
        return acknowledgement;
    }

    public void setAcknowledgement(Acknowledgement acknowledgement) {
        this.acknowledgement = acknowledgement;
    }

    public String getBomRef() {
        return bomRef;
    }

    public void setBomRef(String bomRef) {
        this.bomRef = bomRef;
    }

    public Licensing getLicensing() {
        return licensing;
    }

    public void setLicensing(Licensing licensing) {
        this.licensing = licensing;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpressionDetailed)) return false;
        ExpressionDetailed that = (ExpressionDetailed) o;
        return Objects.equals(expression, that.expression) &&
                Objects.equals(expressionDetails, that.expressionDetails) &&
                acknowledgement == that.acknowledgement &&
                Objects.equals(bomRef, that.bomRef) &&
                Objects.equals(licensing, that.licensing) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, expressionDetails, acknowledgement, bomRef, licensing, properties);
    }
}
