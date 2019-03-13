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
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.cyclonedx.model;

import org.cyclonedx.CycloneDxSchema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlRootElement(name = "licenses", namespace = CycloneDxSchema.NS_BOM_LATEST)
public class LicenseChoice {

    private List<License> licenses;
    private String expression;

    public List<License> getLicenses() {
        return licenses;
    }

    @XmlElement(name = "license", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
        this.expression = null;
    }

    public void addLicense(License license) {
        if (this.licenses == null) {
            this.licenses = new ArrayList<>();
        }
        this.licenses.add(license);
        this.expression = null;
    }

    public String getExpression() {
        return expression;
    }

    @XmlElement(name = "expression", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setExpression(String expression) {
        this.expression = expression;
        this.licenses = null;
    }

    public boolean isExpression() {
        return expression != null;
    }

    public boolean isResolved() {
        return licenses != null && !licenses.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LicenseChoice)) return false;
        LicenseChoice that = (LicenseChoice) o;
        return Objects.equals(licenses, that.licenses) &&
                Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licenses, expression);
    }
}
