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
package org.cyclonedx;

import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * CycloneDxSchema is a base class that provides schema information to
 * {@link BomXmlGenerator}, {@link BomJsonGenerator}, and {@link org.cyclonedx.parsers.Parser}.
 * The class can be extended for other implementations as well.
 * @since 1.1.0
 */
public abstract class CycloneDxSchema {

    public static final String NS_BOM_10 = "http://cyclonedx.org/schema/bom/1.0";
    public static final String NS_BOM_11 = "http://cyclonedx.org/schema/bom/1.1";
    public static final String NS_BOM_12 = "http://cyclonedx.org/schema/bom/1.2";
    public static final String NS_DEPENDENCY_GRAPH_10 = "http://cyclonedx.org/schema/ext/dependency-graph/1.0";
    public static final String NS_BOM_LATEST = NS_BOM_12;
    public static final Version VERSION_LATEST = Version.VERSION_12;

    public enum Version {
        VERSION_10(CycloneDxSchema.NS_BOM_10, "1.0", 1.0),
        VERSION_11(CycloneDxSchema.NS_BOM_11, "1.1", 1.1),
        VERSION_12(CycloneDxSchema.NS_BOM_12, "1.2", 1.2);
        private String namespace;
        private String versionString;
        private double version;
        public String getNamespace() {
            return this.namespace;
        }
        public String getVersionString() {
            return versionString;
        }
        public double getVersion() {
            return version;
        }
        Version(String namespace, String versionString, double version) {
            this.namespace = namespace;
            this.versionString = versionString;
            this.version = version;
        }
    }

    /**
     * Returns the CycloneDX JSON Schema for the specified schema version.
     * @param schemaVersion The version to return the schema for
     * @param strict if true, the strict schema will be used which prohibits non-defined properties from being present in the BOM
     * @return a Schema
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    public org.everit.json.schema.Schema getJsonSchema(CycloneDxSchema.Version schemaVersion, boolean strict) throws IOException {
        return getJsonSchema12(strict);
    }

    /**
     * Returns the CycloneDX XML Schema for the specified schema version.
     * @param schemaVersion The version to return the schema for
     * @return a Schema
     * @throws SAXException a SAXException
     * @since 2.0.0
     */
    public Schema getXmlSchema(CycloneDxSchema.Version schemaVersion) throws SAXException {
        if (CycloneDxSchema.Version.VERSION_10 == schemaVersion) {
            return getXmlSchema10();
        } else if (CycloneDxSchema.Version.VERSION_11 == schemaVersion) {
            return getXmlSchema11();
        } else {
            return getXmlSchema12();
        }
    }

    /**
     * Returns the CycloneDX XML Schema from the specifications XSD.
     * @return a Schema
     * @throws SAXException a SAXException
     * @since 1.1.0
     */
    private Schema getXmlSchema10() throws SAXException {
        // Use local copies of schemas rather than resolving from the net. It's faster, and less prone to errors.
        return getXmlSchema(
                this.getClass().getClassLoader().getResourceAsStream("spdx.xsd"),
                this.getClass().getClassLoader().getResourceAsStream("bom-1.0.xsd")
        );
    }

    /**
     * Returns the CycloneDX XML Schema from the specifications XSD.
     * @return a Schema
     * @throws SAXException a SAXException
     * @since 2.0.0
     */
    private Schema getXmlSchema11() throws SAXException {
        // Use local copies of schemas rather than resolving from the net. It's faster, and less prone to errors.
        return getXmlSchema(
                this.getClass().getClassLoader().getResourceAsStream("spdx.xsd"),
                this.getClass().getClassLoader().getResourceAsStream("bom-1.1.xsd")
        );
    }

    /**
     * Returns the CycloneDX XML Schema from the specifications XSD.
     * @return a Schema
     * @throws SAXException a SAXException
     * @since 2.8.0
     */
    private Schema getXmlSchema12() throws SAXException {
        // Use local copies of schemas rather than resolving from the net. It's faster, and less prone to errors.
        return getXmlSchema(
                this.getClass().getClassLoader().getResourceAsStream("spdx.xsd"),
                this.getClass().getClassLoader().getResourceAsStream("bom-1.2.xsd")
        );
    }

    public Schema getXmlSchema(InputStream... inputStreams) throws SAXException {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Source[] schemaFiles = new Source[inputStreams.length];
        for (int i=0; i<inputStreams.length; i++) {
            schemaFiles[i] = new StreamSource(inputStreams[i]);
        }
        return schemaFactory.newSchema(schemaFiles);
    }

    private org.everit.json.schema.Schema getJsonSchema12(boolean strict) throws IOException {
        if (strict) {
            return getJsonSchema(this.getClass().getClassLoader().getResourceAsStream("bom-1.2-strict.schema.json"));
        } else {
            return getJsonSchema(this.getClass().getClassLoader().getResourceAsStream("bom-1.2.schema.json"));
        }
    }

    private org.everit.json.schema.Schema getJsonSchema(InputStream inputStream) throws IOException {
            final JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            return org.everit.json.schema.loader.SchemaLoader.load(rawSchema);
    }
}
