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
package org.cyclonedx.generators.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;
import org.cyclonedx.Format;
import org.cyclonedx.generators.AbstractBomGenerator;
import org.cyclonedx.Version;
import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.model.Bom;
import org.cyclonedx.util.XmlFactoryUtils;
import org.cyclonedx.util.introspector.VersionXmlAnnotationIntrospector;
import org.cyclonedx.util.serializer.DependencySerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class BomXmlGenerator extends AbstractBomGenerator
{
    private final DefaultXmlPrettyPrinter prettyPrinter;

    /**
     * Constructs a new BomXmlGenerator object.
     * @param bom the BOM to generate
     * @param version the version of the CycloneDX schema to use.
     */
    public BomXmlGenerator(final Bom bom, final Version version) {
        super(version, bom, Format.XML);

        mapper = new XmlMapper();
        prettyPrinter = new DefaultXmlPrettyPrinter();
        setupObjectMapper();
        bom.setXmlns(version.getNamespace());
    }

    protected static final String PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private void setupObjectMapper() {
        mapper.setAnnotationIntrospector(new VersionXmlAnnotationIntrospector(getSchemaVersion()));

        if (version.getVersion() != 1.0) {
            boolean useNamespace = version.getVersion() == 1.1;
            registerDependencyModule(mapper, useNamespace);
        }
        super.setupObjectMapper(true);
    }

    private void registerDependencyModule(final ObjectMapper mapper, final boolean useNamespace) {
        SimpleModule depModule = new SimpleModule();
        depModule.addSerializer(new DependencySerializer(useNamespace, null));
        mapper.registerModule(depModule);
    }

    /**
     * Constructs a new document builder with security features enabled.
     *
     * @return a new document builder
     * @throws javax.xml.parsers.ParserConfigurationException thrown if there is a parser configuration exception
     */
    private DocumentBuilder buildSecureDocumentBuilder() throws ParserConfigurationException {
        final DocumentBuilderFactory factory = XmlFactoryUtils.newDocumentBuilderFactory();
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
            Document doc = docBuilder.parse(new InputSource(new StringReader(toXML(bom, false))));
            doc.setXmlStandalone(true);
            return doc;
        }
        catch (SAXException | ParserConfigurationException | IOException | GeneratorException ex) {
            throw new ParserConfigurationException(ex.toString());
        }
    }

    String toXML(final Bom bom, final boolean prettyPrint) throws GeneratorException {
        try {
            if (prettyPrint) {
                return PROLOG + System.lineSeparator() + mapper.writer(prettyPrinter).writeValueAsString(bom);
            }
            return PROLOG + mapper.writeValueAsString(bom);
        }
        catch (JsonProcessingException ex) {
            throw new GeneratorException(ex);
        }
    }

    /**
     * Creates a CycloneDX BoM from a set of Components.
     * @return an XML Document representing a CycloneDX BoM
     * @since 1.1.0
     * @throws ParserConfigurationException if an error occurs
     */
    public Document generate() throws ParserConfigurationException {
        return generateDocument(bom);
    }


    public String toXmlString() throws GeneratorException {
        return toXML(bom, true);
    }

    public String toXmlString(boolean prettyPrint) throws GeneratorException {
        return toXML(bom, prettyPrint);
    }

    /**
     * @since 13.5.0
     */
    public byte[] toXmlBytes(boolean prettyPrint) throws GeneratorException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeXml(out, prettyPrint);
        return out.toByteArray();
    }

    /**
     * @since 13.5.0
     */
    public void writeXml(OutputStream out, boolean prettyPrint) throws GeneratorException {
        try {
            out.write((prettyPrint ? PROLOG + System.lineSeparator() : PROLOG).getBytes(StandardCharsets.UTF_8));
            final ObjectWriter writer = prettyPrint ? mapper.writer(prettyPrinter) : mapper.writer();
            writer.without(JsonGenerator.Feature.AUTO_CLOSE_TARGET).writeValue(out, bom);
        }
        catch (IOException ex) {
            throw new GeneratorException(ex);
        }
    }

    /**
     * Creates a text representation of a CycloneDX BoM Document. This method calls {@link #toXmlString()} and will return
     * an empty string if {@link #toXmlString()} throws an exception. It's preferred to call {@link #toXmlString()}
     * directly so that exceptions can be caught.
     *
     * @return a String of the BoM
     * @since 1.1.0
     *
     */
    @Override
    public String toString() {
        try {
            return toXML(bom, true);
        }
        catch (GeneratorException e) {
            return "";
        }
    }
}
