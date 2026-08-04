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
import javax.xml.parsers.SAXParserFactory;
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
 * <p>An implementation explicitly requested via the JAXP system properties
 * ({@code javax.xml.parsers.DocumentBuilderFactory} /
 * {@code javax.xml.validation.SchemaFactory:<schemaLanguage>}) is still honored, as that is a
 * deliberate configuration choice rather than an accidental classpath leak.</p>
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
     * Creates a new {@link DocumentBuilderFactory}, preferring the JDK's built-in implementation
     * unless one is explicitly requested via the {@code javax.xml.parsers.DocumentBuilderFactory}
     * system property.
     *
     * @return a new {@link DocumentBuilderFactory}
     */
    public static DocumentBuilderFactory newDocumentBuilderFactory() {
        if (System.getProperty(DocumentBuilderFactory.class.getName()) == null) {
            try {
                return (DocumentBuilderFactory) DocumentBuilderFactory.class.getMethod("newDefaultInstance").invoke(null);
            } catch (ReflectiveOperationException e) {
                // Java 8: fall back to the standard lookup below
            }
        }
        return DocumentBuilderFactory.newInstance();
    }

    /**
     * Creates a new {@link SAXParserFactory}, preferring the JDK's built-in implementation
     * unless one is explicitly requested via the {@code javax.xml.parsers.SAXParserFactory}
     * system property.
     *
     * @return a new {@link SAXParserFactory}
     */
    public static SAXParserFactory newSAXParserFactory() {
        if (System.getProperty(SAXParserFactory.class.getName()) == null) {
            try {
                return (SAXParserFactory) SAXParserFactory.class.getMethod("newDefaultInstance").invoke(null);
            } catch (ReflectiveOperationException e) {
                // Java 8: fall back to the standard lookup below
            }
        }
        return SAXParserFactory.newInstance();
    }

    /**
     * Creates a new {@link SchemaFactory} for W3C XML Schema, preferring the JDK's built-in
     * implementation unless one is explicitly requested via the
     * {@code javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema} system property.
     *
     * @return a new {@link SchemaFactory}
     */
    public static SchemaFactory newSchemaFactory() {
        if (System.getProperty(SchemaFactory.class.getName() + ":" + XMLConstants.W3C_XML_SCHEMA_NS_URI) == null) {
            try {
                return (SchemaFactory) SchemaFactory.class.getMethod("newDefaultInstance").invoke(null);
            } catch (ReflectiveOperationException e) {
                // Java 8: fall back to the standard lookup below
            }
        }
        return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    }
}
