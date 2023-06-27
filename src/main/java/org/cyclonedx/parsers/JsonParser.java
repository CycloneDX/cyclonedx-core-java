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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.model.Bom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * JsonParser is responsible for validating and parsing CycloneDX bill-of-material
 * JSON documents and returning a {@link Bom} object.
 * @since 3.0.0
 */
@SuppressWarnings("unused")
public class JsonParser extends CycloneDxSchema implements Parser {

    private final ObjectMapper mapper;

    public JsonParser() {
        mapper = new ObjectMapper();
    }

    /**
     * {@inheritDoc}
     */
    public Bom parse(final File file) throws ParseException {
        try {
            return mapper.readValue(file, Bom.class);
        } catch (IOException e) {
            throw new ParseException("Unable to parse BOM from File", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Bom parse(final byte[] bomBytes) throws ParseException {
        try {
            return mapper.readValue(bomBytes, Bom.class);
        } catch (RuntimeException | IOException e) {
            throw new ParseException("Unable to parse BOM from byte array", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Bom parse(final InputStream inputStream) throws ParseException {
        try {
            return mapper.readValue(inputStream, Bom.class);
        } catch (IOException e) {
            throw new ParseException("Unable to parse BOM from InputStream", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Bom parse(final Reader reader) throws ParseException {
        try {
            return mapper.readValue(reader, Bom.class);
        } catch (IOException e) {
            throw new ParseException("Unable to parse BOM from Reader", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final File file) throws IOException {
        return validate(file, CycloneDxSchema.VERSION_LATEST);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final File file, final CycloneDxSchema.Version schemaVersion) throws IOException {
        return validate(FileUtils.readFileToString(file, StandardCharsets.UTF_8), schemaVersion);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final byte[] bomBytes) throws IOException {
        return validate(bomBytes, CycloneDxSchema.VERSION_LATEST);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final byte[] bomBytes, final CycloneDxSchema.Version schemaVersion) throws IOException {
        return validate(new String(bomBytes), schemaVersion);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final Reader reader) throws IOException {
        return validate(reader, CycloneDxSchema.VERSION_LATEST);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final Reader reader, final CycloneDxSchema.Version schemaVersion) throws IOException {
        return validate(IOUtils.toString(reader), schemaVersion);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final InputStream inputStream) throws IOException {
        return validate(inputStream, CycloneDxSchema.VERSION_LATEST);
    }

    /**
     * {@inheritDoc}
     */
    public List<ParseException> validate(final InputStream inputStream, final CycloneDxSchema.Version schemaVersion) throws IOException {
        return validate(IOUtils.toString(inputStream, StandardCharsets.UTF_8), schemaVersion);
    }

    /**
     * Verifies a CycloneDX BOM conforms to the specification through JSON validation.
     * @param bomString the CycloneDX BOM to validate
     * @param schemaVersion the schema version to validate against
     * @return true is the file is a valid BOM, false if not
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    public List<ParseException> validate(final String bomString, final CycloneDxSchema.Version schemaVersion) throws IOException {
        return validate(mapper.readTree(bomString), schemaVersion);
    }

    /**
     * Verifies a CycloneDX BOM conforms to the specification through JSON validation.
     * @param bomJson the CycloneDX BOM to validate
     * @param schemaVersion the schema version to validate against
     * @return true is the file is a valid BOM, false if not
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    public List<ParseException> validate(final JsonNode bomJson, final CycloneDxSchema.Version schemaVersion) throws IOException {
        final List<ParseException> exceptions = new ArrayList<>();
        Set<ValidationMessage> errors = getJsonSchema(schemaVersion, mapper).validate(mapper.readTree(bomJson.toString()));
        for (ValidationMessage message: errors) {
            exceptions.add(new ParseException(message.getMessage()));
        }
        return exceptions;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final File file) throws IOException {
        return validate(file).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final File file, final CycloneDxSchema.Version schemaVersion) throws IOException {
        return validate(file, schemaVersion).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final byte[] bomBytes) throws IOException {
        return validate(bomBytes).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final byte[] bomBytes, final CycloneDxSchema.Version schemaVersion) throws IOException {
        return validate(bomBytes, schemaVersion).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final Reader reader) throws IOException {
        return validate(reader).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final Reader reader, final CycloneDxSchema.Version schemaVersion) throws IOException {
        return validate(reader, schemaVersion).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final InputStream inputStream) throws IOException {
        return validate(inputStream).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final InputStream inputStream, final CycloneDxSchema.Version schemaVersion) throws IOException {
        return validate(inputStream, schemaVersion).isEmpty();
    }
}
