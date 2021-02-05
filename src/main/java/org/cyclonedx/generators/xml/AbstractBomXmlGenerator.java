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
package org.cyclonedx.generators.xml;

import java.io.IOException;
import java.io.StringReader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.model.Bom;
import org.cyclonedx.util.CollectionTypeSerializer;
import org.cyclonedx.util.VersionAnnotationIntrospector;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

abstract class AbstractBomXmlGenerator extends CycloneDxSchema implements BomXmlGenerator {

    private ObjectMapper mapper;

    Document doc;

    protected static String PROLOG = "<?xml version=\"1.0\"?>";

    protected void setupObjectMapper() {
        this.mapper = new XmlMapper();

        mapper.setAnnotationIntrospector(
            new VersionAnnotationIntrospector(
                String.valueOf(this.getSchemaVersion().getVersion())));

        if (this.getSchemaVersion().getVersion() == 1.0) {
            // NO-OP
        } else if (this.getSchemaVersion().getVersion() == 1.1) {
            registerDependencyModule(mapper, true);
        } else if (this.getSchemaVersion().getVersion() == 1.2) {
            registerDependencyModule(mapper, false);
        }
    }

    private void registerDependencyModule(final ObjectMapper mapper, final boolean useNamespace) {
        SimpleModule depModule = new SimpleModule();

        depModule.setSerializers(new CollectionTypeSerializer(useNamespace));
        mapper.registerModule(depModule);
    }

    /**
     * Constructs a new document builder with security features enabled.
     *
     * @return a new document builder
     * @throws javax.xml.parsers.ParserConfigurationException thrown if there is
     * a parser configuration exception
     */
    public DocumentBuilder buildSecureDocumentBuilder() throws ParserConfigurationException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder();
    }

    protected Document generateDocument(final Bom bom)
        throws ParserConfigurationException
    {
        try {
            final DocumentBuilder docBuilder = buildSecureDocumentBuilder();

            this.doc = docBuilder.parse(
                new InputSource(
                    new StringReader(
                        toXML(bom))));

            this.doc.setXmlStandalone(true);

            return this.doc;
        } catch (SAXException | ParserConfigurationException | IOException | GeneratorException ex) {
            throw new ParserConfigurationException(ex.toString());
        }
    }

    public String toXML(final Bom bom) throws GeneratorException {
        try {
            return PROLOG + this.mapper.writeValueAsString(bom);
        } catch (JsonProcessingException ex) {
            throw new GeneratorException(ex);
        }
    }

    /**
     * Creates a text representation of a CycloneDX BoM Document. This method
     * calls {@link #toXmlString()} and will return an empty string if {@link #toXmlString()}
     * throws an exception. Its preferred to call {@link #toXmlString()} directly
     * so that exceptions can be caught.
     * @return a String of the BoM
     * @since 1.1.0
     */
    @Override
    public String toString() {
        try {
            return toXmlString();
        } catch (Exception e) {
            return "";
        }
    }
}
