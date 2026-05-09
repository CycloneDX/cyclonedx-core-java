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

import java.util.Objects;

import org.cyclonedx.model.license.Expression;
import org.cyclonedx.model.license.ExpressionDetailed;

/**
 * Represents a single item in a license choice, where each item can be one of:
 * - A License
 * - An Expression
 * - An ExpressionDetailed
 *
 * This implements the CycloneDX 1.7 item-level choice model, where an array can
 * contain a mix of different license types.
 *
 * @since 9.0.0
 */
public class LicenseItem {

    private License license;
    private Expression expression;
    private ExpressionDetailed expressionDetailed;

    /**
     * Default constructor for deserialization
     */
    public LicenseItem() {
    }

    /**
     * Private constructor to enforce factory methods
     */
    private LicenseItem(License license, Expression expression, ExpressionDetailed expressionDetailed) {
        this.license = license;
        this.expression = expression;
        this.expressionDetailed = expressionDetailed;
    }

    /**
     * Creates a LicenseItem containing a License
     */
    public static LicenseItem ofLicense(License license) {
        if (license == null) {
            throw new IllegalArgumentException("License cannot be null");
        }
        return new LicenseItem(license, null, null);
    }

    /**
     * Creates a LicenseItem containing an Expression
     */
    public static LicenseItem ofExpression(Expression expression) {
        if (expression == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
        return new LicenseItem(null, expression, null);
    }

    /**
     * Creates a LicenseItem containing an ExpressionDetailed
     */
    public static LicenseItem ofExpressionDetailed(ExpressionDetailed expressionDetailed) {
        if (expressionDetailed == null) {
            throw new IllegalArgumentException("ExpressionDetailed cannot be null");
        }
        return new LicenseItem(null, null, expressionDetailed);
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        if (license != null) {
            this.expression = null;
            this.expressionDetailed = null;
        }
        this.license = license;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        if (expression != null) {
            this.license = null;
            this.expressionDetailed = null;
        }
        this.expression = expression;
    }

    public ExpressionDetailed getExpressionDetailed() {
        return expressionDetailed;
    }

    public void setExpressionDetailed(ExpressionDetailed expressionDetailed) {
        if (expressionDetailed != null) {
            this.license = null;
            this.expression = null;
        }
        this.expressionDetailed = expressionDetailed;
    }

    /**
     * Returns the type of license item
     */
    public LicenseItemType getType() {
        if (license != null) return LicenseItemType.LICENSE;
        if (expression != null) return LicenseItemType.EXPRESSION;
        if (expressionDetailed != null) return LicenseItemType.EXPRESSION_DETAILED;
        return LicenseItemType.NONE;
    }

    /**
     * Validates that exactly one field is set
     */
    public boolean isValid() {
        int count = 0;
        if (license != null) count++;
        if (expression != null) count++;
        if (expressionDetailed != null) count++;
        return count == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LicenseItem)) return false;
        LicenseItem that = (LicenseItem) o;
        return Objects.equals(license, that.license) &&
               Objects.equals(expression, that.expression) &&
               Objects.equals(expressionDetailed, that.expressionDetailed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(license, expression, expressionDetailed);
    }

    public enum LicenseItemType {
        LICENSE,
        EXPRESSION,
        EXPRESSION_DETAILED,
        NONE
    }
}
