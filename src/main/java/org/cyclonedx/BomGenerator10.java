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

import org.cyclonedx.model.Attribute;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Hash;
import org.cyclonedx.model.License;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * BomGenerator creates a CycloneDX bill-of-material document from a set of
 * {@link Component}s. Proper usage assumes {@link #generate()} is called after
 * construction and optionally followed by {@link #toXmlString()}.
 * @since 1.1.0
 */
public class BomGenerator10 extends AbstractBomGenerator implements BomGenerator {

    private final Set<Component> components;
    private Document doc;

    /**
     * Constructs a new BomGenerator object for the specified components.
     * @param components a BomGenerator object
     */
    BomGenerator10(final Set<Component> components) {
        this.components = components;
    }

    /**
     * Returns the version of the CycloneDX schema used by this instance
     * @return a CycloneDxSchemaVersion enum
     */
    public CycloneDxSchema.Version getSchemaVersion() {
        return CycloneDxSchema.Version.VERSION_10;
    }

    /**
     * Creates a CycloneDX BoM from a set of Components.
     * @return an XML Document representing a CycloneDX BoM
     * @since 1.1.0
     */
    public Document generate() throws ParserConfigurationException {
        final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        final Document doc = docBuilder.newDocument();
        doc.setXmlStandalone(true);

        // Create root <bom> node
        final Element bomNode = createRootElement(doc, "bom", null,
                new Attribute("xmlns", NS_BOM_10),
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
                    final Element licensesNode = doc.createElementNS(NS_BOM_10, "licenses");
                    componentNode.appendChild(licensesNode);
                    for (License license : component.getLicenses()) {
                        // Create individual license node
                        final Element licenseNode = doc.createElementNS(NS_BOM_10, "license");
                        if (license.getId() != null) {
                            final Element licenseIdNode = doc.createElementNS(NS_BOM_10, "id");
                            licenseIdNode.appendChild(doc.createTextNode(license.getId()));
                            licenseNode.appendChild(licenseIdNode);
                        } else if (license.getName() != null) {
                            final Element licenseNameNode = doc.createElementNS(NS_BOM_10, "name");
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
        this.doc = doc;
        return doc;
    }

    /**
     * Creates a text representation of a CycloneDX BoM Document.
     * @return a String of the BoM
     * @throws TransformerException an TransformerException
     * @since 1.1.0
     */
    public String toXmlString() throws TransformerException {
        if (this.doc == null) {
            return null;
        }
        final DOMSource domSource = new DOMSource(this.doc);
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
     * Creates a text representation of a CycloneDX BoM Document. This method
     * calls {@link #toXmlString()} and will return null if {@link #toXmlString()}
     * throws an exception. Its preferred to call {@link #toXmlString()} directly
     * so that exceptions can be caught.
     * @return a String of the BoM
     * @since 1.1.0
     */
    @Override
    public String toString() {
        try {
            return toXmlString();
        } catch (TransformerException e) {
            return null;
        }
    }
}
