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
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cyclonedx.Version;
import org.cyclonedx.model.license.Expression;
import org.cyclonedx.model.license.ExpressionDetailed;
import org.cyclonedx.util.deserializer.LicenseChoiceDeserializer;

/**
 * Represents a choice of licenses for a component or service.
 * In CycloneDX 1.7+, this implements an item-level choice model where an array
 * can contain a mix of License, Expression, and ExpressionDetailed objects.
 * For earlier versions (1.6 and below), this enforces an array-level choice where
 * the entire array must be either all licenses, or a single expression, or a single
 * detailed expression.
 *
 * @since 9.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(using = LicenseChoiceDeserializer.class)
public class LicenseChoice {

    private List<LicenseItem> items;

    /**
     * Gets the list of license items. Each item can be a License, Expression, or ExpressionDetailed.
     * This is the primary API for CycloneDX 1.7+ support.
     */
    public List<LicenseItem> getItems() {
        return items;
    }

    public void setItems(List<LicenseItem> items) {
        this.items = items;
    }

    /**
     * Adds a license item to the choice
     */
    public void addItem(LicenseItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
    }

    /**
     * Convenience method to add a License
     */
    public void addLicense(License license) {
        addItem(LicenseItem.ofLicense(license));
    }

    /**
     * Convenience method to add an Expression
     */
    public void addExpression(Expression expression) {
        addItem(LicenseItem.ofExpression(expression));
    }

    /**
     * Convenience method to add an ExpressionDetailed
     */
    public void addExpressionDetailed(ExpressionDetailed expressionDetailed) {
        addItem(LicenseItem.ofExpressionDetailed(expressionDetailed));
    }

    // ========== Backward Compatibility Methods ==========

    /**
     * @deprecated Use {@link #getItems()} and filter by type instead.
     * Returns only License items for backward compatibility with pre-1.7 API.
     */
    @Deprecated
    @JsonIgnore
    public List<License> getLicenses() {
        if (items == null) return null;
        List<License> licenses = items.stream()
            .filter(item -> item.getLicense() != null)
            .map(LicenseItem::getLicense)
            .collect(Collectors.toList());
        return licenses.isEmpty() ? null : licenses;
    }

    /**
     * @deprecated Use {@link #setItems(List)} with LicenseItem.ofLicense() instead.
     * Sets licenses, clearing all other items. For backward compatibility with pre-1.7 API.
     */
    @Deprecated
    public void setLicenses(List<License> licenses) {
        if (licenses != null && !licenses.isEmpty()) {
            this.items = new ArrayList<>();
            for (License license : licenses) {
                this.items.add(LicenseItem.ofLicense(license));
            }
        } else {
            this.items = null;
        }
    }

    /**
     * @deprecated Use {@link #getItems()} and filter by type instead.
     * Returns the first Expression item for backward compatibility with pre-1.7 API.
     */
    @Deprecated
    @JsonIgnore
    public Expression getExpression() {
        if (items == null) return null;
        return items.stream()
            .filter(item -> item.getExpression() != null)
            .map(LicenseItem::getExpression)
            .findFirst()
            .orElse(null);
    }

    /**
     * @deprecated Use {@link #setItems(List)} with LicenseItem.ofExpression() instead.
     * Sets a single expression, clearing all other items. For backward compatibility with pre-1.7 API.
     */
    @Deprecated
    public void setExpression(Expression expression) {
        if (expression != null) {
            this.items = new ArrayList<>();
            this.items.add(LicenseItem.ofExpression(expression));
        } else {
            this.items = null;
        }
    }


    /**
     * Returns the first ExpressionDetailed item.
     * Note: This is part of the 1.7 API, not deprecated.
     */
    @JsonIgnore
    public ExpressionDetailed getExpressionDetailed() {
        if (items == null) return null;
        return items.stream()
            .filter(item -> item.getExpressionDetailed() != null)
            .map(LicenseItem::getExpressionDetailed)
            .findFirst()
            .orElse(null);
    }

    /**
     * Sets a single detailed expression, clearing all other items.
     * Note: This is part of the 1.7 API, not deprecated.
     */
    public void setExpressionDetailed(ExpressionDetailed expressionDetailed) {
        if (expressionDetailed != null) {
            this.items = new ArrayList<>();
            this.items.add(LicenseItem.ofExpressionDetailed(expressionDetailed));
        } else {
            this.items = null;
        }
    }

    /**
     * Validates whether this choice conforms to a specific version's constraints.
     * For 1.6 and below: array-level choice (all licenses OR single expression OR single expressionDetailed)
     * For 1.7+: item-level choice (mix allowed)
     */
    public boolean isValidForVersion(Version version) {
        if (items == null || items.isEmpty()) {
            return true;
        }

        if (version.getVersion() >= Version.VERSION_17.getVersion()) {
            // 1.7+: All items must be valid
            return items.stream().allMatch(LicenseItem::isValid);
        } else {
            // 1.6 and below: Array-level choice
            long licenseCount = items.stream().filter(i -> i.getLicense() != null).count();
            long expressionCount = items.stream().filter(i -> i.getExpression() != null).count();
            long expressionDetailedCount = items.stream().filter(i -> i.getExpressionDetailed() != null).count();

            // Must be: all licenses, OR single expression, OR single expressionDetailed
            return (licenseCount > 0 && expressionCount == 0 && expressionDetailedCount == 0) ||
                   (licenseCount == 0 && expressionCount == 1 && expressionDetailedCount == 0) ||
                   (licenseCount == 0 && expressionCount == 0 && expressionDetailedCount == 1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LicenseChoice)) return false;
        LicenseChoice that = (LicenseChoice) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}

