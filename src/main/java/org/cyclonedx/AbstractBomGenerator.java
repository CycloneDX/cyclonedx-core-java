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

import org.cyclonedx.model.Attribute;
import org.cyclonedx.model.Hash;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

abstract class AbstractBomGenerator extends CycloneDxSchema implements BomGenerator {

    Document doc;

    Element createElement(Node parent, String name) {
        final Element node = doc.createElement(name);
        parent.appendChild(node);
        return node;
    }

    Element createElement(Node parent, String name, Object value) {
        return createElement(parent, name, value, new Attribute[0]);
    }

    Element createElement(Node parent, String name, Object value, Attribute... attributes) {
        Element node = null;
        if (value != null || attributes.length > 0) {
            if (name.contains(":")) {
                node = doc.createElement(name);
            } else {
                node = doc.createElementNS(getSchemaVersion().getNamespace(), name);
            }
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

    void createHashesNode(Node parent, List<Hash> hashes) {
        if (hashes != null && !hashes.isEmpty()) {
            final Element hashesNode = createElement(parent, "hashes");
            for (Hash hash : hashes) {
                createElement(hashesNode, "hash", hash.getValue(), new Attribute("alg", hash.getAlgorithm()));
            }
        }
    }

    void createLicenseNode(Node parent, LicenseChoice licenseChoice, boolean expressionSupport) {
        if (licenseChoice != null) {
            final Element licensesNode = doc.createElement("licenses");
            if (licenseChoice.getLicenses() != null) {
                parent.appendChild(licensesNode);
                for (License license : licenseChoice.getLicenses()) {
                    // Create individual license node
                    final Element licenseNode = doc.createElement("license");
                    if (license.getId() != null) {
                        final Element licenseIdNode = doc.createElement("id");
                        licenseIdNode.appendChild(doc.createTextNode(license.getId()));
                        licenseNode.appendChild(licenseIdNode);
                    } else if (license.getName() != null) {
                        final Element licenseNameNode = doc.createElement("name");
                        licenseNameNode.appendChild(doc.createTextNode(license.getName()));
                        licenseNode.appendChild(licenseNameNode);
                    }
                    licensesNode.appendChild(licenseNode);
                }
            } else if (expressionSupport && licenseChoice.getExpression() != null) {
                createElement(licensesNode, "expression", stripBreaks(licenseChoice.getExpression()));
            }
        }
    }

    Element createRootElement(String name, Object value, Attribute... attributes) {
        Element node = null;
        if (value != null || attributes.length > 0) {
            node = doc.createElementNS(getSchemaVersion().getNamespace(), name);
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

    static String stripBreaks(String in) {
        if (in == null) {
            return null;
        }
        return in.trim().replace("\r\n", " ").replace("\n", " ").replace("\t", " ").replace("\r", " ").replaceAll(" +", " ");
    }

    /**
     * Creates a text representation of a CycloneDX BoM Document.
     * @return a String of the BoM
     * @throws TransformerException an TransformerException
     * @since 1.1.0
     */
    public String toXmlString() throws TransformerException {
        if (doc == null) {
            return null;
        }
        final DOMSource domSource = new DOMSource(doc);
        final StringWriter writer = new StringWriter();
        final StreamResult result = new StreamResult(writer);
        final TransformerFactory tf = TransformerFactory.newInstance();
        tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
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
            return "";
        }
    }
}
