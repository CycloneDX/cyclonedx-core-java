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
package org.cyclonedx.util;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.SchemaFactory;

/**
 * Creates JAXP factories for XML processing, preferring the JDK's built-in system-default
 * implementations over the JAXP lookup mechanism.
 *
 * <p>The standard {@code newInstance()} lookup uses the classpath (ServiceLoader / system
 * properties), so an outdated XML parser leaking onto the classpath (e.g. Xerces 2.x pulled in
 * transitively by another library) would be picked up and break BOM parsing and validation with
 * errors like {@code Property 'http://javax.xml.XMLConstants/property/accessExternalDTD' is not
 * recognized}, because such parsers pre-date the JAXP 1.5 secure-processing properties.
 * See <a href="https://github.com/CycloneDX/cyclonedx-gradle-plugin/issues/349">cyclonedx-gradle-plugin#349</a>.</p>
 *
 * <p>The {@code newDefaultInstance()} factory methods only exist since Java 9 while this library
 * targets Java 8, so they are invoked reflectively, falling back to the standard lookup.</p>
 *
 * @since 13.1.0
 */
public final class XmlFactoryUtils
{
    private XmlFactoryUtils() {
    }

    /**
     * Creates a new {@link DocumentBuilderFactory}, preferring the JDK's built-in implementation.
     *
     * @return a new {@link DocumentBuilderFactory}
     */
    public static DocumentBuilderFactory newDocumentBuilderFactory() {
        try {
            return (DocumentBuilderFactory) DocumentBuilderFactory.class.getMethod("newDefaultInstance").invoke(null);
        } catch (ReflectiveOperationException e) {
            return DocumentBuilderFactory.newInstance();
        }
    }

    /**
     * Creates a new {@link SchemaFactory} for W3C XML Schema, preferring the JDK's built-in
     * implementation.
     *
     * @return a new {@link SchemaFactory}
     */
    public static SchemaFactory newSchemaFactory() {
        try {
            return (SchemaFactory) SchemaFactory.class.getMethod("newDefaultInstance").invoke(null);
        } catch (ReflectiveOperationException e) {
            return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        }
    }
}
