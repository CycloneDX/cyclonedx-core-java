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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomWriter;
import com.thoughtworks.xstream.io.xml.QNameMap;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.model.Bom;
import org.cyclonedx.util.XStreamUtils;
import org.w3c.dom.Document;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

abstract class AbstractBomXmlGenerator extends CycloneDxSchema implements BomXmlGenerator {

    Document doc;

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

    protected Document generateDocument(final XStream xStream, final Bom bom) throws ParserConfigurationException {
        final DocumentBuilder docBuilder = buildSecureDocumentBuilder();
        this.doc = docBuilder.newDocument();
        this.doc.setXmlStandalone(true);
        DomWriter writer = new DomWriter(this.doc);
        xStream.marshal(bom, writer);

        return this.doc;
    }

    public String toXML(final Bom bom) throws Exception {
        XStream xStream;
        if (this.getSchemaVersion().getVersion() == 1.0) {
            xStream = XStreamUtils.mapObjectModelBom1_0(XStreamUtils.createXStream(CycloneDxSchema.NS_BOM_10, null));
        } else if (this.getSchemaVersion().getVersion() == 1.1) {

            if (bom.getDependencies() != null && bom.getDependencies().size() > 0) {
                QNameMap nsm = new QNameMap();
                QName dependency =
                    new QName("",
                        "dependency",
                        "dg");
                QName dependencies = new QName(
                    "http://cyclonedx.org/schema/ext/dependency-graph/1.0",
                    "dependencies",
                    "dg");
                nsm.registerMapping(dependency, "dependency");
                nsm.registerMapping(dependencies, "dependencies");
                xStream = XStreamUtils.mapObjectModelBom1_1(XStreamUtils.createXStream(CycloneDxSchema.NS_BOM_11, nsm));
            } else {
                xStream = XStreamUtils.mapObjectModelBom1_1(XStreamUtils.createXStream(CycloneDxSchema.NS_BOM_11, null));
            }

        } else if (this.getSchemaVersion().getVersion() == 1.2) {
            xStream = XStreamUtils.mapObjectModelBom1_2(XStreamUtils.createXStream(CycloneDxSchema.NS_BOM_12, null));
        } else {
            throw new Exception("Unsupported schema version");
        }

        return xStream.toXML(bom);
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
        } catch (Exception e) {
            return "";
        }
    }
}
