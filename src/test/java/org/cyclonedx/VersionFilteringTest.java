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
package org.cyclonedx;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.generators.BomGeneratorFactory;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Hash;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.ReleaseNotes;
import org.cyclonedx.model.Service;
import org.cyclonedx.parsers.JsonParser;
import org.cyclonedx.parsers.XmlParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies that version-specific fields are correctly filtered when generating
 * BOMs for older CycloneDX versions. A BOM populated with features from the
 * latest version (1.7) must pass schema validation when generated for any
 * supported target version.
 *
 * This test ensures that:
 * <ul>
 *   <li>{@code @VersionFilter} annotations on model fields cause automatic removal
 *       by {@code CustomSerializerModifier} for classes using default Jackson serialization</li>
 *   <li>Custom serializers (LicenseChoiceSerializer, ExternalReferenceSerializer, etc.)
 *       properly check versions via {@code SerializerUtils.shouldSerializeField()}</li>
 *   <li>Enum constants with {@code @VersionFilter} (ExternalReference.Type, Hash.Algorithm,
 *       Component.Type) are filtered at serialization time</li>
 * </ul>
 */
public class VersionFilteringTest {

    /**
     * Creates a BOM populated with features spanning all CycloneDX versions.
     * Fields from newer versions should be automatically filtered out when
     * generating for older versions.
     */
    private Bom createFullFeaturedBom() {
        Bom bom = new Bom();
        bom.setSerialNumber("urn:uuid:3e671687-395b-41f5-a30f-a58921a69b79");

        // Metadata with timestamp (v1.0+)
        Metadata metadata = new Metadata();
        metadata.setTimestamp(new Date());
        bom.setMetadata(metadata);

        // Component with v1.0+ fields
        Component component = new Component();
        component.setType(Component.Type.LIBRARY);
        component.setName("acme-lib");
        component.setVersion("1.0.0");
        component.setPublisher("Acme Inc.");
        component.setDescription("A test library");
        component.setModified(false);

        // bom-ref (v1.1+)
        component.setBomRef("comp-1");

        // ExternalReferences with types from various versions
        List<ExternalReference> extRefs = new ArrayList<>();

        // v1.0+ type
        ExternalReference websiteRef = new ExternalReference();
        websiteRef.setType(ExternalReference.Type.WEBSITE);
        websiteRef.setUrl("https://example.com");
        extRefs.add(websiteRef);

        // v1.4+ type
        ExternalReference releaseNotesRef = new ExternalReference();
        releaseNotesRef.setType(ExternalReference.Type.RELEASE_NOTES);
        releaseNotesRef.setUrl("https://example.com/releases");
        extRefs.add(releaseNotesRef);

        // v1.5+ type
        ExternalReference attestationRef = new ExternalReference();
        attestationRef.setType(ExternalReference.Type.ATTESTATION);
        attestationRef.setUrl("https://example.com/attestation");
        extRefs.add(attestationRef);

        // v1.6+ type
        ExternalReference sourceDistRef = new ExternalReference();
        sourceDistRef.setType(ExternalReference.Type.SOURCE_DISTRIBUTION);
        sourceDistRef.setUrl("https://example.com/source");
        extRefs.add(sourceDistRef);

        component.setExternalReferences(extRefs);

        // Hashes (v1.0+)
        List<Hash> hashes = new ArrayList<>();
        hashes.add(new Hash(Hash.Algorithm.SHA_256,
            "a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2"));
        component.setHashes(hashes);

        bom.setComponents(Collections.singletonList(component));

        // Service with releaseNotes (v1.4+)
        Service service = new Service();
        service.setName("acme-service");
        service.setBomRef("svc-1");
        ReleaseNotes releaseNotes = new ReleaseNotes();
        releaseNotes.setType("major");
        releaseNotes.setTitle("v2.0");
        service.setReleaseNotes(releaseNotes);
        bom.setServices(Collections.singletonList(service));

        return bom;
    }

    static Stream<Arguments> allXmlVersions() {
        return Arrays.stream(Version.values())
            .filter(v -> v.getFormats().contains(Format.XML))
            .map(Arguments::of);
    }

    static Stream<Arguments> allJsonVersions() {
        return Arrays.stream(Version.values())
            .filter(v -> v.getFormats().contains(Format.JSON))
            .map(Arguments::of);
    }

    @ParameterizedTest(name = "XML v{0}: full-featured BOM passes schema validation")
    @MethodSource("allXmlVersions")
    void xmlSchemaValidation(Version targetVersion) throws Exception {
        Bom bom = createFullFeaturedBom();
        BomXmlGenerator generator = BomGeneratorFactory.createXml(targetVersion, bom);
        Document doc = generator.generate();

        byte[] xmlBytes = generator.toXmlString().getBytes(StandardCharsets.UTF_8);
        XmlParser parser = new XmlParser();
        List<ParseException> errors = parser.validate(xmlBytes, targetVersion);

        assertTrue(errors.isEmpty(),
            String.format("XML v%s schema validation failed with %d error(s): %s",
                targetVersion.getVersionString(), errors.size(),
                errors.isEmpty() ? "" : errors.get(0).getMessage()));
    }

