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

import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.model.Attribute;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.List;

/**
 * BomGenerator creates a CycloneDX bill-of-material document from a set of
 * {@link Component}s. Proper usage assumes {@link #generate()} is called after
 * construction and optionally followed by {@link #toXmlString()}.
 * @since 2.0.0
 */
public class BomXmlGenerator11 extends AbstractBomXmlGenerator implements BomXmlGenerator {

    private final Bom bom;

    /**
     * Constructs a new BomGenerator object.
     * @param bom the BOM to generate
     */
    public BomXmlGenerator11(final Bom bom) {
        this.bom = bom;
    }

    /**
     * Returns the version of the CycloneDX schema used by this instance
     * @return a CycloneDxSchemaVersion enum
     */
    public Version getSchemaVersion() {
        return CycloneDxSchema.Version.VERSION_11;
    }

    /**
     * Creates a CycloneDX BoM from a set of Components.
     * @return an XML Document representing a CycloneDX BoM
     * @since 2.0.0
     */
    public Document generate() throws ParserConfigurationException {
        final DocumentBuilder docBuilder = buildSecureDocumentBuilder();
        this.doc = docBuilder.newDocument();
        doc.setXmlStandalone(true);

        final List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("xmlns", NS_BOM_11));
        attributes.add(new Attribute("version", String.valueOf(bom.getVersion())));
        if (bom.getDependencies() != null && bom.getDependencies().size() > 0) {
            attributes.add(new Attribute("xmlns:dg", NS_DEPENDENCY_GRAPH_10));
        }

        // Create root <bom> node
        final Element bomNode = createRootElement("bom", null, attributes.toArray(new Attribute[0]));
        if (bom.getSerialNumber() != null) {
            bomNode.setAttribute("serialNumber", bom.getSerialNumber());
        }

        final Element componentsNode = createElement(bomNode, "components");
        createComponentsNode(componentsNode, bom.getComponents());
        createExternalReferencesNode(bomNode, bom.getExternalReferences());

        if (bom.getDependencies() != null && bom.getDependencies().size() > 0) {
            final Element dependenciesNode = createElement(bomNode, "dg:dependencies");
            createDependenciesNode(dependenciesNode, bom.getDependencies());
        }

        processExtensions(bomNode, bom);
        return doc;
    }
}
