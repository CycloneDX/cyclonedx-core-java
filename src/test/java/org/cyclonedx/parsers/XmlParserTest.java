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

import org.apache.commons.io.IOUtils;
import org.cyclonedx.Version;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Component.Type;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Pedigree;
import org.cyclonedx.model.attestation.Assessor;
import org.cyclonedx.model.attestation.Attestation;
import org.cyclonedx.model.attestation.AttestationMap;
import org.cyclonedx.model.attestation.Claim;
import org.cyclonedx.model.attestation.Confidence;
import org.cyclonedx.model.attestation.Conformance;
import org.cyclonedx.model.attestation.Targets;
import org.cyclonedx.model.attestation.affirmation.Affirmation;
import org.cyclonedx.model.attestation.affirmation.Signatory;
import org.cyclonedx.model.attestation.evidence.Data;
import org.cyclonedx.model.attestation.evidence.Evidence;
import org.cyclonedx.model.component.ModelCard;
import org.cyclonedx.model.component.crypto.AlgorithmProperties;
import org.cyclonedx.model.component.crypto.CryptoProperties;
import org.cyclonedx.model.component.crypto.enums.AssetType;
import org.cyclonedx.model.component.crypto.enums.CertificationLevel;
import org.cyclonedx.model.component.crypto.enums.CryptoFunction;
import org.cyclonedx.model.component.crypto.enums.ExecutionEnvironment;
import org.cyclonedx.model.component.crypto.enums.ImplementationPlatform;
import org.cyclonedx.model.component.crypto.enums.Mode;
import org.cyclonedx.model.component.crypto.enums.Primitive;
import org.cyclonedx.model.component.data.Content;
import org.cyclonedx.model.component.evidence.Identity;
import org.cyclonedx.model.component.modelCard.Considerations;
import org.cyclonedx.model.component.modelCard.ModelParameters;
import org.cyclonedx.model.component.modelCard.consideration.EnvironmentalConsideration;
import org.cyclonedx.model.component.modelCard.consideration.consumption.Activity;
import org.cyclonedx.model.component.modelCard.consideration.consumption.EnergyConsumption;
import org.cyclonedx.model.component.modelCard.consideration.consumption.energy.EnergyProvider;
import org.cyclonedx.model.component.modelCard.consideration.consumption.energy.EnergySource;
import org.cyclonedx.model.component.modelCard.consideration.consumption.energy.Unit;
import org.cyclonedx.model.definition.Level;
import org.cyclonedx.model.definition.Requirement;
import org.cyclonedx.model.definition.Standard;
import org.cyclonedx.model.license.Acknowledgement;
import org.cyclonedx.model.license.Expression;
import org.cyclonedx.model.license.ExpressionDetailed;
import org.cyclonedx.model.license.ExpressionDetail;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XmlParserTest
    extends AbstractParserTest
{

    @Test
    public void testValid10Bom() throws Exception {
        final File file = new File(Objects.requireNonNull(this.getClass().getResource("/bom-1.0.xml")).getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, Version.VERSION_10);
        assertTrue(valid);
    }

    @Test
    public void testValid11Bom() throws Exception {
        final File file = new File(Objects.requireNonNull(this.getClass().getResource("/bom-1.1.xml")).getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, Version.VERSION_11);
        assertTrue(valid);
    }

    @Test
    public void testValid11BomWithDependencyGraph10() throws Exception {
        final File file = new File(
            Objects.requireNonNull(this.getClass().getResource("/bom-1.1-dependency-graph-1.0.xml")).getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, Version.VERSION_11);
        assertTrue(valid);
    }

    @Test
    public void testValid11BomWithVulnerability10() throws Exception {
        final File file = new File(
            Objects.requireNonNull(this.getClass().getResource("/bom-1.1-vulnerability-1.0.xml")).getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, Version.VERSION_11);
        assertTrue(valid);
    }

    @Test
    public void testValid12Bom() throws Exception {
        final File file = new File(Objects.requireNonNull(this.getClass().getResource("/bom-1.2.xml")).getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, Version.VERSION_12);
        assertTrue(valid);
    }

    @Test
    public void testValidBomLink() throws Exception {
        final File file = new File(Objects.requireNonNull(this.getClass().getResource("/bom-1.4-bomlink.xml")).getFile());
        final XmlParser parser = new XmlParser();
        Bom bom = parser.parse(file);
        assertTrue(parser.isValid(file, Version.VERSION_14));
        ExternalReference ref = bom.getComponents().get(0).getExternalReferences().get(0);
        assertEquals("bom", ref.getType().getTypeName());
        assertEquals("urn:cdx:f08a6ccd-4dce-4759-bd84-c626675d60a7/1", ref.getUrl());
    }

    @Test
    public void testValid12BomWithPedigree() throws Exception {
        final File file = new File(Objects.requireNonNull(this.getClass().getResource("/bom-1.2-pedigree.xml")).getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, Version.VERSION_12);
        assertTrue(valid);

        final Bom bom = parser.parse(file);
        testPedigree(bom.getComponents().get(0).getPedigree());
    }

    private void testPedigree(final Pedigree pedigree) {
        assertEquals("sample-library-ancestor", pedigree.getAncestors().getComponents().get(0).getName());
        assertEquals("sample-library-descendant", pedigree.getDescendants().getComponents().get(0).getName());
        assertEquals("sample-library-variant", pedigree.getVariants().getComponents().get(0).getName());
    }

    @Test
    public void testValid12BomWithPedigreeWithPatches() throws Exception {
        final File file = new File(Objects.requireNonNull(this.getClass().getResource("/bom-1.2-pedigree-example.xml")).getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, Version.VERSION_12);
        assertTrue(valid);

        final Bom bom = parser.parse(file);
        testPedigreeFromExample(bom.getComponents().get(0).getPedigree());
    }

    private void testPedigreeFromExample(final Pedigree pedigree) {
        assertEquals(2, pedigree.getPatches().size());
        assertEquals(2, pedigree.getPatches().get(1).getResolves().size());
    }

    @Test
    public void testValid12BomWithMetadataPedigree() throws Exception {
        final File file = new File(Objects.requireNonNull(this.getClass().getResource("/bom-1.2-metadata-pedigree.xml")).getFile());
        final XmlParser parser = new XmlParser();
        final boolean valid = parser.isValid(file, Version.VERSION_12);
        assertTrue(valid);

        final Bom bom = parser.parse(file);
        Pedigree pedigree = bom.getMetadata().getComponent().getPedigree();
        assertEquals(2, pedigree.getAncestors().getComponents().size());
        assertEquals(1, pedigree.getDescendants().getComponents().size());
        assertEquals(0, pedigree.getVariants().getComponents().size());
    }

    @Test
    public void testParsedObjects10Bom() throws Exception {
        final Bom bom = getXmlBom("bom-1.0.xml");
        assertEquals(1, bom.getComponents().size());
        assertEquals("1.0", bom.getSpecVersion());
        assertEquals(1, bom.getVersion());
        final List<Component> components = bom.getComponents();
        assertEquals(1, components.size());
        Component c1 = components.get(0);
        assertEquals("org.example", c1.getGroup());
        assertEquals("1.0.0", c1.getVersion());
        assertEquals(Component.Type.APPLICATION, c1.getType());
        assertEquals("2342c2eaf1feb9a80195dbaddf2ebaa3", c1.getHashes().get(0).getValue());
        assertEquals("68b78babe00a053f9e35ec6a2d9080f5b90122b0", c1.getHashes().get(1).getValue());
        assertEquals("708f1f53b41f11f02d12a11b1a38d2905d47b099afc71a0f1124ef8582ec7313", c1.getHashes().get(2).getValue());
        assertEquals("387b7ae16b9cae45f830671541539bf544202faae5aac544a93b7b0a04f5f846fa2f4e81ef3f1677e13aed7496408a441f5657ab6d54423e56bf6f38da124aef", c1.getHashes().get(3).getValue());
        assertEquals("cpe:/a:example:myapplication:1.0.0", c1.getCpe());
        assertEquals("pkg:maven/com.example/myapplication@1.0.0?packaging=war", c1.getPurl());
        assertEquals("An example application", c1.getDescription());
        assertEquals("Copyright Example Inc. All rights reserved.", c1.getCopyright());
        assertEquals("Apache-2.0", c1.getLicenseChoice().getLicenses().get(0).getId());
        assertEquals("Apache-2.0", c1.getLicenses().getLicenses().get(0).getId());
        assertEquals(2, c1.getComponents().size());
    }

    @Test
    public void testParsedObjects11Bom() throws Exception {
        final Bom bom = getXmlBom("bom-1.1.xml");
        assertEquals(3, bom.getComponents().size());
        assertEquals("1.1", bom.getSpecVersion());
        assertEquals(1, bom.getVersion());
        final List<Component> components = bom.getComponents();
        assertEquals(3, components.size());
        Component c1 = components.get(0);
        Component c2 = components.get(1);
        assertEquals("Acme Inc", c1.getPublisher());
        assertEquals("com.acme", c1.getGroup());
        assertEquals("tomcat-catalina", c1.getName());
        assertEquals("9.0.14", c1.getVersion());
        assertEquals("Modified version of Apache Catalina", c1.getDescription());
        assertEquals(Component.Type.APPLICATION, c1.getType());
        assertEquals("required", c1.getScope().getScopeName());
        assertEquals("3942447fac867ae5cdb3229b658f4d48", c1.getHashes().get(0).getValue());
        assertEquals("e6b1000b94e835ffd37f4c6dcbdad43f4b48a02a", c1.getHashes().get(1).getValue());
        assertEquals("f498a8ff2dd007e29c2074f5e4b01a9a01775c3ff3aeaf6906ea503bc5791b7b", c1.getHashes().get(2).getValue());
        assertEquals("e8f33e424f3f4ed6db76a482fde1a5298970e442c531729119e37991884bdffab4f9426b7ee11fccd074eeda0634d71697d6f88a460dce0ac8d627a29f7d1282", c1.getHashes().get(3).getValue());
        assertEquals("pkg:maven/com.acme/tomcat-catalina@9.0.14?packaging=jar", c1.getPurl());
        assertEquals("Apache-2.0", c1.getLicenseChoice().getLicenses().get(0).getId());
        assertTrue(c1.getLicenseChoice().getLicenses().get(0).getAttachmentText().getText().startsWith("CiAgICA"));
        assertEquals("text/plain", c1.getLicenseChoice().getLicenses().get(0).getAttachmentText().getContentType());
        assertEquals("base64", c1.getLicenseChoice().getLicenses().get(0).getAttachmentText().getEncoding());
        assertNotNull(c1.getPedigree());

        assertEquals(1, c1.getPedigree().getAncestors().getComponents().size());
        assertNull(c1.getPedigree().getDescendants());
        assertNull(c1.getPedigree().getVariants());
        assertEquals(1, c1.getPedigree().getCommits().size());
        assertEquals("7638417db6d59f3c431d3e1f261cc637155684cd", c1.getPedigree().getCommits().get(0).getUid());
        assertEquals("https://location/to/7638417db6d59f3c431d3e1f261cc637155684cd", c1.getPedigree().getCommits().get(0).getUrl());
        assertEquals("John Doe", c1.getPedigree().getCommits().get(0).getAuthor().getName());
        assertEquals("john.doe@example.com", c1.getPedigree().getCommits().get(0).getAuthor().getEmail());
        assertNotNull(c1.getPedigree().getCommits().get(0).getAuthor().getTimestamp());
        assertEquals("Jane Doe", c1.getPedigree().getCommits().get(0).getCommitter().getName());
        assertEquals("jane.doe@example.com", c1.getPedigree().getCommits().get(0).getCommitter().getEmail());
        assertNotNull(c1.getPedigree().getCommits().get(0).getCommitter().getTimestamp());
        assertEquals("Initial commit", c1.getPedigree().getCommits().get(0).getMessage());
        assertEquals("Commentary here", c1.getPedigree().getNotes());
        assertEquals("EPL-2.0 OR GPL-2.0-with-classpath-exception", c2.getLicenseChoice().getExpression().getValue());
    }

    @Test
    public void testParsedObjects11BomWithDependencyGraph10() throws Exception {
        final Bom bom = getXmlBom("bom-1.1-dependency-graph-1.0.xml");

        assertEquals(3, bom.getComponents().size());
        assertEquals("1.1", bom.getSpecVersion());
        assertEquals(1, bom.getVersion());
        final List<Component> components = bom.getComponents();
        assertEquals(3, components.size());
        Component c1 = components.get(0);
        //Component c2 = components.get(1);
        assertEquals("org.example.acme", c1.getGroup());
        assertEquals("web-framework", c1.getName());
        assertEquals("1.0.0", c1.getVersion());
        assertEquals(Component.Type.FRAMEWORK, c1.getType());
        assertEquals("pkg:maven/org.example.acme/web-framework@1.0.0", c1.getPurl());
        assertEquals(3, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        Dependency d2 = bom.getDependencies().get(1);
        Dependency d3 = bom.getDependencies().get(2);
        assertNotNull(d1);
        assertEquals("pkg:maven/org.example.acme/web-framework@1.0.0", d1.getRef());
        assertEquals(2, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        assertEquals("pkg:maven/org.example.acme/common-util@3.0.0", d11.getRef());
        assertNull(d11.getDependencies());
        assertEquals(1, d2.getDependencies().size());
        assertNull(d3.getDependencies());
    }

    @Test
    public void testParsedObjects12Bom() throws Exception {
        final Bom bom = getXmlBom("bom-1.2.xml");

        assertMetadata(bom, Version.VERSION_12);

        final List<Component> components = bom.getComponents();

        // Assertions for bom.components
        assertComponent(components.get(0), Component.Type.APPLICATION, "pkg:maven/com.acme/tomcat-catalina@9.0.14?packaging=jar");

        assertServices(bom, Version.VERSION_12);

        // Assertions for bom.dependencies
        assertEquals(1, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("acme-app", d1.getRef());
        assertEquals(2, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        assertEquals("pkg:maven/org.acme/web-framework@1.0.0", d11.getRef());
        assertNull(d11.getDependencies());
    }

    @Test
    public void testParsedObjects13Bom() throws Exception {
        final Bom bom = getXmlBom("bom-1.3.xml");

        assertMetadata(bom, Version.VERSION_13);

        final List<Component> components = bom.getComponents();
        assertEquals(3, components.size());

        assertComponent(components.get(0), Component.Type.APPLICATION, "pkg:maven/com.acme/tomcat-catalina@9.0.14?packaging=jar");

        assertServices(bom, Version.VERSION_13);

        // Assertions for bom.dependencies
        assertEquals(1, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("acme-app", d1.getRef());
        assertEquals(2, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        assertEquals("pkg:maven/org.acme/web-framework@1.0.0", d11.getRef());
        assertNull(d11.getDependencies());
    }

    @Test
    public void testParsedObjects14Bom() throws Exception {
        final Bom bom = getXmlBom("bom-1.4.xml");

        assertCommonBomProperties(bom, Version.VERSION_14);

        assertMetadata(bom.getMetadata(), Version.VERSION_14);
        assertComponent(bom, Version.VERSION_14);
        assertServices(bom, Version.VERSION_14);
        assertCompositions(bom, Version.VERSION_14);
        assertVulnerabilities(bom, Version.VERSION_14);

        // Dependencies
        assertEquals(1, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("pkg:maven/com.acme/jackson-databind@2.9.4", d1.getRef());

        //Assert Bom Properties
        assertNull(bom.getProperties());

        //Assert Annotations
        assertAnnotations(bom, Version.VERSION_14);
        //Assert Formulation
        assertFormulation(bom, Version.VERSION_14);
    }

    @Test
    public void testParsedObjects15Bom() throws Exception {
        final Bom bom = getXmlBom("bom-1.5.xml");

        assertCommonBomProperties(bom, Version.VERSION_15);

        assertMetadata(bom.getMetadata(), Version.VERSION_15);
        assertComponent(bom, Version.VERSION_15);
        assertServices(bom, Version.VERSION_15);
        assertCompositions(bom, Version.VERSION_15);
        assertVulnerabilities(bom, Version.VERSION_15);

        // Dependencies
        assertEquals(1, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("pkg:maven/com.acme/jackson-databind@2.9.4", d1.getRef());

        //Assert Bom Properties
        assertEquals(bom.getProperties().size(), 1);

        //Assert Annotations
        assertAnnotations(bom, Version.VERSION_15);
        //Assert Formulation
        assertFormulation(bom, Version.VERSION_15);
    }

    @Test
    public void testParsedObjects15Bom_validTools() throws Exception {
        final Bom bom = getXmlBom("1.5/valid-metadata-tool-1.5.xml");
        assertCommonBomProperties(bom, Version.VERSION_15);
        assertMetadata_validTools(bom.getMetadata());
    }

    @Test
    public void testParsedObjects14Bom_WithVulnsExtension() throws Exception {
        final Bom bom = getXmlBom("valid-ext-vulnerability-1.4.xml");

        assertEquals("1.4", bom.getSpecVersion());
        assertEquals(1, bom.getVersion());
        assertNull(bom.getVulnerabilities());
    }

    @Test
    public void testIssue336Regression() throws Exception {
        final Bom bom = getXmlBom("regression/issue336.xml");
        assertEquals("foo", bom.getMetadata().getComponent().getProperties().get(0).getName());
        assertEquals("bar", bom.getMetadata().getComponent().getProperties().get(0).getValue());
    }

    @Test
    public void testIssue338RegressionWithSingleTool() throws Exception {
        final Bom bom = getXmlBom("regression/issue338-single-tool.xml");
        assertEquals("acme-tool-a", bom.getMetadata().getTools().get(0).getName());
    }

    @Test
    public void testIssue338RegressionWithMultipleTools() throws Exception {
        final Bom bom = getXmlBom("regression/issue338-multiple-tools.xml");
        assertEquals("acme-tool-a", bom.getMetadata().getTools().get(0).getName());
        assertEquals("acme-tool-b", bom.getMetadata().getTools().get(1).getName());
    }

    @Test
    public void testIssue343Regression() throws Exception {
        final Bom bom = getXmlBom("regression/issue343-empty-hashes.xml");
        assertEquals(0, bom.getComponents().get(0).getHashes().size());
    }

    @Test
    public void schema16_license_id_acknowledgement() throws Exception {
        final Bom bom = getXmlBom("1.6/valid-license-id-1.6.xml");

        assertAck(bom);
    }

    @Test
    public void schema16_license_expression_acknowledgement() throws Exception {
        final Bom bom = getXmlBom("1.6/valid-license-expression-1.6.xml");

        assertNotNull(bom.getComponents());
        LicenseChoice lc = bom.getComponents().get(0).getLicenses();
        assertNotNull(lc.getExpression());

        Expression expression = lc.getExpression();
        assertEquals("EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0", expression.getValue());
        assertEquals("my-license", expression.getBomRef());
        assertEquals(Acknowledgement.DECLARED, expression.getAcknowledgement());
    }

    @Test
    public void schema17_license_mixed_choice() throws Exception {
        final Bom bom = getXmlBom("1.7/valid-license-choice-1.7.xml");

        assertNotNull(bom.getComponents());
        Component component = bom.getComponents().get(0);
        LicenseChoice lc = component.getLicenses();
        assertNotNull(lc);
        assertNotNull(lc.getItems());
        assertEquals(4, lc.getItems().size());

        // First item: license with id
        assertNotNull(lc.getItems().get(0).getLicense());
        assertEquals("Apache-2.0", lc.getItems().get(0).getLicense().getId());

        // Second item: license with name and text
        assertNotNull(lc.getItems().get(1).getLicense());
        assertEquals("My Own License", lc.getItems().get(1).getLicense().getName());
        assertNotNull(lc.getItems().get(1).getLicense().getAttachmentText());
        assertTrue(lc.getItems().get(1).getLicense().getAttachmentText().getText().contains("Lorem ipsum"));

        // Third item: expression
        assertNotNull(lc.getItems().get(2).getExpression());
        assertEquals("EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0", lc.getItems().get(2).getExpression().getValue());

        // Fourth item: expression-detailed
        assertNotNull(lc.getItems().get(3).getExpressionDetailed());
        ExpressionDetailed ed = lc.getItems().get(3).getExpressionDetailed();
        assertEquals("LicenseRef-MIT-Style-2", ed.getExpression());
        assertNotNull(ed.getExpressionDetails());
        assertEquals(1, ed.getExpressionDetails().size());
        assertEquals("LicenseRef-MIT-Style-2", ed.getExpressionDetails().get(0).getLicenseIdentifier());
        assertEquals("https://example.com/license", ed.getExpressionDetails().get(0).getUrl());
    }

    @Test
    public void schema17_license_expression_detailed_with_text() throws Exception {
        final Bom bom = getXmlBom("1.7/valid-license-expression-with-text-1.7.xml");

        assertNotNull(bom.getComponents());
        Component component = bom.getComponents().get(0);
        LicenseChoice lc = component.getLicenses();
        assertNotNull(lc);
        assertNotNull(lc.getItems());
        assertEquals(1, lc.getItems().size());

        ExpressionDetailed ed = lc.getItems().get(0).getExpressionDetailed();
        assertNotNull(ed);
        assertEquals("LicenseRef-my-custom-license AND (EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0) AND MIT", ed.getExpression());
        assertEquals("my-application-license", ed.getBomRef());
        assertEquals(Acknowledgement.DECLARED, ed.getAcknowledgement());

        assertNotNull(ed.getExpressionDetails());
        assertEquals(5, ed.getExpressionDetails().size());

        // First detail: LicenseRef-my-custom-license
        ExpressionDetail detail0 = ed.getExpressionDetails().get(0);
        assertEquals("LicenseRef-my-custom-license", detail0.getLicenseIdentifier());
        assertNotNull(detail0.getText());
        assertTrue(detail0.getText().getText().contains("Lorem ipsum"));
        assertEquals("https://my-application.example.com/license.txt", detail0.getUrl());

        // Second detail: EPL-2.0
        ExpressionDetail detail1 = ed.getExpressionDetails().get(1);
        assertEquals("EPL-2.0", detail1.getLicenseIdentifier());
        assertNotNull(detail1.getText());
        assertTrue(detail1.getText().getText().contains("Eclipse Public License"));

        // Third detail: GPL-2.0 WITH Classpath-exception-2.0
        ExpressionDetail detail2 = ed.getExpressionDetails().get(2);
        assertEquals("GPL-2.0 WITH Classpath-exception-2.0", detail2.getLicenseIdentifier());
        assertNotNull(detail2.getText());

        assertTrue(detail2.getText().getText().contains("GNU GENERAL PUBLIC LICENSE"));
        assertEquals("text/plain", detail2.getText().getContentType());

        // Fourth detail: MIT (component B)
        ExpressionDetail detail3 = ed.getExpressionDetails().get(3);
        assertEquals("MIT", detail3.getLicenseIdentifier());
        assertEquals("LicenseDetails-component-B", detail3.getBomRef());
        assertNotNull(detail3.getText());
        assertTrue(detail3.getText().getText().contains("Component-B-Creators Inc"));

        // Fifth detail: MIT (component C)
        ExpressionDetail detail4 = ed.getExpressionDetails().get(4);
        assertEquals("MIT", detail4.getLicenseIdentifier());
        assertEquals("LicenseDetails-component-C", detail4.getBomRef());
        assertNotNull(detail4.getText());
        assertTrue(detail4.getText().getText().contains("Component-C-Creators Org"));
    }

    @Test
    public void schema17_license_expression_detailed_with_licensing() throws Exception {
        final Bom bom = getXmlBom("1.7/valid-license-expression-with-licensing-1.7.xml");

        assertNotNull(bom.getComponents());
        Component component = bom.getComponents().get(0);
        LicenseChoice lc = component.getLicenses();
        assertNotNull(lc);
        assertNotNull(lc.getItems());
        assertEquals(1, lc.getItems().size());

        ExpressionDetailed ed = lc.getItems().get(0).getExpressionDetailed();
        assertNotNull(ed);
        assertEquals("LicenseRef-AcmeCommercialLicense", ed.getExpression());
        assertEquals("acme-license-1", ed.getBomRef());

        assertNotNull(ed.getLicensing());
        assertNotNull(ed.getLicensing().getAltIds());
        assertEquals(2, ed.getLicensing().getAltIds().size());
        assertTrue(ed.getLicensing().getAltIds().contains("acme"));
        assertTrue(ed.getLicensing().getAltIds().contains("acme-license"));

        assertNotNull(ed.getLicensing().getLicensor());
        assertNotNull(ed.getLicensing().getLicensor().getOrganization());
        assertEquals("Acme Inc", ed.getLicensing().getLicensor().getOrganization().getName());

        assertNotNull(ed.getLicensing().getLicensee());
        assertNotNull(ed.getLicensing().getLicensee().getOrganization());
        assertEquals("Example Co.", ed.getLicensing().getLicensee().getOrganization().getName());

        assertNotNull(ed.getLicensing().getPurchaser());
        assertNotNull(ed.getLicensing().getPurchaser().getIndividual());
        assertEquals("Samantha Wright", ed.getLicensing().getPurchaser().getIndividual().getName());

        assertEquals("PO-12345", ed.getLicensing().getPurchaseOrder());

        assertNotNull(ed.getLicensing().getLicenseTypes());
        assertEquals(1, ed.getLicensing().getLicenseTypes().size());
    }

    @Test
    public void schema17_license_declared_concluded_mix() throws Exception {
        final Bom bom = getXmlBom("1.7/valid-license-declared-concluded-mix-1.7.xml");

        assertNotNull(bom.getComponents());
        assertEquals(5, bom.getComponents().size());

        // Situation A: Multiple declared licenses + concluded expression
        Component sitA = bom.getComponents().get(0);
        assertEquals("situation-A", sitA.getName());
        LicenseChoice lcA = sitA.getLicenses();
        assertNotNull(lcA);
        assertNotNull(lcA.getItems());
        assertEquals(4, lcA.getItems().size());
        // 3 declared licenses
        assertNotNull(lcA.getItems().get(0).getLicense());
        assertEquals("MIT", lcA.getItems().get(0).getLicense().getId());
        assertEquals(Acknowledgement.DECLARED, lcA.getItems().get(0).getLicense().getAcknowledgement());
        assertNotNull(lcA.getItems().get(1).getLicense());
        assertEquals("PostgreSQL", lcA.getItems().get(1).getLicense().getId());
        assertNotNull(lcA.getItems().get(2).getLicense());
        assertEquals("Apache Software License", lcA.getItems().get(2).getLicense().getName());
        // 1 concluded expression
        assertNotNull(lcA.getItems().get(3).getExpression());
        assertEquals(Acknowledgement.CONCLUDED, lcA.getItems().get(3).getExpression().getAcknowledgement());

        // Situation B: declared expression + concluded expression
        Component sitB = bom.getComponents().get(1);
        assertEquals("situation-B", sitB.getName());
        LicenseChoice lcB = sitB.getLicenses();
        assertNotNull(lcB);
        assertNotNull(lcB.getItems());
        assertEquals(2, lcB.getItems().size());
        assertNotNull(lcB.getItems().get(0).getExpression());
        assertEquals(Acknowledgement.DECLARED, lcB.getItems().get(0).getExpression().getAcknowledgement());
        assertNotNull(lcB.getItems().get(1).getExpression());
        assertEquals(Acknowledgement.CONCLUDED, lcB.getItems().get(1).getExpression().getAcknowledgement());

        // Situation C: declared expression + concluded license ID
        // Note: XML deserializer groups by element type (license, expression, expression-detailed)
        // so license comes before expression regardless of document order
        Component sitC = bom.getComponents().get(2);
        assertEquals("situation-C", sitC.getName());
        LicenseChoice lcC = sitC.getLicenses();
        assertNotNull(lcC);
        assertNotNull(lcC.getItems());
        assertEquals(2, lcC.getItems().size());
        assertNotNull(lcC.getItems().get(0).getLicense());
        assertEquals("GPL-3.0-only", lcC.getItems().get(0).getLicense().getId());
        assertEquals(Acknowledgement.CONCLUDED, lcC.getItems().get(0).getLicense().getAcknowledgement());
        assertNotNull(lcC.getItems().get(1).getExpression());
        assertEquals(Acknowledgement.DECLARED, lcC.getItems().get(1).getExpression().getAcknowledgement());

        // Situation D: declared expression-detailed with texts + concluded license with text
        // Note: XML deserializer groups by element type: license first, then expression-detailed
        Component sitD = bom.getComponents().get(3);
        assertEquals("situation-D", sitD.getName());
        LicenseChoice lcD = sitD.getLicenses();
        assertNotNull(lcD);
        assertNotNull(lcD.getItems());
        assertEquals(2, lcD.getItems().size());
        assertNotNull(lcD.getItems().get(0).getLicense());
        assertEquals(Acknowledgement.CONCLUDED, lcD.getItems().get(0).getLicense().getAcknowledgement());
        assertNotNull(lcD.getItems().get(1).getExpressionDetailed());
        ExpressionDetailed edD = lcD.getItems().get(1).getExpressionDetailed();
        assertEquals("GPL-3.0-or-later OR GPL-2.0", edD.getExpression());
        assertEquals(Acknowledgement.DECLARED, edD.getAcknowledgement());
        assertNotNull(edD.getExpressionDetails());
        assertEquals(2, edD.getExpressionDetails().size());

        // Situation E: declared licenses with URLs + concluded expression-detailed with URLs
        Component sitE = bom.getComponents().get(4);
        assertEquals("situation-E", sitE.getName());
        LicenseChoice lcE = sitE.getLicenses();
        assertNotNull(lcE);
        assertNotNull(lcE.getItems());
        assertEquals(4, lcE.getItems().size());
        // 3 declared licenses with URLs
        assertNotNull(lcE.getItems().get(0).getLicense());
        assertEquals("https://example.com/licenses/MIT", lcE.getItems().get(0).getLicense().getUrl());
        // 1 concluded expression-detailed with URLs
        assertNotNull(lcE.getItems().get(3).getExpressionDetailed());
        ExpressionDetailed edE = lcE.getItems().get(3).getExpressionDetailed();
        assertEquals(Acknowledgement.CONCLUDED, edE.getAcknowledgement());
        assertNotNull(edE.getExpressionDetails());
        assertEquals(3, edE.getExpressionDetails().size());
        assertEquals("https://example.com/licenses/MIT", edE.getExpressionDetails().get(0).getUrl());
    }

    @Test
    public void schema17_license_backward_compat_getLicenses() throws Exception {
        final Bom bom = getXmlBom("1.7/valid-license-choice-1.7.xml");

        Component component = bom.getComponents().get(0);
        LicenseChoice lc = component.getLicenses();

        // Deprecated getLicenses() should still return only License items
        assertNotNull(lc.getLicenses());
        assertEquals(2, lc.getLicenses().size());
        assertEquals("Apache-2.0", lc.getLicenses().get(0).getId());
        assertEquals("My Own License", lc.getLicenses().get(1).getName());

        // Deprecated getExpression() should return the first expression
        assertNotNull(lc.getExpression());
        assertEquals("EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0", lc.getExpression().getValue());

        // getExpressionDetailed() should return the first expression-detailed
        assertNotNull(lc.getExpressionDetailed());
        assertEquals("LicenseRef-MIT-Style-2", lc.getExpressionDetailed().getExpression());
    }

    @Test
    public void schema16_ml_considerations() throws Exception {
        final Bom bom = getXmlBom("1.6/valid-machine-learning-considerations-env-1.6.xml");

        assertNotNull(bom.getComponents());
        ModelCard mc = bom.getComponents().get(0).getModelCard();
        assertNotNull(mc);

        Considerations considerations = mc.getConsiderations();
        assertNotNull(considerations);

        EnvironmentalConsideration ec = considerations.getEnvironmentalConsiderations();
        assertNotNull(ec);

        assertEquals(1, ec.getEnergyConsumptions().size());

        EnergyConsumption eec = ec.getEnergyConsumptions().get(0);

        assertEquals(Activity.TRAINING, eec.getActivity());
        assertEquals(Unit.KWH, eec.getActivityEnergyCost().getUnit());
        assertEquals(0.4, eec.getActivityEnergyCost().getValue());
        assertEquals(org.cyclonedx.model.component.modelCard.consideration.consumption.co2.Unit.TCO2EQ,
            eec.getCo2CostEquivalent().getUnit());
        assertEquals(31.22, eec.getCo2CostEquivalent().getValue());
        assertEquals(org.cyclonedx.model.component.modelCard.consideration.consumption.co2.Unit.TCO2EQ,
            eec.getCo2CostOffset().getUnit());
        assertEquals(31.22, eec.getCo2CostOffset().getValue());

        assertNull(eec.getProperties());
        assertEquals(1, eec.getEnergyProviders().size());

        EnergyProvider ep = eec.getEnergyProviders().get(0);

        assertEquals("Meta data-center, US-East", ep.getDescription());
        assertNull(ep.getExternalReferences());
        assertNull(ep.getBomRef());
        assertNotNull(ep.getOrganization());

        assertEquals(EnergySource.NATURAL_GAS, ep.getEnergySource());

        assertEquals(0.4, ep.getEnergyProvided().getValue());
        assertEquals(Unit.KWH, ep.getEnergyProvided().getUnit());

        assertNull(ep.getOrganization().getAddress().getBomRef());
        assertEquals("United States", ep.getOrganization().getAddress().getCountry());
        assertEquals("Newark", ep.getOrganization().getAddress().getLocality());
        assertNull(ep.getOrganization().getAddress().getStreetAddress());
        assertNull(ep.getOrganization().getAddress().getPostalCode());
        assertNull(ep.getOrganization().getAddress().getPostOfficeBoxNumber());
        assertEquals("New Jersey", ep.getOrganization().getAddress().getRegion());
        assertNull(eec.getProperties());
    }

    @Test
    public void schema16_ml_content() throws Exception {
        final Bom bom = getXmlBom("1.6/valid-machine-learning-1.6.xml");

        assertNotNull(bom.getComponents());
        ModelCard mc = bom.getComponents().get(0).getModelCard();
        assertNotNull(mc);

        ModelParameters mp = mc.getModelParameters();
        assertNotNull(mp);

        Content content = mp.getDatasets().get(0).getComponentData().getContents();
        assertNotNull(content);

        assertEquals(2, content.getProperties().size());
    }

    @Test
    public void schema16_component_identifiers() throws Exception {
        final Bom bom = getXmlBom("1.6/valid-component-identifiers-1.6.xml");

        assertIdentifiers(bom);
    }

    @Test
    public void schema16_tags() throws Exception {
        final Bom bom = getXmlBom("1.6/valid-tags-1.6.xml");
        assertTags(bom);
    }

    @Test
    public void schema16_manufacture() throws Exception {
        final Bom bom = getXmlBom("1.6/valid-metadata-manufacture-1.6.xml");

        assertNotNull(bom.getMetadata());
        OrganizationalEntity manufacture = bom.getMetadata().getManufacture();
        assertNotNull(manufacture);
        assertManufacturerMetadata(manufacture, Version.VERSION_16, true);
    }

    @Test
    public void schema16_manufacturer() throws Exception {
        final Bom bom = getXmlBom("1.6/valid-metadata-manufacturer-1.6.xml");

        assertNotNull(bom.getMetadata());
        OrganizationalEntity manufacturer = bom.getMetadata().getManufacturer();
        assertNotNull(manufacturer);
        assertManufacturerMetadata(manufacturer, Version.VERSION_16, false);
    }

    @Test
    public void schema16_evidence() throws Exception {
        final Bom bom = getXmlBom("1.6/valid-evidence-1.6.xml");

        List<Identity> identities = bom.getComponents().get(1).getEvidence().getIdentities();
        assertEquals(3, identities.size());
        List<String> list = identities.stream().map(Identity::getConcludedValue).collect(Collectors.toList());
        assertTrue(list.containsAll(Arrays.asList("com.example", "example-project", "1.0.0")));
    }

    @Test
    public void schema16_attestation_standard() throws Exception {
        final Bom bom = getXmlBom("1.6/valid-standard-1.6.xml");

        assertNotNull(bom.getDefinitions());
        List<Standard> standards = bom.getDefinitions().getStandards();
        assertEquals(1, standards.size());

        Standard standard = standards.get(0);
        assertEquals("standard-1", standard.getBomRef());
        assertEquals("Description here", standard.getDescription());
        assertEquals("Sample Standard", standard.getName());
        assertEquals("Acme Inc", standard.getOwner());
        assertEquals("1.0.0", standard.getVersion());

        //Requirements
        assertEquals(3, standard.getRequirements().size());
        Requirement requirement = standard.getRequirements().get(2);
        assertEquals("requirement-1.1.1", requirement.getBomRef());
        assertEquals("Text of the requirement here", requirement.getText());
        assertEquals("v1.1.1", requirement.getIdentifier());
        assertEquals("requirement-1.1", requirement.getParent());
        assertEquals("Supplemental text here", requirement.getDescriptions().get(0));
        assertEquals(1, requirement.getOpenCre().size());
        assertNull(requirement.getExternalReferences());
        assertNull(requirement.getProperties());
        assertNull(requirement.getTitle());

        //Levels
        assertEquals(3, standard.getLevels().size());
        Level level = standard.getLevels().get(0);
        assertEquals("Level 1", level.getIdentifier());
        assertEquals("Description here", level.getDescription());
        assertEquals("level-1", level.getBomRef());
        assertNull(level.getTitle());
        assertEquals(1, level.getRequirements().size());
        assertEquals("requirement-1.1.1", level.getRequirements().get(0));

        assertNull(standard.getSignature());
    }

    @Test
    public void schema16_attestation() throws Exception {
        final Bom bom = getXmlBom("1.6/valid-attestation-1.6.xml");

        assertNotNull(bom.getDeclarations());

        //Assessors
        List<Assessor> assessors = bom.getDeclarations().getAssessors();
        assertEquals(1, assessors.size());

        Assessor assessor = assessors.get(0);
        assertEquals(true, assessor.getThirdParty());
        assertEquals("Assessors Inc", assessor.getOrganization().getName());
        assertEquals("assessor-1", assessor.getBomRef());

        //Attestations
        List<Attestation> attestations = bom.getDeclarations().getAttestations();
        assertEquals(1, attestations.size());

        Attestation attestation = attestations.get(0);
        assertEquals("Attestation summary here", attestation.getSummary());
        assertEquals("assessor-1", attestation.getAssessor());
        assertEquals(1, attestation.getMap().size());

        AttestationMap map = attestation.getMap().get(0);
        assertEquals("requirement-1", map.getRequirement());
        assertEquals("claim-1", map.getClaims().get(0));
        assertEquals("counterClaim-1", map.getCounterClaims().get(0));

        Conformance conformance = map.getConformance();
        assertEquals(0.8, conformance.getScore());
        assertEquals("Conformance rationale here", conformance.getRationale());
        assertEquals("mitigationStrategy-1", conformance.getMitigationStrategies().get(0));

        Confidence confidence = map.getConfidence();
        assertEquals(1.0, confidence.getScore());
        assertEquals("Confidence rationale here", confidence.getRationale());

        //Claims
        List<Claim> claims = bom.getDeclarations().getClaims();
        assertEquals(1, claims.size());

        Claim claim = claims.get(0);
        assertEquals("claim-1", claim.getBomRef());
        assertEquals("Confidence rationale here", confidence.getRationale());
        assertEquals("acme-inc", claim.getTarget());
        assertEquals("Predicate here", claim.getPredicate());
        assertEquals("Reasoning here", claim.getReasoning());
        assertEquals("evidence-1", claim.getEvidence().get(0));
        assertEquals("counterEvidence-1", claim.getCounterEvidence().get(0));
        assertEquals("mitigationStrategy-1", claim.getMitigationStrategies().get(0));

        ExternalReference er = claim.getExternalReferences().get(0);
        assertEquals("https://alm.example.com", er.getUrl());
        assertEquals(ExternalReference.Type.ISSUE_TRACKER, er.getType());

        //Evidence
        List<Evidence> evidences = bom.getDeclarations().getEvidence();
        assertEquals(3, evidences.size());

        Evidence evidence = evidences.get(0);
        assertEquals("evidence-1", evidence.getBomRef());
        assertEquals("internal.com.acme.someProperty", evidence.getPropertyName());
        assertEquals("Description here", evidence.getDescription());
        assertNotNull(evidence.getCreated());
        assertNotNull(evidence.getExpires());
        assertEquals("Mary", evidence.getAuthor().getName());
        assertEquals("Jane", evidence.getReviewer().getName());

        Data data = evidence.getData().get(0);
        assertEquals("Name of the data", data.getName());
        assertEquals("PII", data.getClassification());
        assertEquals("Describe sensitive data here", data.getSensitiveData().get(0));
        assertEquals("Evidence here", data.getContents().getAttachment().getText());

        //Targets
        Targets targets = bom.getDeclarations().getTargets();
        assertNotNull(targets);
        assertEquals(1, targets.getOrganizations().size());

        //Affirmation
        Affirmation affirmation = bom.getDeclarations().getAffirmation();
        assertNotNull(affirmation);

        assertEquals("I certify, to the best of my knowledge, that all information is correct...",
            affirmation.getStatement());
        assertEquals(2, affirmation.getSignatories().size());

        Signatory s1 = affirmation.getSignatories().get(0);
        assertEquals("Tom", s1.getName());
        assertEquals("CEO", s1.getRole());
        assertNull(s1.getSignature());
        assertNull(s1.getOrganization());
        assertNull(s1.getExternalReference());

        Signatory s2 = affirmation.getSignatories().get(1);
        assertEquals("Jerry", s2.getName());
        assertEquals("COO", s2.getRole());
        assertEquals("Acme Inc", s2.getOrganization().getName());
        assertEquals("https://example.com/coo-sig.png", s2.getExternalReference().getUrl());
        assertNull(s2.getSignature());
    }

    @Test
    public void schema16_cbom() throws Exception {
        final Bom bom = getXmlBom("1.6/valid-cryptography-implementation-1.6.xml");

        assertEquals(3, bom.getComponents().size());
        assertEquals(2, bom.getDependencies().size());

        Component component = bom.getComponents().get(0);
        assertEquals("AES", component.getName());
        assertEquals("aes128gcm", component.getBomRef());
        assertEquals(Type.CRYPTOGRAPHIC_ASSET, component.getType());

        CryptoProperties cp = component.getCryptoProperties();
        assertEquals(AssetType.ALGORITHM, cp.getAssetType());
        assertEquals("oid:2.16.840.1.101.3.4.1.6", cp.getOid());

        AlgorithmProperties ap = cp.getAlgorithmProperties();
        assertEquals(Primitive.AE, ap.getPrimitive());
        assertEquals("128", ap.getParameterSetIdentifier());
        assertEquals(ExecutionEnvironment.SOFTWARE_PLAIN_RAM, ap.getExecutionEnvironment());
        assertEquals(ImplementationPlatform.X86_64, ap.getImplementationPlatform());
        assertEquals(CertificationLevel.NONE, ap.getCertificationLevel().get(0));
        assertEquals(Mode.GCM, ap.getMode());
        assertEquals(128, ap.getClassicalSecurityLevel());
        assertEquals(1, ap.getNistQuantumSecurityLevel());

        assertTrue(new ArrayList<>(ap.getCryptoFunctions())
            .containsAll(Arrays.asList(CryptoFunction.KEYGEN, CryptoFunction.ENCRYPT, CryptoFunction.DECRYPT,
                CryptoFunction.TAG)));
    }

    @Test
    public void testIssue562Regression() throws Exception {
        final Bom bom = getXmlBom("regression/issue562.xml");
        assertEquals(2, bom.getMetadata().getToolChoice().getComponents().size());
        assertEquals(2, bom.getMetadata().getAuthors().size());
    }

    @Test
    public void testIssue492Regression() throws Exception {
        final Bom bom = getXmlBom("regression/issue492.xml");
        assertEquals(2, bom.getMetadata().getTools().size());
    }

    @Test
    void validateShouldNotBeVulnerableToXxe() throws Exception {
        final byte[] bomBytes;
        try (final InputStream bomInputStream = getClass().getResourceAsStream("/security/xxe-protection.xml")) {
            assertThat(bomInputStream).isNotNull();
            bomBytes = IOUtils.toByteArray(bomInputStream);
        }

        final List<ParseException> validationFailures = new XmlParser().validate(bomBytes);
        assertThat(validationFailures).extracting(Throwable::getMessage).allSatisfy(
                failureMessage -> assertThat(failureMessage)
                        .contains("not allowed due to restriction set by the accessExternalDTD property"));
    }

}
