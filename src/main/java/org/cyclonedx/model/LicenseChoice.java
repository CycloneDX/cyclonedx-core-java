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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.model.license.Expression;
import org.cyclonedx.util.deserializer.LicenseDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(using = LicenseDeserializer.class)
public class LicenseChoice {

    private List<License> license;
    private Expression expression;

    @JacksonXmlProperty(localName = "license")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<License> getLicenses() {
        return license;
    }

    public void setLicenses(List<License> licenses) {
        this.license = licenses;
        this.expression = null;
    }

    public void addLicense(License license) {
        if (this.license == null) {
            this.license = new ArrayList<>();
        }
        this.license.add(license);
        this.expression = null;
    }

    @JacksonXmlProperty(localName = "expression")
    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
        this.license = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LicenseChoice)) return false;
        LicenseChoice that = (LicenseChoice) o;
        return Objects.equals(license, that.license) &&
                Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(license, expression);
    }
}
