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

import org.cyclonedx.Version;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Component.Type;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.OrganizationalEntity;
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
import org.cyclonedx.model.component.evidence.Identity;
import org.cyclonedx.model.component.modelCard.Considerations;
import org.cyclonedx.model.component.modelCard.consideration.EnvironmentalConsideration;
import org.cyclonedx.model.component.modelCard.consideration.consumption.Activity;
import org.cyclonedx.model.component.modelCard.consideration.consumption.EnergyConsumption;
import org.cyclonedx.model.component.modelCard.consideration.consumption.energy.EnergyProvider;
import org.cyclonedx.model.component.modelCard.consideration.consumption.energy.EnergySource;
import org.cyclonedx.model.component.modelCard.consideration.consumption.energy.Unit;
import org.cyclonedx.model.definition.Level;
import org.cyclonedx.model.definition.Requirement;
import org.cyclonedx.model.definition.Standard;
import org.cyclonedx.model.license.Expression;
import org.junit.jupiter.api.Test;
import java.io.File;
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

public class JsonParserTest
    extends AbstractParserTest
{

    @Test
    public void testValid12Bom() throws Exception {
        final File file = new File(Objects.requireNonNull(this.getClass().getResource("/bom-1.2.json")).getFile());
        final JsonParser parser = new JsonParser();
        Bom bom = parser.parse(file);
        assertTrue(parser.isValid(file, Version.VERSION_12));
        System.out.println(bom.getSerialNumber());
    }

    @Test
    public void testParsedObjects12Bom() throws Exception {
        final Bom bom = getJsonBom("bom-1.2.json");

        assertMetadata(bom, Version.VERSION_12);

        final List<Component> components = bom.getComponents();

        // Assertions for bom.components
        assertComponent(components.get(0), Component.Type.LIBRARY, "pkg:npm/acme/component@1.0.0");

        Component c3 = components.get(2);
        assertEquals(Component.Type.LIBRARY, c3.getType());
        assertEquals("pkg:maven/org.glassfish.hk2/osgi-resource-locator@1.0.1?type=jar", c3.getBomRef());
        assertEquals("GlassFish Community", c3.getPublisher());
        assertEquals("org.glassfish.hk2", c3.getGroup());
        assertEquals("osgi-resource-locator", c3.getName());
        assertEquals("1.0.1", c3.getVersion());
        assertEquals("(CDDL-1.0 OR GPL-2.0-with-classpath-exception)",
            c3.getLicenseChoice().getExpression().getValue());

        assertServices(bom, Version.VERSION_12);

        // Assertions for bom.dependencies
        assertEquals(2, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("pkg:npm/acme/component@1.0.0", d1.getRef());
        assertEquals(1, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        assertEquals("pkg:npm/acme/common@1.0.0", d11.getRef());
        assertNull(d11.getDependencies());
    }

    @Test
    public void testParsedObjects13Bom() throws Exception {
        final Bom bom = getJsonBom("bom-1.3.json");

        assertMetadata(bom, Version.VERSION_13);

        assertServices(bom, Version.VERSION_13);

        final List<Component> components = bom.getComponents();
        assertEquals(3, components.size());

        assertComponent(components.get(0), Component.Type.LIBRARY, "pkg:npm/acme/component@1.0.0");

        // Assertions for bom.dependencies
        assertEquals(2, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("pkg:npm/acme/component@1.0.0", d1.getRef());
        assertEquals(1, d1.getDependencies().size());
        Dependency d11 = d1.getDependencies().get(0);
        assertEquals("pkg:npm/acme/common@1.0.0", d11.getRef());
        assertNull(d11.getDependencies());
        Dependency d2 = bom.getDependencies().get(1);
        assertEquals("pkg:npm/acme/component@2.0.0", d2.getRef());
        assertEquals(3, d2.getDependencies().size());
        Dependency d21 = d2.getDependencies().get(0);
        assertEquals("pkg:npm/acme/common@1.1.0", d21.getRef());
        Dependency d22 = d2.getDependencies().get(1);
        assertEquals("pkg:npm/acme/common@2.2.0", d22.getRef());
    }

    @Test
    public void testValidBomLink() throws Exception {
        final File file =
            new File(Objects.requireNonNull(this.getClass().getResource("/bom-1.4-bomlink.json")).getFile());
        final JsonParser parser = new JsonParser();
        Bom bom = parser.parse(file);
        assertTrue(parser.isValid(file, Version.VERSION_14));
        ExternalReference ref = bom.getComponents().get(0).getExternalReferences().get(0);
        assertEquals("bom", ref.getType().getTypeName());
        assertEquals("urn:cdx:f08a6ccd-4dce-4759-bd84-c626675d60a7/1", ref.getUrl());
    }

    @Test
    public void testParsedObjects14Bom() throws Exception {
        final Bom bom = getJsonBom("bom-1.4.json");

        assertCommonBomProperties(bom, Version.VERSION_14);

        assertMetadata(bom.getMetadata(), Version.VERSION_14);
        assertComponent(bom, Version.VERSION_14);
        assertServices(bom, Version.VERSION_14);
        assertCompositions(bom, Version.VERSION_14);
        assertVulnerabilities(bom, Version.VERSION_14);

        // Dependencies
        assertEquals(2, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("pkg:npm/acme/component@1.0.0", d1.getRef());

        //Assert Bom Properties
        assertNull(bom.getProperties());

        //Assert Annotations
        assertAnnotations(bom, Version.VERSION_14);

        //Assert Formulation
        assertFormulation(bom, Version.VERSION_14);
    }

    @Test
    public void testParsedObjects15Bom() throws Exception {
        final Bom bom = getJsonBom("bom-1.5.json");

        assertCommonBomProperties(bom, Version.VERSION_15);

        assertMetadata(bom.getMetadata(), Version.VERSION_15);
        assertComponent(bom, Version.VERSION_15);
        assertServices(bom, Version.VERSION_15);
        assertCompositions(bom, Version.VERSION_15);
        assertVulnerabilities(bom, Version.VERSION_15);

        //Assert Metadata License Choice
        assertEquals(2, bom.getMetadata().getLicenseChoice().getLicenses().size());
        assertEquals(2, bom.getMetadata().getLicenses().getLicenses().size());

        // Dependencies
        assertEquals(2, bom.getDependencies().size());
        Dependency d1 = bom.getDependencies().get(0);
        assertNotNull(d1);
        assertEquals("pkg:npm/acme/component@1.0.0", d1.getRef());

        //Assert Bom Properties
        assertEquals(bom.getProperties().size(), 1);

        //Assert Annotations
        assertAnnotations(bom, Version.VERSION_15);

        //Assert Formulation
        assertFormulation(bom, Version.VERSION_15);
    }

    @Test
    public void testParsedObjects15Bom_validTools() throws Exception {
        final Bom bom = getJsonBom("1.5/valid-metadata-tool-1.5.json");
        assertCommonBomProperties(bom, Version.VERSION_15);
        assertMetadata_validTools(bom.getMetadata());
    }

    @Test
    public void testIssue336Regression() throws Exception {
        final Bom bom = getJsonBom("regression/issue336.json");
        assertEquals("foo", bom.getMetadata().getComponent().getProperties().get(0).getName());
        assertEquals("bar", bom.getMetadata().getComponent().getProperties().get(0).getValue());
    }
    
    @Test
    public void testIssue338RegressionWithSingleTool() throws Exception {
        final Bom bom = getJsonBom("regression/issue338-single-tool.json");
        assertEquals("acme-tool-a", bom.getMetadata().getTools().get(0).getName());
    }

    @Test
    public void testIssue338RegressionWithMultipleTools() throws Exception {
        final Bom bom = getJsonBom("regression/issue338-multiple-tools.json");
        assertEquals("acme-tool-a", bom.getMetadata().getTools().get(0).getName());
        assertEquals("acme-tool-b", bom.getMetadata().getTools().get(1).getName());
    }

    @Test
    public void testIssue343Regression() throws Exception {
        final Bom bom = getJsonBom("regression/issue343-empty-hashes.json");
        assertEquals(0, bom.getComponents().get(0).getHashes().size());
    }

    @Test
    public void testIssue507Regression() throws Exception {
        final Bom bom = getJsonBom("regression/issue507.json");
        assertThat(bom.getComponents()).hasSize(1);
        assertThat(bom.getComponents().get(0).getLicenses()).isNotNull();
        assertThat(bom.getComponents().get(0).getLicenses().getLicenses()).hasSize(1);
        assertThat(bom.getComponents().get(0).getLicenses().getLicenses().get(0).getLicensing()).isNotNull();
        assertThat(bom.getComponents().get(0).getLicenses().getLicenses().get(0).getLicensing().getPurchaser()).isNotNull();
        assertThat(bom.getComponents().get(0).getLicenses().getLicenses().get(0).getLicensing().getPurchaser().getOrganization()).isNotNull();
        assertThat(bom.getComponents().get(0).getLicenses().getLicenses().get(0).getLicensing().getPurchaser().getOrganization().getContacts()).hasSize(1);
    }

    @Test
    public void schema16_license_id_acknowledgement() throws Exception {
        final Bom bom = getJsonBom("1.6/valid-license-id-1.6.json");

        assertNotNull(bom.getComponents());
        LicenseChoice lc = bom.getComponents().get(0).getLicenses();
        assertNotNull(lc.getLicenses());
        assertEquals(1, lc.getLicenses().size());

        License license = lc.getLicenses().get(0);
        assertEquals("Apache-2.0", license.getId());
        assertEquals("my-license", license.getBomRef());
        assertEquals("declared", license.getAcknowledgement());
    }

    @Test
    public void schema16_license_expression_acknowledgement() throws Exception {
        final Bom bom = getJsonBom("1.6/valid-license-expression-1.6.json");

        assertNotNull(bom.getComponents());
        LicenseChoice lc = bom.getComponents().get(0).getLicenses();
        assertNotNull(lc.getExpression());

        Expression expression = lc.getExpression();
        assertEquals("EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0", expression.getValue());
        assertEquals("my-license", expression.getBomRef());
        assertEquals("declared", expression.getAcknowledgement());
    }

    @Test
    public void schema16_ml_considerations() throws Exception {
        final Bom bom = getJsonBom("1.6/valid-machine-learning-considerations-env-1.6.json");

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
    public void schema16_component_identifiers() throws Exception {
        final Bom bom = getJsonBom("1.6/valid-component-identifiers-1.6.json");

        assertIdentifiers(bom);
    }

    @Test
    public void schema16_tags() throws Exception {
        final Bom bom = getJsonBom("1.6/valid-tags-1.6.json");
        assertTags(bom);
    }

    @Test
    public void schema16_manufacture() throws Exception {
        final Bom bom = getJsonBom("1.6/valid-metadata-manufacture-1.6.json");

        assertNotNull(bom.getMetadata());
        OrganizationalEntity manufacture = bom.getMetadata().getManufacture();
        assertNotNull(manufacture);
        assertManufacturerMetadata(manufacture, Version.VERSION_16, true);
    }

    @Test
    public void schema16_manufacturer() throws Exception {
        final Bom bom = getJsonBom("1.6/valid-metadata-manufacturer-1.6.json");

        assertNotNull(bom.getMetadata());
        OrganizationalEntity manufacturer = bom.getMetadata().getManufacturer();
        assertNotNull(manufacturer);
        assertManufacturerMetadata(manufacturer, Version.VERSION_16, false);
    }

    @Test
    public void schema16_evidence() throws Exception {
        final Bom bom = getJsonBom("1.6/valid-evidence-1.6.json");

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
}
