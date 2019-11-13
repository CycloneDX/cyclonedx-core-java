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
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Commit;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.IdentifiableActionType;
import org.cyclonedx.model.Pedigree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * BomGenerator creates a CycloneDX bill-of-material document from a set of
 * {@link Component}s. Proper usage assumes {@link #generate()} is called after
 * construction and optionally followed by {@link #toXmlString()}.
 * @since 2.0.0
 */
public class BomGenerator11 extends AbstractBomGenerator implements BomGenerator {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
    private final Bom bom;

    /**
     * Constructs a new BomGenerator object.
     * @param bom the BOM to generate
     */
    BomGenerator11(final Bom bom) {
        this.bom = bom;
    }

    /**
     * Returns the version of the CycloneDX schema used by this instance
     * @return a CycloneDxSchemaVersion enum
     */
    public CycloneDxSchema.Version getSchemaVersion() {
        return CycloneDxSchema.Version.VERSION_11;
    }

    /**
     * Creates a CycloneDX BoM from a set of Components.
     * @return an XML Document representing a CycloneDX BoM
     * @since 2.0.0
     */
    public Document generate() throws ParserConfigurationException {
        final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        this.doc = docBuilder.newDocument();
        doc.setXmlStandalone(true);

        // Create root <bom> node
        final Element bomNode = createRootElement("bom", null,
                new Attribute("xmlns", NS_BOM_11),
                new Attribute("version", String.valueOf(bom.getVersion())));
        if (bom.getSerialNumber() != null) {
            bomNode.setAttribute("serialNumber", bom.getSerialNumber());
        }

        final Element componentsNode = createElement(bomNode, "components");
        createComponentsNode(componentsNode, bom.getComponents());
        createExternalReferencesNode(bomNode, bom.getExternalReferences());
        return doc;
    }

    private void createComponentsNode(Node parent, List<Component> components) {
        if (components != null && !components.isEmpty()) {
            for (Component component : components) {
                final Element componentNode = createElement(parent, "component", null, new Attribute("type", component.getType().getTypeName()));
                createElement(componentNode, "publisher", stripBreaks(component.getPublisher()));
                createElement(componentNode, "group", stripBreaks(component.getGroup()));
                createElement(componentNode, "name", stripBreaks(component.getName()));
                createElement(componentNode, "version", stripBreaks(component.getVersion()));
                createElement(componentNode, "description", stripBreaks(component.getDescription()));
                if (component.getScope() == null) {
                    createElement(componentNode, "scope", Component.Scope.REQUIRED.getScopeName());
                } else {
                    createElement(componentNode, "scope", component.getScope().getScopeName());
                }
                createHashesNode(componentNode, component.getHashes());
                createLicenseNode(componentNode, component.getLicenseChoice(), true);
                createElement(componentNode, "copyright", stripBreaks(component.getCopyright()));
                createElement(componentNode, "cpe", stripBreaks(component.getCpe()));
                createElement(componentNode, "purl", stripBreaks(component.getPurl()));
                createPedigreeNode(componentNode, component.getPedigree());
                createExternalReferencesNode(componentNode, component.getExternalReferences());
                if (component.getComponents() != null && !component.getComponents().isEmpty()) {
                    final Element subComponentsNode = createElement(componentNode, "components");
                    createComponentsNode(subComponentsNode, component.getComponents());
                }
            }
        }
    }

    private void createPedigreeNode(Node parent, Pedigree pedigree) {
        if (pedigree != null) {
            final Element pedigreeNode = createElement(parent, "pedigree");
            if (pedigree.getAncestors() != null && !pedigree.getAncestors().isEmpty()) {
                final Element ancestorsNode = createElement(pedigreeNode, "ancestors");
                createComponentsNode(ancestorsNode, pedigree.getAncestors());
            }
            if (pedigree.getDescendants() != null && !pedigree.getDescendants().isEmpty()) {
                final Element descendantsNode = createElement(pedigreeNode, "descendants");
                createComponentsNode(descendantsNode, pedigree.getDescendants());
            }
            if (pedigree.getVariants() != null && !pedigree.getVariants().isEmpty()) {
                final Element variantsNode = createElement(pedigreeNode, "variants");
                createComponentsNode(variantsNode, pedigree.getVariants());
            }
            if (pedigree.getCommits() != null && !pedigree.getCommits().isEmpty()) {
                final Element commitsNode = createElement(pedigreeNode, "commits");
                createCommitsNode(commitsNode, pedigree.getCommits());
            }
            createElement(pedigreeNode, "notes", stripBreaks(pedigree.getNotes()));
        }
    }

    private void createCommitsNode(Node parent, List<Commit> commits) {
        if (commits != null) {
            for (Commit commit: commits) {
                final Element commitNode = createElement(parent, "commit");
                createElement(commitNode, "uid", stripBreaks(commit.getUid()));
                createElement(commitNode, "url", stripBreaks(commit.getUrl()));
                createActorNode(commitNode, "author", commit.getAuthor());
                createActorNode(commitNode, "committer", commit.getCommitter());
                createElement(commitNode, "message", stripBreaks(commit.getMessage()));
            }
        }
    }

    private void createActorNode(Node parent, String nodeName, IdentifiableActionType actor) {
        if (actor != null) {
            final Element authorNode = createElement(parent, nodeName);
            if (actor.getTimestamp() != null) {
                createElement(authorNode, "timestamp", dateFormat.format(actor.getTimestamp()));
            }
            createElement(authorNode, "name", stripBreaks(actor.getName()));
            createElement(authorNode, "email", stripBreaks(actor.getEmail()));
        }
    }

    private void createExternalReferencesNode(Node parent, List<ExternalReference> references) {
        if (references != null && !references.isEmpty()) {
            final Element externalReferencesNode = createElement(parent, "externalReferences");
            for (ExternalReference reference: references) {
                if (reference.getType() != null) {
                    final Element referenceNode = createElement(externalReferencesNode, "reference", null, new Attribute("type", reference.getType().getTypeName()));
                    createElement(referenceNode, "url", stripBreaks(reference.getUrl()));
                    createElement(referenceNode, "comment", stripBreaks(reference.getComment()));
                }
            }
        }
    }
}
