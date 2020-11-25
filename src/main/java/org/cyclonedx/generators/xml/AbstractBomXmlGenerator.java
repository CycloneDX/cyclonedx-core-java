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
import org.cyclonedx.model.Commit;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.ExtensibleElement;
import org.cyclonedx.model.ExtensibleType;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Hash;
import org.cyclonedx.model.IdentifiableActionType;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Pedigree;
import org.cyclonedx.model.Swid;
import org.cyclonedx.model.Tool;
import org.cyclonedx.util.BomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.XMLConstants;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractBomXmlGenerator extends CycloneDxSchema implements BomXmlGenerator {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset

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

    Element createElement(Node parent, String name) {
        final Element node = doc.createElement(name);
        parent.appendChild(node);
        return node;
    }

    Element createElementNS(Node parent, String namespace, String name) {
        final Element node = doc.createElementNS(namespace, name);
        parent.appendChild(node);
        return node;
    }

    Element createElement(Node parent, String name, Object value) {
        return createElement(parent, name, value, new Attribute[0]);
    }

    Element createElement(Node parent, String name, Object value, boolean insideCDATA) {
        return createElement(parent, name, value, insideCDATA, new Attribute[0]);
    }

    Element createElement(Node parent, String name, Object value, Attribute... attributes) {
        return createElement(parent, name, value, false, attributes);
    }

    Element createElement(Node parent, String name, Object value, boolean insideCDATA, Attribute... attributes) {
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
                if (insideCDATA) {
                    node.appendChild(doc.createCDATASection(value.toString()));
                } else {
                    node.appendChild(doc.createTextNode(value.toString()));
                }
            }
            parent.appendChild(node);
        }
        return node;
    }

    void createComponentsNode(Node parent, List<Component> components) {
        if (components != null && !components.isEmpty()) {
            for (Component component : components) {
                createComponentNode(parent, component);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void createComponentNode(Node parent, Component component) {
        if (component == null) {
            return;
        }
        final List<Attribute> componentAttrs = new ArrayList<>();
        componentAttrs.add(new Attribute("type", component.getType().getTypeName()));
        if (component.getBomRef() != null) {
            componentAttrs.add(new Attribute("bom-ref", component.getBomRef()));
        }
        if (component.getMimeType() != null && this.getSchemaVersion().getVersion() >= 1.2) {
            componentAttrs.add(new Attribute("mime-type", component.getMimeType()));
        }
        final Element componentNode = createElement(parent, "component", null, componentAttrs.toArray(new Attribute[0]));
        if (this.getSchemaVersion().getVersion() >= 1.2) {
            createOrganizationalEntityNode(componentNode, component.getSupplier(), "supplier");
            createElement(componentNode, "author", stripBreaks(component.getAuthor()));
        }
        createElement(componentNode, "publisher", stripBreaks(component.getPublisher()));
        createElement(componentNode, "group", stripBreaks(component.getGroup()));
        createElement(componentNode, "name", stripBreaks(component.getName()));
        createElement(componentNode, "version", stripBreaks(component.getVersion()));
        createElement(componentNode, "description", stripBreaks(component.getDescription()), true);
        if (component.getScope() == null) {
            createElement(componentNode, "scope", Component.Scope.REQUIRED.getScopeName());
        } else {
            createElement(componentNode, "scope", component.getScope().getScopeName());
        }
        createHashesNode(componentNode, component.getHashes());
        createLicenseNode(componentNode, component.getLicenseChoice());
        createElement(componentNode, "copyright", stripBreaks(component.getCopyright()));
        createElement(componentNode, "cpe", stripBreaks(component.getCpe()));
        createElement(componentNode, "purl", stripBreaks(component.getPurl()));
        if (this.getSchemaVersion().getVersion() == 1.0 || component.isModified()) {
            createElement(componentNode, "modified", component.isModified());
        }
        if (this.getSchemaVersion().getVersion() >= 1.2) {
            createSwidNode(componentNode, component.getSwid());
        }
        if (this.getSchemaVersion().getVersion() >= 1.1) {
            createPedigreeNode(componentNode, component.getPedigree());
            createExternalReferencesNode(componentNode, component.getExternalReferences());
        }
        if (component.getComponents() != null && !component.getComponents().isEmpty()) {
            final Element subComponentsNode = createElement(componentNode, "components");
            createComponentsNode(subComponentsNode, component.getComponents());
        }
        processExtensions(componentNode, component);
    }

    private void createHashesNode(Node parent, List<Hash> hashes) {
        if (hashes != null && !hashes.isEmpty()) {
            final Element hashesNode = createElement(parent, "hashes");
            for (Hash hash : hashes) {
                createElement(hashesNode, "hash", hash.getValue(), new Attribute("alg", hash.getAlgorithm()));
            }
        }
    }

    private void createLicenseNode(Node parent, LicenseChoice licenseChoice) {
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
                    if (this.getSchemaVersion().getVersion() >= 1.1) {
                        if (license.getAttachmentText() != null && license.getAttachmentText().getText() != null) {
                            final Element licenseTextNode = doc.createElement("text");
                            if (license.getAttachmentText().getContentType() != null) {
                                licenseTextNode.setAttribute("content-type", license.getAttachmentText().getContentType());
                            }
                            if (license.getAttachmentText().getEncoding() != null) {
                                licenseTextNode.setAttribute("encoding", license.getAttachmentText().getEncoding());
                                if ("base64".equals(license.getAttachmentText().getEncoding())) {
                                    licenseTextNode.appendChild(doc.createTextNode(license.getAttachmentText().getText()));
                                } else {
                                    licenseTextNode.appendChild(doc.createCDATASection(license.getAttachmentText().getText()));
                                }
                            } else {
                                licenseTextNode.appendChild(doc.createCDATASection(license.getAttachmentText().getText()));
                            }
                            licenseNode.appendChild(licenseTextNode);
                        }
                        if (BomUtils.validateUrlString(license.getUrl())) {
                            final Element licenseUrlNode = doc.createElement("url");
                            licenseUrlNode.appendChild(doc.createTextNode(license.getUrl()));
                            licenseNode.appendChild(licenseUrlNode);
                        }

                        processExtensions(licenseNode, license);
                    }
                    licensesNode.appendChild(licenseNode);
                }
            } else if (this.getSchemaVersion().getVersion() >= 1.1 && licenseChoice.getExpression() != null) {
                createElement(licensesNode, "expression", stripBreaks(licenseChoice.getExpression()));
            }
        }
    }

    private void createSwidNode(Node parent, Swid swid) {
        if (swid != null) {
            final Element swidNode = createElement(parent, "swid");
            if (swid.getTagId() != null) {
                swidNode.setAttribute("tagId", swid.getTagId());
            }
            if (swid.getName() != null) {
                swidNode.setAttribute("name", swid.getName());
            }
            if (swid.getVersion() != null) {
                swidNode.setAttribute("version", swid.getVersion());
            }
            if (swid.getTagVersion() > 0) {
                swidNode.setAttribute("tagVersion", String.valueOf(swid.getTagVersion()));
            }
            if (swid.isPatch()) {
                swidNode.setAttribute("patch", "true");
            }
            if (swid.getAttachmentText() != null && swid.getAttachmentText().getText() != null) {
                final Element attachmentTextNode = doc.createElement("text");
                if (swid.getAttachmentText().getContentType() != null) {
                    attachmentTextNode.setAttribute("content-type", swid.getAttachmentText().getContentType());
                }
                if (swid.getAttachmentText().getEncoding() != null) {
                    attachmentTextNode.setAttribute("encoding", swid.getAttachmentText().getEncoding());
                    if ("base64".equals(swid.getAttachmentText().getEncoding())) {
                        attachmentTextNode.appendChild(doc.createTextNode(swid.getAttachmentText().getText()));
                    } else {
                        attachmentTextNode.appendChild(doc.createCDATASection(swid.getAttachmentText().getText()));
                    }
                } else {
                    attachmentTextNode.appendChild(doc.createCDATASection(swid.getAttachmentText().getText()));
                }
                swidNode.appendChild(attachmentTextNode);
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
            processExtensions(pedigreeNode, pedigree);
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
                processExtensions(commitNode, commit);
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
            processExtensions(authorNode, actor);
        }
    }

    void createExternalReferencesNode(Node parent, List<ExternalReference> references) {
        if (references != null && !references.isEmpty()) {
            final Element externalReferencesNode = createElement(parent, "externalReferences");
            for (ExternalReference reference: references) {
                if (reference.getType() != null && BomUtils.validateUrlString(reference.getUrl())) {
                    final Element referenceNode = createElement(externalReferencesNode, "reference", null, new Attribute("type", reference.getType().getTypeName()));
                    createElement(referenceNode, "url", stripBreaks(reference.getUrl()));
                    createElement(referenceNode, "comment", stripBreaks(reference.getComment()));
                }
            }
        }
    }

    void createDependenciesNode(Node parent, List<Dependency> dependencies) {
        if (dependencies != null && !dependencies.isEmpty()) {
            for (Dependency dependency : dependencies) {
                final String nodeName = (this.getSchemaVersion().getVersion() >= 1.2) ? "dependency" : "dg:dependency";
                final Element dependencyNode = createElement(parent, nodeName, null, new Attribute("ref", dependency.getRef()));
                if (dependency.getDependencies() != null && !dependency.getDependencies().isEmpty()) {
                    createDependenciesNode(dependencyNode, dependency.getDependencies());
                }
            }
        }
    }

    void createMetadataNode(Node parent, Metadata metadata) {
        if (metadata != null) {
            final Element metadataNode = createElement(parent, "metadata");
            if (metadata.getTimestamp() != null) {
                createElement(metadataNode, "timestamp", dateFormat.format(metadata.getTimestamp()));
            }
            createToolsNode(metadataNode, metadata.getTools());
            createOrganizationalContactsNode(metadataNode, metadata.getAuthors(), "authors", "author");
            createComponentNode(metadataNode, metadata.getComponent());
            createOrganizationalEntityNode(metadataNode, metadata.getManufacture(), "manufacture");
            createOrganizationalEntityNode(metadataNode, metadata.getSupplier(), "supplier");
            processExtensions(metadataNode, metadata);
        }
    }

    private void createToolsNode(Node parent, List<Tool> tools) {
        if (tools != null && !tools.isEmpty()) {
            final Element toolsNode = createElement(parent, "tools");
            for (Tool tool : tools) {
                final Element toolNode = createElement(toolsNode, "tool");
                createElement(toolNode, "vendor", stripBreaks(tool.getVendor()));
                createElement(toolNode, "name", stripBreaks(tool.getName()));
                createElement(toolNode, "version", stripBreaks(tool.getVersion()));
                createHashesNode(toolNode, tool.getHashes());
                processExtensions(toolNode, tool);
            }
        }
    }

    private void createOrganizationalContactsNode(Node parent, List<OrganizationalContact> contacts, String parentElementName, String elementName) {
        if (contacts != null && !contacts.isEmpty()) {
            final Element contactsNode = createElement(parent, parentElementName);
            for (OrganizationalContact contact : contacts) {
                createOrganizationalContactNode(contactsNode, contact, elementName);
            }
        }
    }

    private void createOrganizationalContactNode(Node parent, OrganizationalContact contact, String elementName) {
        final Element contactNode = createElement(parent, elementName);
        createElement(contactNode, "name", stripBreaks(contact.getName()));
        createElement(contactNode, "email", stripBreaks(contact.getEmail()));
        createElement(contactNode, "phone", stripBreaks(contact.getPhone()));
        processExtensions(contactNode, contact);
    }

    private void createOrganizationalEntityNode(Node parent, OrganizationalEntity entity, String elementName) {
        if (entity != null) {
            final Element entityNode = createElement(parent, elementName);
            createElement(entityNode, "name", stripBreaks(entity.getName()));
            createElement(entityNode, "url", stripBreaks(entity.getUrl()));
            if (entity.getContacts() != null && entity.getContacts().size() > 0) {
                createOrganizationalContactNode(entityNode, entity.getContacts().get(0), "contact");
            }
            processExtensions(entityNode, entity);
        }
    }

    protected void processExtensions(final Node node, final ExtensibleElement extensibleElement) {
        if (extensibleElement != null && extensibleElement.getExtensibleTypes() != null) {
            for (final ExtensibleType t: extensibleElement.getExtensibleTypes()) {
                final Element e = createElementNS(node, t.getNamespace(), t.getName());
                if (t.getAttributes() != null) {
                    for (Attribute attr: t.getAttributes()) {
                        e.setAttribute(attr.getKey(), attr.getValue());
                    }
                }
                if (t.getValue() != null) {
                    e.appendChild(doc.createTextNode(t.getValue()));
                } else if (t.getExtensibleTypes() != null) {
                    for (final ExtensibleType childType: t.getExtensibleTypes()) {
                        final Element childNode = createElementNS(e, childType.getNamespace(), childType.getName());
                        processExtensions(childNode, childType);
                    }
                }
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
