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
package org.cyclonedx.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.model.Attribute;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Hash;
import org.cyclonedx.model.License;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class BomUtils {

    private static final String NS_BOM = "http://cyclonedx.org/schema/bom/1.0";

    private BomUtils() { }

    /**
     * Returns the CycloneDX Schema from the specifications XSD.
     * @return a Schema
     * @throws SAXException a SAXException
     * @since 1.1.0
     */
    public static Schema getSchema() throws SAXException {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        // Use local copies of schemas rather than resolving from the net. It's faster, and less prone to errors.
        final Source[] schemaFiles = {
                new StreamSource(BomUtils.class.getClassLoader().getResourceAsStream("schema/cyclonedx/spdx.xsd")),
                new StreamSource(BomUtils.class.getClassLoader().getResourceAsStream("schema/cyclonedx/bom-1.0.xsd"))
        };
        return schemaFactory.newSchema(schemaFiles);
    }

    /**
     * Creates a CycloneDX BoM from a set of Components.
     * @param components the components to add to the BoM
     * @return an XML Document representing a CycloneDX BoM
     * @throws ParserConfigurationException an ParserConfigurationException
     * @since 1.0.0
     */
    public static Document createBom(Set<Component> components) throws ParserConfigurationException {
        final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        final Document doc = docBuilder.newDocument();
        doc.setXmlStandalone(true);

        // Create root <bom> node
        final Element bomNode = createRootElement(doc, "bom", null,
                new Attribute("xmlns", NS_BOM),
                new Attribute("version", "1"));

        final Element componentsNode = createElement(doc, bomNode, "components");

        if (components != null) {
            for (Component component : components) {
                final Element componentNode = createElement(doc, componentsNode, "component", null, new Attribute("type", component.getType()));

                createElement(doc, componentNode, "publisher", stripBreaks(component.getPublisher()));
                createElement(doc, componentNode, "group", stripBreaks(component.getGroup()));
                createElement(doc, componentNode, "name", stripBreaks(component.getName()));
                createElement(doc, componentNode, "version", stripBreaks(component.getVersion()));
                createElement(doc, componentNode, "description", stripBreaks(component.getDescription()));

                if (component.getHashes() != null) {
                    // Create the hashes node
                    final Element hashesNode = createElement(doc, componentNode, "hashes");
                    for (Hash hash : component.getHashes()) {
                        createElement(doc, hashesNode, "hash", hash.getValue(), new Attribute("alg", hash.getAlgorithm()));
                    }
                }

                if (component.getLicenses() != null) {
                    // Create the licenses node
                    final Element licensesNode = doc.createElementNS(NS_BOM, "licenses");
                    componentNode.appendChild(licensesNode);
                    for (License license : component.getLicenses()) {
                        // Create individual license node
                        final Element licenseNode = doc.createElementNS(NS_BOM, "license");
                        if (license.getId() != null) {
                            final Element licenseIdNode = doc.createElementNS(NS_BOM, "id");
                            licenseIdNode.appendChild(doc.createTextNode(license.getId()));
                            licenseNode.appendChild(licenseIdNode);
                        } else if (license.getName() != null) {
                            final Element licenseNameNode = doc.createElementNS(NS_BOM, "name");
                            licenseNameNode.appendChild(doc.createTextNode(license.getName()));
                            licenseNode.appendChild(licenseNameNode);
                        }
                        licensesNode.appendChild(licenseNode);
                    }
                }
                createElement(doc, componentNode, "cpe", stripBreaks(component.getCpe()));
                createElement(doc, componentNode, "purl", stripBreaks(component.getPurl()));
                createElement(doc, componentNode, "modified", String.valueOf(component.isModified()));
            }
        }
        return doc;
    }

    private static Element createElement(Document doc, Node parent, String name) {
        final Element node = doc.createElementNS(NS_BOM, name);
        parent.appendChild(node);
        return node;
    }

    private static Element createElement(Document doc, Node parent, String name, Object value) {
        return createElement(doc, parent, name, value, new Attribute[0]);
    }

    private static Element createElement(Document doc, Node parent, String name, Object value, Attribute... attributes) {
        Element node = null;
        if (value != null || attributes.length > 0) {
            node = doc.createElementNS(NS_BOM, name);
            for (Attribute attribute: attributes) {
                node.setAttribute(attribute.getKey(), attribute.getValue());
            }
            if (value != null) {
                node.appendChild(doc.createTextNode(value.toString()));
            }
            parent.appendChild(node);
        }
        return node;
    }

    private static Element createRootElement(Document doc, String name, Object value, Attribute... attributes) {
        Element node = null;
        if (value != null || attributes.length > 0) {
            node = doc.createElementNS(NS_BOM, name);
            node.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            node.setAttribute("xsi:schemaLocation", NS_BOM + " " + NS_BOM);
            for (Attribute attribute: attributes) {
                node.setAttribute(attribute.getKey(), attribute.getValue());
            }
            if (value != null) {
                node.appendChild(doc.createTextNode(value.toString()));
            }
            doc.appendChild(node);
        }
        return node;
    }

    /**
     * Creates a text representation of a CycloneDX BoM Document.
     * @param doc the XML BoM Document to convert to text
     * @return a String of the BoM
     * @throws TransformerException an TransformerException
     * @since 1.1.0
     */
    public static String toXmlString(Document doc) throws TransformerException {
        final DOMSource domSource = new DOMSource(doc);
        final StringWriter writer = new StringWriter();
        final StreamResult result = new StreamResult(writer);
        final TransformerFactory tf = TransformerFactory.newInstance();

        final Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(domSource, result);

        return writer.toString();
    }

    /**
     * Verifies a CycloneDX BoM conforms to the specification through XML validation.
     * @param file the CycloneDX BoM file to validate
     * @return a List of SAXParseExceptions. If the size of the list is 0, validation was successful
     * @since 1.0.0
     */
    public static List<SAXParseException> validateBom(File file) {
        final Source xmlFile = new StreamSource(file);
        final List<SAXParseException> exceptions = new LinkedList<>();
        try {
            final Schema schema = getSchema();
            final Validator validator = schema.newValidator();
            validator.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) {
                    exceptions.add(exception);
                }
                @Override
                public void fatalError(SAXParseException exception) {
                    exceptions.add(exception);
                }
                @Override
                public void error(SAXParseException exception) {
                    exceptions.add(exception);
                }
            });
            validator.validate(xmlFile);
        } catch (IOException | SAXException e) {
            // throw it away
        }
        return exceptions;
    }

    /**
     * Verifies a CycloneDX BoM conforms to the specification through XML validation.
     * @param file the CycloneDX BoM file to validate
     * @return true is the file is a valid BoM, false if not
     * @since 1.1.0
     */
    public static boolean isValid(File file) {
        return validateBom(file).size() == 0;
    }

    /**
     * Calculates the hashes of the specified file.
     * @param file the File to calculate hashes on
     * @return a List of Hash objets
     * @throws IOException an IOException
     * @since 1.0.0
     */
    public static List<Hash> calculateHashes(File file) throws IOException {
        if (file == null || !file.exists() || !file.canRead()) {
            return null;
        }
        final List<Hash> hashes = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file)) {
            hashes.add(new Hash(Hash.Algorithm.MD5, DigestUtils.md5Hex(fis)));
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            hashes.add(new Hash(Hash.Algorithm.SHA1, DigestUtils.sha1Hex(fis)));
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            hashes.add(new Hash(Hash.Algorithm.SHA_256, DigestUtils.sha256Hex(fis)));
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            hashes.add(new Hash(Hash.Algorithm.SHA_384, DigestUtils.sha384Hex(fis)));
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            hashes.add(new Hash(Hash.Algorithm.SHA_512, DigestUtils.sha512Hex(fis)));
        }
        return hashes;
    }

    private static String stripBreaks(String in) {
        if (in == null) {
            return null;
        }
        return in.trim().replace("\r\n", " ").replace("\n", " ").replace("\t", " ").replace("\r", " ").replaceAll(" +", " ");
    }

    /**
     * Parses a CycloneDX BOM.
     *
     * @param file the BOM
     * @return an Bom object
     * @throws ParseException when errors are encountered
     * @since 1.1.0
     */
    public static Bom parse(File file) throws ParseException {
        return parse(new StreamSource(file.getAbsolutePath()));
    }

    /**
     * Parses a CycloneDX BOM.
     *
     * @param bomBytes the BOM
     * @return an Bom object
     * @throws ParseException when errors are encountered
     * @since 1.1.0
     */
    public static Bom parse(byte[] bomBytes) throws ParseException {
        return parse(new StreamSource(new ByteArrayInputStream(bomBytes)));
    }

    /**
     * Parses a CycloneDX BOM.
     *
     * @param streamSource the BOM
     * @return an Bom object
     * @throws ParseException when errors are encountered
     * @since 1.1.0
     */
    private static Bom parse(StreamSource streamSource) throws ParseException {
        try {
            final Schema schema = getSchema();

            // Parse the native bom
            final JAXBContext jaxbContext = JAXBContext.newInstance(Bom.class);
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            // Prevent XML External Entity Injection
            final XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            final XMLStreamReader xsr = xif.createXMLStreamReader(streamSource);

            return (Bom) unmarshaller.unmarshal(xsr);
        } catch (JAXBException | XMLStreamException | SAXException e) {
            throw new ParseException(e);
        }
    }
}
