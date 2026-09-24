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

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.cyclonedx.Format;
import org.cyclonedx.Version;
import org.cyclonedx.generators.BomGeneratorFactory;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.LicenseItem;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.license.Expression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class MetadataLicensesTest {

    static Stream<Arguments> licenseCases() {
        return Stream.of(Format.JSON, Format.XML).flatMap(format -> Stream.of(
                Arguments.of(format, Version.VERSION_16, "license", licenses(license("MIT"))),
                Arguments.of(format, Version.VERSION_16, "licenses", licenses(license("MIT"), license("BSD-3-Clause"))),
                Arguments.of(format, Version.VERSION_16, "expression", licenses(expression("MIT OR Apache-2.0"))),
                Arguments.of(format, Version.VERSION_17, "licenses", licenses(license("MIT"), license("BSD-3-Clause"))),
                Arguments.of(format, Version.VERSION_17, "expression", licenses(expression("MIT OR Apache-2.0"))),
                Arguments.of(format, Version.VERSION_17, "expressions", licenses(expression("MIT"), expression("Apache-2.0"))),
                Arguments.of(format, Version.VERSION_17, "license then expression", licenses(license("MIT"), expression("Apache-2.0 OR BSD-3-Clause"))),
                Arguments.of(format, Version.VERSION_17, "expression then license", licenses(expression("Apache-2.0 OR BSD-3-Clause"), license("MIT")))));
    }

    @ParameterizedTest(name = "{0} {1}: {2}")
    @MethodSource("licenseCases")
    void preservesMetadataLicenses(Format format, Version version, String description, LicenseChoice licenses) throws Exception {
        Bom original = new Bom();
        Metadata metadata = new Metadata();
        metadata.setLicenses(licenses);
        original.setMetadata(metadata);

        Parser parser = format == Format.JSON ? new JsonParser() : new XmlParser();
        byte[] serialized = serialize(original, format, version);
        assertThat(parser.validate(serialized, version)).isEmpty();

        Bom parsed = parser.parse(serialized);
        assertThat(parsed.getMetadata().getLicenses().getItems())
                .containsExactlyInAnyOrderElementsOf(licenses.getItems());

        byte[] reserialized = serialize(parsed, format, version);
        assertThat(parser.validate(reserialized, version)).isEmpty();
        assertThat(parser.parse(reserialized).getMetadata().getLicenses().getItems())
                .containsExactlyInAnyOrderElementsOf(licenses.getItems());
    }

    private static byte[] serialize(Bom bom, Format format, Version version) throws Exception {
        String value = format == Format.JSON
                ? BomGeneratorFactory.createJson(version, bom).toJsonString()
                : BomGeneratorFactory.createXml(version, bom).toXmlString();
        return value.getBytes(StandardCharsets.UTF_8);
    }

    @ParameterizedTest
    @EnumSource(Format.class)
    void rejectsMixedMetadataLicensesInSchema16(Format format) throws Exception {
        Bom original = new Bom();
        Metadata metadata = new Metadata();
        metadata.setLicenses(licenses(license("MIT"), expression("Apache-2.0")));
        original.setMetadata(metadata);

        Parser parser = format == Format.JSON ? new JsonParser() : new XmlParser();
        String serialized = new String(serialize(original, format, Version.VERSION_17), StandardCharsets.UTF_8);
        assertThat(parser.validate(serialized.getBytes(StandardCharsets.UTF_8), Version.VERSION_17)).isEmpty();
        byte[] invalidFor16 = serialized.replace("1.7", "1.6").getBytes(StandardCharsets.UTF_8);
        assertThat(parser.validate(invalidFor16, Version.VERSION_16)).isNotEmpty();
    }

    @Test
    void preservesInterleavedXmlLicenses() throws Exception {
        byte[] serialized = ("<bom xmlns=\"http://cyclonedx.org/schema/bom/1.7\" version=\"1\">"
                + "<metadata><licenses><license><id>MIT</id></license>"
                + "<expression>Apache-2.0</expression><license><id>BSD-3-Clause</id></license>"
                + "</licenses></metadata></bom>").getBytes(StandardCharsets.UTF_8);
        Parser parser = new XmlParser();
        assertThat(parser.validate(serialized, Version.VERSION_17)).isEmpty();
        assertThat(parser.parse(serialized).getMetadata().getLicenses().getItems())
                .containsExactlyInAnyOrder(license("MIT"), expression("Apache-2.0"), license("BSD-3-Clause"));
    }

    private static LicenseChoice licenses(LicenseItem... items) {
        LicenseChoice choice = new LicenseChoice();
        for (LicenseItem item : items) {
            choice.addItem(item);
        }
        return choice;
    }

    private static LicenseItem license(String id) {
        License license = new License();
        license.setId(id);
        return LicenseItem.ofLicense(license);
    }

    private static LicenseItem expression(String value) {
        return LicenseItem.ofExpression(new Expression(value));
    }
}
