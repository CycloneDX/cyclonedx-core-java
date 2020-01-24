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

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CycloneDxSchema is a base class that provides schema information to
 * {@link BomGenerator10} and {@link BomParser}. The class can be extended
 * for other implementations as well.
 * @since 1.1.0
 */
public abstract class CycloneDxSchema {

    private static final Logger LOGGER = Logger.getLogger(CycloneDxSchema.class.getName());

    public static final String NS_BOM_10 = "http://cyclonedx.org/schema/bom/1.0";
    public static final String NS_BOM_11 = "http://cyclonedx.org/schema/bom/1.1";
    public static final String NS_BOM_LATEST = NS_BOM_11;
    public static final String NS_DEPENDENCY_GRAPH_10 = "http://cyclonedx.org/schema/ext/dependency-graph/1.0";
    public static final String NS_VULNERABILITY_10 = "http://cyclonedx.org/schema/ext/vulnerability/1.0";
    public static final String NS_BOM_DESCRIPTOR_10 = "http://cyclonedx.org/schema/ext/bom-descriptor/1.0";

    public enum Version {
        VERSION_10(CycloneDxSchema.NS_BOM_10, "1.0"),
        VERSION_11(CycloneDxSchema.NS_BOM_11, "1.1");
        private String namespace;
        private String versionString;
        public String getNamespace() {
            return this.namespace;
        }
        public String getVersionString() {
            return versionString;
        }
        Version(String namespace, String versionString) {
            this.namespace = namespace;
            this.versionString = versionString;
        }
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
        } else {
            return getXmlSchema11();
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
        final List<InputStream> streams = new ArrayList<>();
        streams.add(this.getClass().getClassLoader().getResourceAsStream("spdx.xsd"));
        streams.add(this.getClass().getClassLoader().getResourceAsStream("bom-1.1.xsd"));
        // Automatically load all schema extensions from 'resources/ext' directory
        streams.addAll(loadSchemaFromExtensions("ext"));

        return getXmlSchema(streams.toArray(new InputStream[0]));
    }

    protected List<InputStream> loadSchemaFromExtensions(String extLocation) {
        final List<InputStream> streams = new ArrayList<>();
        try {
            URL url = this.getClass().getClassLoader().getResource(extLocation);
            if (url != null ) {
                URI uri = url.toURI();
                if (uri.getScheme().equals("jar")) {
                    LOGGER.log(Level.FINE, "Provided location loaded as a jar", uri.toASCIIString());
                    FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                    Stream<Path> walk = Files.list(fileSystem.getPath(extLocation));
                    for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
                        Path p = it.next();
                        LOGGER.log(Level.FINE, "Reading resource ", p.getFileName().toString());
                        streams.add(this.getClass().getClassLoader().getResourceAsStream(extLocation + "/" + p.getFileName().toString()));
                    }
                    fileSystem.close();
                } else {
                    LOGGER.log(Level.FINE, "Provided location loaded as a regular file ", uri.toASCIIString());
                    Stream<Path> walk = Files.list(Paths.get(uri));
                    for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
                        Path p = it.next();
                        LOGGER.log(Level.FINE, "Reading resource ", p.getFileName().toString());
                        streams.add(this.getClass().getClassLoader().getResourceAsStream(extLocation + "/" + p.getFileName().toString()));
                    }
                }
            } else {
                LOGGER.log(Level.WARNING, "Unable to load the requested resource from url");
            }
        } catch(URISyntaxException use){
            LOGGER.log(Level.WARNING, "An error occurred attempting to load schema extensions", use);
        } catch(IOException io) {
            LOGGER.log(Level.WARNING, "An error occurred attempting to load schema extensions", io);
        }
        return streams;
    }

    public Schema getXmlSchema(InputStream... inputStreams) throws SAXException {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Source[] schemaFiles = new Source[inputStreams.length];
        for (int i=0; i<inputStreams.length; i++) {
            schemaFiles[i] = new StreamSource(inputStreams[i]);
        }
        return schemaFactory.newSchema(schemaFiles);
    }
}
