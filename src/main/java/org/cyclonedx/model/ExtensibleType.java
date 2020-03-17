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

import java.util.List;

public class ExtensibleType {

    private final String namespace;
    private final String name;
    private List<Attribute> attributes;
    private String value;
    private List<ExtensibleType> children;

    public ExtensibleType(final String namespace, final String name, final List<Attribute> attributes, final String value) {
        this.namespace = namespace;
        this.name = name;
        this.attributes = attributes;
        this.value = value;
    }

    public ExtensibleType(final String namespace, final String name, final List<Attribute> attributes, final List<ExtensibleType> children) {
        this.namespace = namespace;
        this.name = name;
        this.attributes = attributes;
        this.children = children;
    }

    public ExtensibleType(final String namespace, final String name, final String value) {
        this.namespace = namespace;
        this.name = name;
        this.value = value;
    }

    public ExtensibleType(final String namespace, final String name, final List<ExtensibleType> children) {
        this.namespace = namespace;
        this.name = name;
        this.children = children;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public String getValue() {
        return value;
    }

    public List<ExtensibleType> getChildren() {
        return children;
    }
}