    @ParameterizedTest(name = "JSON v{0}: full-featured BOM passes schema validation")
    @MethodSource("allJsonVersions")
    void jsonSchemaValidation(Version targetVersion) throws Exception {
        Bom bom = createFullFeaturedBom();
        BomJsonGenerator generator = BomGeneratorFactory.createJson(targetVersion, bom);
        String json = generator.toJsonString();

        JsonParser parser = new JsonParser();
        List<ParseException> errors = parser.validate(json.getBytes(StandardCharsets.UTF_8), targetVersion);

        assertTrue(errors.isEmpty(),
            String.format("JSON v%s schema validation failed with %d error(s): %s",
                targetVersion.getVersionString(), errors.size(),
                errors.isEmpty() ? "" : errors.get(0).getMessage()));
    }

    @ParameterizedTest(name = "XML v{0}: version-gated ExternalReference types are filtered")
    @MethodSource("allXmlVersions")
    void xmlExternalReferenceTypeFiltering(Version targetVersion) throws Exception {
        Bom bom = createFullFeaturedBom();
        BomXmlGenerator generator = BomGeneratorFactory.createXml(targetVersion, bom);
        String xml = generator.toXmlString();

        // externalReferences on Component are v1.1+ (VersionFilter(VERSION_11))
        if (targetVersion.getVersion() >= Version.VERSION_11.getVersion()) {
            // WEBSITE (v1.0+) should be present when externalReferences are supported
            assertTrue(xml.contains("https://example.com"),
                "WEBSITE reference should be present in v" + targetVersion.getVersionString());
        }

        // RELEASE_NOTES (v1.4+)
        if (targetVersion.getVersion() < Version.VERSION_14.getVersion()) {
            assertFalse(xml.contains("release-notes"),
                "release-notes type should not appear in v" + targetVersion.getVersionString());
        }

        // ATTESTATION (v1.5+)
        if (targetVersion.getVersion() < Version.VERSION_15.getVersion()) {
            assertFalse(xml.contains("attestation"),
                "attestation type should not appear in v" + targetVersion.getVersionString());
        }

        // SOURCE_DISTRIBUTION (v1.6+)
        if (targetVersion.getVersion() < Version.VERSION_16.getVersion()) {
            assertFalse(xml.contains("source-distribution"),
                "source-distribution type should not appear in v" + targetVersion.getVersionString());
        }
    }

    @ParameterizedTest(name = "JSON v{0}: version-gated ExternalReference types are filtered")
    @MethodSource("allJsonVersions")
    void jsonExternalReferenceTypeFiltering(Version targetVersion) throws Exception {
        Bom bom = createFullFeaturedBom();
        BomJsonGenerator generator = BomGeneratorFactory.createJson(targetVersion, bom);
        String json = generator.toJsonString();

        // WEBSITE (v1.0+) should always be present
        assertTrue(json.contains("website"),
            "WEBSITE reference should be present in v" + targetVersion.getVersionString());

        // RELEASE_NOTES (v1.4+)
        if (targetVersion.getVersion() < Version.VERSION_14.getVersion()) {
            assertFalse(json.contains("release-notes"),
                "release-notes type should not appear in v" + targetVersion.getVersionString());
        }

        // ATTESTATION (v1.5+)
        if (targetVersion.getVersion() < Version.VERSION_15.getVersion()) {
            assertFalse(json.contains("attestation"),
                "attestation type should not appear in v" + targetVersion.getVersionString());
        }

        // SOURCE_DISTRIBUTION (v1.6+)
        if (targetVersion.getVersion() < Version.VERSION_16.getVersion()) {
            assertFalse(json.contains("source-distribution"),
                "source-distribution type should not appear in v" + targetVersion.getVersionString());
        }
    }

    @ParameterizedTest(name = "JSON v{0}: Service.releaseNotes filtered before v1.4")
    @MethodSource("allJsonVersions")
    void jsonServiceReleaseNotesFiltering(Version targetVersion) throws Exception {
        Bom bom = createFullFeaturedBom();
        BomJsonGenerator generator = BomGeneratorFactory.createJson(targetVersion, bom);
        String json = generator.toJsonString();

        if (targetVersion.getVersion() < Version.VERSION_14.getVersion()) {
            assertFalse(json.contains("releaseNotes"),
                "releaseNotes should not appear in JSON v" + targetVersion.getVersionString());
        } else {
            assertTrue(json.contains("releaseNotes"),
                "releaseNotes should appear in JSON v" + targetVersion.getVersionString());
        }
    }

    @ParameterizedTest(name = "XML v{0}: Component.bomRef filtered before v1.1")
    @MethodSource("allXmlVersions")
    void xmlComponentBomRefFiltering(Version targetVersion) throws Exception {
        Bom bom = createFullFeaturedBom();
        BomXmlGenerator generator = BomGeneratorFactory.createXml(targetVersion, bom);
        String xml = generator.toXmlString();

        if (targetVersion.getVersion() < Version.VERSION_11.getVersion()) {
            assertFalse(xml.contains("bom-ref"),
                "bom-ref should not appear in XML v" + targetVersion.getVersionString());
        } else {
            assertTrue(xml.contains("bom-ref"),
                "bom-ref should appear in XML v" + targetVersion.getVersionString());
        }
    }

    @ParameterizedTest(name = "JSON v{0}: Component.bomRef filtered before v1.1")
    @MethodSource("allJsonVersions")
    void jsonComponentBomRefFiltering(Version targetVersion) throws Exception {
        Bom bom = createFullFeaturedBom();
        BomJsonGenerator generator = BomGeneratorFactory.createJson(targetVersion, bom);
        String json = generator.toJsonString();

        // JSON starts at v1.2, so bom-ref should always be present
        assertTrue(json.contains("bom-ref"),
            "bom-ref should appear in JSON v" + targetVersion.getVersionString());
    }
}
