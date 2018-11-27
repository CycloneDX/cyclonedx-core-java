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
package org.cyclonedx;

import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

/**
 * CycloneDxSchema is a base class that provides schema information to
 * {@link BomGenerator} and {@link BomParser}. The class can be extended
 * for other implementations as well.
 * @since 1.1.0
 */
public abstract class CycloneDxSchema {

    public static final String NS_BOM = "http://cyclonedx.org/schema/bom/1.0";

    /**
     * Returns the CycloneDX XML Schema from the specifications XSD.
     * @return a Schema
     * @throws SAXException a SAXException
     * @since 1.1.0
     */
    public Schema getXmlSchema() throws SAXException {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        // Use local copies of schemas rather than resolving from the net. It's faster, and less prone to errors.
        final Source[] schemaFiles = {
                new StreamSource(this.getClass().getClassLoader().getResourceAsStream("spdx.xsd")),
                new StreamSource(this.getClass().getClassLoader().getResourceAsStream("bom-1.0.xsd"))
        };
        return schemaFactory.newSchema(schemaFiles);
    }
}
