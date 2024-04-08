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
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.component.ModelCard;
import org.cyclonedx.model.component.Tags;
import org.cyclonedx.model.component.modelCard.Considerations;
import org.cyclonedx.model.component.modelCard.consideration.EnvironmentalConsideration;
import org.cyclonedx.model.component.modelCard.consideration.consumption.Activity;
import org.cyclonedx.model.component.modelCard.consideration.consumption.EnergyConsumption;
import org.cyclonedx.model.component.modelCard.consideration.consumption.energy.EnergyProvider;
import org.cyclonedx.model.component.modelCard.consideration.consumption.energy.EnergySource;
import org.cyclonedx.model.component.modelCard.consideration.consumption.energy.Unit;
import org.cyclonedx.model.license.Expression;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        assertEquals("(CDDL-1.0 OR GPL-2.0-with-classpath-exception)", c3.getLicenseChoice().getExpression().getId());

        assertServices(bom);

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

        assertServices(bom);

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
        assertServices(bom);
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
        assertServices(bom);
        assertCompositions(bom, Version.VERSION_15);
        assertVulnerabilities(bom, Version.VERSION_15);

        //Assert Metadata License Choice
        assertEquals(2, bom.getMetadata().getLicenseChoice().getLicenses().size());

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
    public void schema16_license_id_acknowledgement() throws Exception {
        final Bom bom  = getJsonBom("1.6/valid-license-id-1.6.json");

        assertNotNull(bom.getComponents());
        LicenseChoice lc = bom.getComponents().get(0).getLicenseChoice();
        assertNotNull(lc.getLicenses());
        assertEquals(1, lc.getLicenses().size());

        License license = lc.getLicenses().get(0);
        assertEquals("Apache-2.0", license.getId());
        assertEquals("my-license", license.getBomRef());
        assertEquals("declared", license.getAcknowledgement());
    }

    @Test
    public void schema16_license_expression_acknowledgement() throws Exception {
        final Bom bom  = getJsonBom("1.6/valid-license-expression-1.6.json");

        assertNotNull(bom.getComponents());
        LicenseChoice lc = bom.getComponents().get(0).getLicenseChoice();
        assertNotNull(lc.getExpression());

        Expression expression = lc.getExpression();
        assertEquals("EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0", expression.getId());
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
        final Bom bom  = getJsonBom("1.6/valid-component-identifiers-1.6.json");

        assertNotNull(bom.getComponents());
        List<String> omnis = bom.getComponents().get(0).getOmniborId();
        assertEquals(2, omnis.size());
        assertTrue(omnis.containsAll(Arrays.asList("gitoid:blob:sha1:261eeb9e9f8b2b4b0d119366dda99c6fd7d35c64",
            "gitoid:blob:sha256:9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08")));

        List<String> swhid = bom.getComponents().get(0).getSwhid();
        assertEquals(2, swhid.size());
        assertTrue(swhid.containsAll(Arrays.asList("swh:1:cnt:94a9ed024d3859793618152ea559a168bbcbb5e2",
            "swh:1:dir:d198bc9d7a6bcf6db04f476d29314f157507d505")));
    }

    @Test
    public void schema16_tags() throws Exception {
        final Bom bom  = getJsonBom("1.6/valid-tags-1.6.json");

        assertNotNull(bom.getComponents());
        Tags tags = bom.getComponents().get(0).getTags();
        assertNotNull(tags);
        assertEquals(3, tags.getTags().size());
        assertTrue(tags.getTags().containsAll(Arrays.asList("json-parser", "javascript", "node.js")));

        assertNotNull(bom.getServices());
        tags = bom.getServices().get(0).getTags();
        assertNotNull(tags);
        assertEquals(4, tags.getTags().size());
        assertTrue(tags.getTags().containsAll(Arrays.asList("microservice", "golang", "aws", "us-east-1")));
    }

    @Test
    public void schema16_manufacture() throws Exception {
        final Bom bom  = getJsonBom("1.6/valid-metadata-manufacture-1.6.json");

        assertNotNull(bom.getMetadata());
        OrganizationalEntity manufacture = bom.getMetadata().getManufacture();
        assertNotNull(manufacture);
        assertManufacturerMetadata(manufacture, Version.VERSION_16, true);
    }

    @Test
    public void schema16_manufacturer() throws Exception {
        final Bom bom  = getJsonBom("1.6/valid-metadata-manufacturer-1.6.json");

        assertNotNull(bom.getMetadata());
        OrganizationalEntity manufacturer = bom.getMetadata().getManufacturer();
        assertNotNull(manufacturer);
        assertManufacturerMetadata(manufacturer, Version.VERSION_16, false);
    }

    @Test
    public void schema16_evidence() throws Exception {
        final Bom bom  = getJsonBom("1.6/valid-evidence-1.6.json");

        List<String> list =
            bom.getComponents().get(1).getEvidence().getIdentities().stream().map(i -> i.getConcludedValue()).
                collect(Collectors.toList());

        assertTrue(list.containsAll(Arrays.asList("com.example", "example-project", "1.0.0")));
    }
}
