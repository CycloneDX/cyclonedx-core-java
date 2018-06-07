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
import org.cyclonedx.model.Attribute;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

    public static Document createBom(Set<Component> components) throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        doc.setXmlStandalone(true);

        // Create root <bom> node
        Element bomNode = createRootElement(doc, "bom", null,
                new Attribute("xmlns", NS_BOM),
                new Attribute("version", "1"));

        Element componentsNode = createElement(doc, bomNode, "components");

        if (components != null) {
            for (Component component : components) {
                Element componentNode = createElement(doc, componentsNode, "component", null, new Attribute("type", component.getType()));

                createElement(doc, componentNode, "publisher", stripBreaks(component.getPublisher()));
                createElement(doc, componentNode, "group", stripBreaks(component.getGroup()));
                createElement(doc, componentNode, "name", stripBreaks(component.getName()));
                createElement(doc, componentNode, "version", stripBreaks(component.getVersion()));
                createElement(doc, componentNode, "description", stripBreaks(component.getDescription()));

                if (component.getHashes() != null) {
                    // Create the hashes node
                    Element hashesNode = createElement(doc, componentNode, "hashes");
                    for (Hash hash : component.getHashes()) {
                        createElement(doc, hashesNode, "hash", hash.getValue(), new Attribute("alg", hash.getAlgorithm()));
                    }
                }

                if (component.getLicenses() != null) {
                    // Create the licenses node
                    Element licensesNode = doc.createElementNS(NS_BOM, "licenses");
                    componentNode.appendChild(licensesNode);
                    for (License license : component.getLicenses()) {
                        // Create individual license node
                        Element licenseNode = doc.createElementNS(NS_BOM, "license");
                        if (license.getId() != null) {
                            Element licenseIdNode = doc.createElementNS(NS_BOM, "id");
                            licenseIdNode.appendChild(doc.createTextNode(license.getId()));
                            licenseNode.appendChild(licenseIdNode);
                        } else if (license.getName() != null) {
                            Element licenseNameNode = doc.createElementNS(NS_BOM, "name");
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
        Element node = doc.createElementNS(NS_BOM, name);
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

    public static String toString(Document doc) throws TransformerException {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();

        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(domSource, result);

        return writer.toString();
    }

    public static List<SAXParseException> validateBom(ClassLoader classLoader, File file) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        // Use local copies of schemas rather than resolving from the net. It's faster, and less prone to errors.
        Source[] schemaFiles = {
                new StreamSource(classLoader.getResourceAsStream("spdx.xsd")),
                new StreamSource(classLoader.getResourceAsStream("bom-1.0.xsd"))
        };
        Source xmlFile = new StreamSource(file);
        final List<SAXParseException> exceptions = new LinkedList<>();
        try {
            Schema schema = schemaFactory.newSchema(schemaFiles);
            Validator validator = schema.newValidator();
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

    public static List<Hash> calculateHashes(File file) throws IOException {
        if (file == null || !file.exists() || !file.canRead()) {
            return null;
        }
        List<Hash> hashes = new ArrayList<>();

        FileInputStream fis = new FileInputStream(file);
        hashes.add(new Hash("MD5", DigestUtils.md5Hex(fis)));
        fis.close();

        fis = new FileInputStream(file);
        hashes.add(new Hash("SHA-1", DigestUtils.sha1Hex(fis)));
        fis.close();

        fis = new FileInputStream(file);
        hashes.add(new Hash("SHA-256", DigestUtils.sha256Hex(fis)));
        fis.close();

        fis = new FileInputStream(file);
        hashes.add(new Hash("SHA-384", DigestUtils.sha384Hex(fis)));
        fis.close();

        fis = new FileInputStream(file);
        hashes.add(new Hash("SHA-512", DigestUtils.sha512Hex(fis)));
        fis.close();

        return hashes;
    }

    private static String stripBreaks(String in) {
        if (in == null) {
            return null;
        }
        return in.trim().replace("\r\n", " ").replace("\n", " ").replace("\t", " ").replace("\r", " ").replaceAll(" +", " ");
    }

}
