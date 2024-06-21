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
package org.cyclonedx.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.Version;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.model.Bom;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * XmlParser is responsible for validating and parsing CycloneDX bill-of-material
 * XML documents and returning a {@link Bom} object.
 * @since 3.0.0
 */
@SuppressWarnings("unused")
public class XmlParser extends CycloneDxSchema implements Parser {

    private final ObjectMapper mapper;

    public XmlParser() {
        mapper = new XmlMapper();
    }

    private static final Map<String, String> NAMESPACE_TO_VERSION_MAP = new HashMap<>();

    static {
        for (Version version : Version.values()) {
            NAMESPACE_TO_VERSION_MAP.put(version.getNamespace(), version.getVersionString());
        }
    }

    /**
     * {@inheritDoc}
     */
    public Bom parse(final File file) throws ParseException {
        try {
            final String schemaVersion = identifySchemaVersion(new InputSource(Files.newInputStream(file.toPath())));

            return injectSchemaVersion(mapper.readValue(file, Bom.class), schemaVersion);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParseException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Bom parse(final byte[] bomBytes) throws ParseException {
        try {
            final String schemaVersion = identifySchemaVersion(new InputSource(new ByteArrayInputStream(bomBytes)));

            return injectSchemaVersion(mapper.readValue(bomBytes, Bom.class), schemaVersion);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new ParseException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Bom parse(final InputStream inputStream) throws ParseException {
        try {
            return mapper.readValue(inputStream, Bom.class);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Bom parse(final Reader reader) throws ParseException {
        try {
            return mapper.readValue(reader, Bom.class);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    /**
     * Uses reflection to set the schemaVersion field inside a Bom instance.
     * The schemaVersion is 'not user serviceable' so no methods for setting
     * it are provided, other than the constructor, which xstream does not
     * use.
     * @param bom the Bom to set the schemaVersion for
     * @param schemaVersion the value of the schema version
     * @return the updated Bom
     * @since 3.0.0
     */
    private Bom injectSchemaVersion(final Bom bom, final String schemaVersion) {
        try {
            final Field field = Bom.class.getDeclaredField("specVersion");
            field.setAccessible(true);
            field.set(bom, schemaVersion);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // throw it away
        }
        return bom;
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final File file) throws IOException {
        return validate(file, CycloneDxSchema.VERSION_LATEST);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final File file, final Version schemaVersion) throws IOException {
        final Source source = new StreamSource(file);
        return validate(source, schemaVersion);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final byte[] bomBytes) throws IOException {
        return validate(bomBytes, CycloneDxSchema.VERSION_LATEST);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final byte[] bomBytes, final Version schemaVersion) throws IOException {
        final Source source = new StreamSource(new ByteArrayInputStream(bomBytes));
        return validate(source, schemaVersion);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final Reader reader) throws IOException {
        return validate(reader, CycloneDxSchema.VERSION_LATEST);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final Reader reader, final Version schemaVersion) throws IOException {
        final Source source = new StreamSource(reader);
        return validate(source, schemaVersion);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final InputStream inputStream) throws IOException {
        return validate(inputStream, CycloneDxSchema.VERSION_LATEST);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final InputStream inputStream, final Version schemaVersion) throws IOException {
        final Source source = new StreamSource(inputStream);
        return validate(source, schemaVersion);
    }

    public List<ParseException> validate(final Source source, final Version schemaVersion) throws IOException {
        final List<ParseException> exceptions = new LinkedList<>();
        try {
            final Schema schema = getXmlSchema(schemaVersion);
            final Validator validator = schema.newValidator();
            validator.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException e) {
                    exceptions.add(new ParseException(e.getMessage(), e));
                }

                @Override
                public void fatalError(SAXParseException e) {
                    exceptions.add(new ParseException(e.getMessage(), e));
                }

                @Override
                public void error(SAXParseException e) {
                    exceptions.add(new ParseException(e.getMessage(), e));
                }
            });
            validator.validate(source);
        } catch (SAXException e) {
            exceptions.add(new ParseException(e.getMessage(), e));
        }
        return exceptions;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final File file) throws IOException {
        return validate(file).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final File file, final Version schemaVersion) throws IOException {
        return validate(file, schemaVersion).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final byte[] bomBytes) throws IOException {
        return validate(bomBytes).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final byte[] bomBytes, final Version schemaVersion) throws IOException {
        return validate(bomBytes, schemaVersion).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final Reader reader) throws IOException {
        return validate(reader).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final Reader reader, final Version schemaVersion) throws IOException {
        return validate(reader, schemaVersion).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final InputStream inputStream) throws IOException {
        return validate(inputStream).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final InputStream inputStream, final Version schemaVersion) throws IOException {
        return validate(inputStream, schemaVersion).isEmpty();
    }

    private String identifySchemaVersion(final InputSource in)
        throws ParserConfigurationException, IOException, SAXException
    {

        List<String> namespaces = extractAllNamespaceDeclarations(in);

        for (String namespaceUri : namespaces) {
            String versionString = NAMESPACE_TO_VERSION_MAP.get(namespaceUri);
            if (versionString != null) {
                return versionString;
            }
        }
        return null;
    }

    private List<String> extractAllNamespaceDeclarations(final InputSource in)
        throws ParserConfigurationException, IOException, SAXException
    {
        Document doc = createSecureDocument(in);

        // Extract all namespaces, including the default namespace
        List<String>namespaces = new ArrayList<>();
        extractNamespaces(doc.getDocumentElement(), namespaces);

        return namespaces;
    }

    private void extractNamespaces(Node node, List<String> namespaces) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            NamedNodeMap attributes = node.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attr = attributes.item(i);
                if (attr.getNodeName().equals("xmlns")) {
                    namespaces.add(attr.getNodeValue());
                }
            }
        }
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            extractNamespaces(children.item(i), namespaces);
        }
    }

    private Document createSecureDocument(InputSource in) throws ParserConfigurationException, IOException, SAXException
    {
        //https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html#xpathexpression
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        DocumentBuilder builder = df.newDocumentBuilder();
        return builder.parse(in);
    }
}
