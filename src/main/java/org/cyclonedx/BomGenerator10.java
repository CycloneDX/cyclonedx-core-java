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
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

/**
 * BomGenerator creates a CycloneDX bill-of-material document from a set of
 * {@link Component}s. Proper usage assumes {@link #generate()} is called after
 * construction and optionally followed by {@link #toXmlString()}.
 * @since 1.1.0
 */
public class BomGenerator10 extends AbstractBomGenerator implements BomGenerator {

    private final Bom bom;

    /**
     * Constructs a new BomGenerator object.
     * @param bom the BOM to generate
     */
    BomGenerator10(final Bom bom) {
        this.bom = bom;
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
        this.doc = docBuilder.newDocument();
        doc.setXmlStandalone(true);

        // Create root <bom> node
        final Element bomNode = createRootElement("bom", null,
                new Attribute("xmlns", NS_BOM_10),
                new Attribute("version", "1"));

        final Element componentsNode = createElement(bomNode, "components");
        createComponentsNode(componentsNode, bom.getComponents());
        return doc;
    }

    private void createComponentsNode(Node parent, List<Component> components) {
        if (components != null && !components.isEmpty()) {
            for (Component component : components) {
                final Element componentNode = createElement(parent, "component", null, new Attribute("type", component.getType()));
                createElement(componentNode, "publisher", stripBreaks(component.getPublisher()));
                createElement(componentNode, "group", stripBreaks(component.getGroup()));
                createElement(componentNode, "name", stripBreaks(component.getName()));
                createElement(componentNode, "version", stripBreaks(component.getVersion()));
                createElement(componentNode, "description", stripBreaks(component.getDescription()));
                createElement(componentNode, "scope", stripBreaks(component.getScope()));
                createHashesNode(componentNode, component.getHashes());
                createLicenseNode(componentNode, component.getLicenseChoice(), false);
                createElement(componentNode, "copyright", stripBreaks(component.getCopyright()));
                createElement(componentNode, "cpe", stripBreaks(component.getCpe()));
                createElement(componentNode, "purl", stripBreaks(component.getPurl()));
                createElement(componentNode, "modified", String.valueOf(component.isModified()));
                if (component.getComponents() != null && !component.getComponents().isEmpty()) {
                    final Element subComponentsNode = createElement(componentNode, "components");
                    createComponentsNode(subComponentsNode, component.getComponents());
                }
            }
        }
    }
}
