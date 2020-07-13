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
package org.cyclonedx.parsers;

import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.model.Bom;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

public interface Parser {

    /**
     * Parses a CycloneDX BOM.
     * @param file a File to parse
     * @return a Bom object
     * @throws ParseException when errors are encountered
     * @since 3.0.0
     */
    Bom parse(File file) throws ParseException;

    /**
     * Parses a CycloneDX BOM.
     * @param bomBytes the byte array to parse
     * @return a Bom object
     * @throws ParseException when errors are encountered
     * @since 3.0.0
     */
    Bom parse(byte[] bomBytes) throws ParseException;

    /**
     * Parses a CycloneDX BOM.
     * @param reader the Reader from which to parse
     * @return a Bom object
     * @throws ParseException when errors are encountered
     * @since 3.0.0
     */
    Bom parse(Reader reader) throws ParseException;

    /**
     * Parses a CycloneDX BOM.
     * @param inputStream the InputStream from which to parse
     * @return a Bom object
     * @throws ParseException when errors are encountered
     * @since 3.0.0
     */
    Bom parse(InputStream inputStream) throws ParseException;

    /**
     * Validates a CycloneDX BOM.
     * @param file the CycloneDX BOM file to validate
     * @return a List of ParseException. If the size of the list is 0, validation was successful
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    List<ParseException> validate(File file) throws IOException;

    /**
     * Validates a CycloneDX BOM conforms to a specific specification version.
     * @param file the CycloneDX BOM file to validate
     * @param schemaVersion the schema version to validate against
     * @return a List of ParseException. If the size of the list is 0, validation was successful
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    List<ParseException> validate(File file, CycloneDxSchema.Version schemaVersion) throws IOException;

    /**
     * Validates a CycloneDX BOM.
     * @param bomBytes the byte array to validate
     * @return a List of ParseException. If the size of the list is 0, validation was successful
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    List<ParseException> validate(byte[] bomBytes) throws IOException;

    /**
     * Validates a CycloneDX BOM conforms to a specific specification version.
     * @param bomBytes the byte array to validate
     * @param schemaVersion the schema version to validate against
     * @return a List of ParseException. If the size of the list is 0, validation was successful
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    List<ParseException> validate(byte[] bomBytes, CycloneDxSchema.Version schemaVersion) throws IOException;

    /**
     * Validates a CycloneDX BOM.
     * @param reader the Reader from which to parse
     * @return a List of ParseException. If the size of the list is 0, validation was successful
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    List<ParseException> validate(Reader reader) throws IOException;

    /**
     * Validates a CycloneDX BOM conforms to a specific specification version.
     * @param reader the Reader from which to parse
     * @param schemaVersion the schema version to validate against
     * @return a List of ParseException. If the size of the list is 0, validation was successful
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    List<ParseException> validate(Reader reader, CycloneDxSchema.Version schemaVersion) throws IOException;

    /**
     * Validates a CycloneDX BOM.
     * @param inputStream the InputStream from which to validate
     * @return a List of ParseException. If the size of the list is 0, validation was successful
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    List<ParseException> validate(InputStream inputStream) throws IOException;

    /**
     * Validates a CycloneDX BOM conforms to a specific specification version.
     * @param inputStream the InputStream from which to validate
     * @param schemaVersion the schema version to validate against
     * @return a List of ParseException. If the size of the list is 0, validation was successful
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    List<ParseException> validate(InputStream inputStream, CycloneDxSchema.Version schemaVersion) throws IOException;

    /**
     * Verifies a CycloneDX BOM conforms to the latest version of the specification.
     * @param file the CycloneDX BOM file to validate
     * @return true if the file is a valid BOM, false if not
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    boolean isValid(File file) throws IOException;

    /**
     * Verifies a CycloneDX BOM conforms to the specified specification version.
     * @param file the CycloneDX BOM file to validate
     * @param schemaVersion the schema version to validate against
     * @return true if the file is a valid BOM, false if not
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    boolean isValid(File file, CycloneDxSchema.Version schemaVersion) throws IOException;

    /**
     * Verifies a CycloneDX BOM conforms to the latest version of the specification.
     * @param bomBytes the byte array to validate
     * @return true if the file is a valid BOM, false if not
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    boolean isValid(byte[] bomBytes) throws IOException;

    /**
     * Verifies a CycloneDX BOM conforms to the specified specification version.
     * @param bomBytes the byte array to validate
     * @param schemaVersion the schema version to validate against
     * @return true if the file is a valid BOM, false if not
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    boolean isValid(byte[] bomBytes, CycloneDxSchema.Version schemaVersion) throws IOException;

    /**
     * Verifies a CycloneDX BOM conforms to the latest version of the specification.
     * @param reader the Reader from which to validate
     * @return true if the file is a valid BOM, false if not
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    boolean isValid(Reader reader) throws IOException;

    /**
     * Verifies a CycloneDX BOM conforms to the specified specification version.
     * @param reader the Reader from which to validate
     * @param schemaVersion the schema version to validate against
     * @return true if the file is a valid BOM, false if not
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    boolean isValid(Reader reader, CycloneDxSchema.Version schemaVersion) throws IOException;

    /**
     * Verifies a CycloneDX BOM conforms to the latest version of the specification.
     * @param inputStream the InputStream from which to validate
     * @return true if the file is a valid BOM, false if not
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    boolean isValid(InputStream inputStream) throws IOException;

    /**
     * Verifies a CycloneDX BOM conforms to the specified specification version.
     * @param inputStream the InputStream from which to validate
     * @param schemaVersion the schema version to validate against
     * @return true if the file is a valid BOM, false if not
     * @throws IOException when errors are encountered
     * @since 3.0.0
     */
    boolean isValid(InputStream inputStream, CycloneDxSchema.Version schemaVersion) throws IOException;

}
