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

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.model.Bom;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * BomParser is responsible for validating and parsing CycloneDX bill-of-material
 * documents and returning a high-level {@link Bom} object with all its components.
 * @since 1.1.0
 */
public class BomParser extends CycloneDxSchema {

    /**
     * Parses a CycloneDX BOM.
     *
     * @param file the BOM
     * @return an Bom object
     * @throws ParseException when errors are encountered
     * @since 1.1.0
     */
    public Bom parse(File file) throws ParseException {
        return parse(new StreamSource(file.getAbsolutePath()));
    }

    /**
     * Parses a CycloneDX BOM.
     *
     * @param bomBytes the BOM
     * @return an Bom object
     * @throws ParseException when errors are encountered
     * @since 1.1.0
     */
    public Bom parse(byte[] bomBytes) throws ParseException {
        return parse(new StreamSource(new ByteArrayInputStream(bomBytes)));
    }
    /**
     * Parses a CycloneDX BOM.
     *
     * @param streamSource the BOM
     * @return an Bom object
     * @throws ParseException when errors are encountered
     * @since 1.1.0
     */
    private Bom parse(StreamSource streamSource) throws ParseException {
        try {
            final Schema schema = getXmlSchema(CycloneDxSchema.Version.VERSION_11);
            final XmlMapper mapper = new XmlMapper();

            // Prevent XML External Entity Injection
            final XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            final XMLStreamReader xsr = new NamespaceStreamReaderDelegate(
                    xif.createXMLStreamReader(streamSource));
            final Bom bom = mapper.readValue(xsr, Bom.class);
            xsr.close();
            return bom;
        } catch (IOException | XMLStreamException | SAXException e) {
            throw new ParseException(e);
        }
    }

    /**
     * Verifies a CycloneDX BoM conforms to the latest version of the specification
     * through XML validation.
     * @param file the CycloneDX BoM file to validate
     * @return a List of SAXParseExceptions. If the size of the list is 0, validation was successful
     * @since 1.1.0
     */
    public List<SAXParseException> validate(File file) {
        return validate(file, CycloneDxSchema.Version.VERSION_11);
    }

    /**
     * Verifies a CycloneDX BoM conforms to the specification through XML validation.
     * @param file the CycloneDX BoM file to validate
     * @param schemaVersion the schema version to validate against
     * @return a List of SAXParseExceptions. If the size of the list is 0, validation was successful
     * @since 2.0.0
     */
    public List<SAXParseException> validate(File file, CycloneDxSchema.Version schemaVersion) {
        final Source xmlFile = new StreamSource(file);
        final List<SAXParseException> exceptions = new LinkedList<>();
        try {
            final Schema schema = getXmlSchema(schemaVersion);
            final Validator validator = schema.newValidator();
            validator.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) {
                    exceptions.add(exception);
                }
                @Override
                public void fatalError(SAXParseException exception) {
                    exceptions.add(exception);
                }
                @Override
                public void error(SAXParseException exception) {
                    exceptions.add(exception);
                }
            });
            validator.validate(xmlFile);
        } catch (IOException | SAXException e) {
            // throw it away
        }
        return exceptions;
    }

    /**
     * Verifies a CycloneDX BoM conforms to the latest version of the specification
     * through XML validation.
     * @param file the CycloneDX BoM file to validate
     * @return true is the file is a valid BoM, false if not
     * @since 1.1.0
     */
    public boolean isValid(File file) {
        return validate(file).isEmpty();
    }

    /**
     * Verifies a CycloneDX BoM conforms to the specification through XML validation.
     * @param file the CycloneDX BoM file to validate
     * @param schemaVersion the schema version to validate against
     * @return true is the file is a valid BoM, false if not
     * @since 2.0.0
     */
    public boolean isValid(File file, CycloneDxSchema.Version schemaVersion) {
        return validate(file, schemaVersion).isEmpty();
    }
}
