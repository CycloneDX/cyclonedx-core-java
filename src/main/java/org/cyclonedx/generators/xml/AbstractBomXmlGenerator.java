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

import java.io.IOException;
import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.DependencyList;
import org.cyclonedx.util.serializer.DependencySerializer;
import org.cyclonedx.util.serializer.InputTypeSerializer;
import org.cyclonedx.util.serializer.LifecycleSerializer;
import org.cyclonedx.util.VersionXmlAnnotationIntrospector;
import org.cyclonedx.util.serializer.OutputTypeSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;

public abstract class AbstractBomXmlGenerator extends CycloneDxSchema implements BomXmlGenerator {

    private final ObjectMapper mapper;

    private final DefaultXmlPrettyPrinter prettyPrinter;

	public AbstractBomXmlGenerator() {
        mapper = new XmlMapper();
        prettyPrinter = new DefaultXmlPrettyPrinter();
        setupObjectMapper(mapper);
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
	
    Document doc;

    protected static final String PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private void setupObjectMapper(final ObjectMapper mapper) {
        mapper.setAnnotationIntrospector(
            new VersionXmlAnnotationIntrospector(String.valueOf(this.getSchemaVersion().getVersion())));

        if (this.getSchemaVersion().getVersion() == 1.0) {
            // NO-OP
        }
        else {
            boolean useNamespace = this.getSchemaVersion().getVersion() == 1.1;
            registerDependencyModule(mapper, useNamespace);
        }

        SimpleModule lifecycleModule = new SimpleModule();
        lifecycleModule.addSerializer(new LifecycleSerializer(true));
        mapper.registerModule(lifecycleModule);

        SimpleModule inputTypeModule = new SimpleModule();
        inputTypeModule.addSerializer(new InputTypeSerializer(true));
        mapper.registerModule(inputTypeModule);

        SimpleModule outputTypeModule = new SimpleModule();
        outputTypeModule.addSerializer(new OutputTypeSerializer(false));
        mapper.registerModule(outputTypeModule);
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

            this.doc = docBuilder.parse(new InputSource(new StringReader(toXML(bom, false))));

            this.doc.setXmlStandalone(true);

            return this.doc;
        } catch (SAXException | ParserConfigurationException | IOException | GeneratorException ex) {
            throw new ParserConfigurationException(ex.toString());
        }
    }

    String toXML(final Bom bom, final boolean prettyPrint) throws GeneratorException {
        try {
            if (prettyPrint) {
                return PROLOG + System.lineSeparator() + mapper.writer(prettyPrinter).writeValueAsString(bom);
            }
            return PROLOG + mapper.writeValueAsString(bom);
        } catch (JsonProcessingException ex) {
            throw new GeneratorException(ex);
        }
    }

    /**
     * Creates a text representation of a CycloneDX BoM Document. This method
     * calls {@link #toXmlString()} and will return an empty string if {@link #toXmlString()}
     * throws an exception. It's preferred to call {@link #toXmlString()} directly
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
