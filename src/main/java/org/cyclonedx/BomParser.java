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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.converters.enums.EnumToStringConverter;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.cyclonedx.converters.HashConverter;
import org.cyclonedx.converters.AttachmentTextConverter;
import org.cyclonedx.exception.ParseException;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Commit;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Hash;
import org.cyclonedx.model.License;
import org.cyclonedx.model.LicenseChoice;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.OrganizationalContact;
import org.cyclonedx.model.OrganizationalEntity;
import org.cyclonedx.model.Pedigree;
import org.cyclonedx.model.Swid;
import org.cyclonedx.model.Tool;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * BomParser is responsible for validating and parsing CycloneDX bill-of-material
 * documents and returning a high-level {@link Bom} object with all its components.
 * @since 1.1.0
 */
@SuppressWarnings("unused")
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
        try {
            final String schemaVersion = identifySchemaVersion(
                    extractAllNamespaceDeclarations(new InputSource(new FileInputStream(file))));
            final XStream xstream = mapDefaultObjectModel(createXStream());
            final Bom bom = (Bom) xstream.fromXML(file);
            return injectSchemaVersion(bom, schemaVersion);
        } catch (XStreamException | XPathExpressionException | FileNotFoundException e) {
            throw new ParseException(e);
        }
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
        try {
            final String schemaVersion = identifySchemaVersion(
                    extractAllNamespaceDeclarations(new InputSource(new ByteArrayInputStream(bomBytes))));
            final XStream xstream = mapDefaultObjectModel(createXStream());
            final Bom bom = (Bom)xstream.fromXML(new ByteArrayInputStream(bomBytes));
            return injectSchemaVersion(bom, schemaVersion);
        } catch (XStreamException | XPathExpressionException e) {
            throw new ParseException(e);
        }
    }

    /**
     * Parses a CycloneDX BOM.
     *
     * @param url to the BOM
     * @return an Bom object
     * @throws ParseException when errors are encountered
     * @since 2.0.0
     */
    public Bom parse(URL url) throws ParseException {
        try {
            final String schemaVersion = identifySchemaVersion(
                    extractAllNamespaceDeclarations(new InputSource(url.openStream())));
            final XStream xstream = mapDefaultObjectModel(createXStream());
            final Bom bom = (Bom) xstream.fromXML(url);
            return injectSchemaVersion(bom, schemaVersion);
        } catch (XStreamException | XPathExpressionException | IOException e) {
            throw new ParseException(e);
        }
    }

    /**
     * Parses a CycloneDX BOM.
     *
     * @param reader a Reader object
     * @return an Bom object
     * @throws ParseException when errors are encountered
     * @since 2.0.0
     */
    public Bom parse(Reader reader) throws ParseException {
        try {
            XStream xstream = mapDefaultObjectModel(createXStream());
            return (Bom) xstream.fromXML(reader);
        } catch (XStreamException e) {
            throw new ParseException(e);
        }
    }

    /**
     * Uses reflection to set the schemaVersion field inside a Bom instance.
     * The schemaVersion is 'not user serviceable' so no methods for setting
     * it are provided, other than the constructor, which xstream does not
     * use.
     * @param bom the Bom to set the schemaVersion for
     * @param schemaVersion the value of the schema version
     * @return the updated Bom
     * @since 2.5.0
     */
    private Bom injectSchemaVersion(Bom bom, String schemaVersion) {
        try {
            final Field field = Bom.class.getDeclaredField("schemaVersion");
            field.setAccessible(true);
            field.set(bom, schemaVersion);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // throw it away
        }
        return bom;
    }

    /**
     * Verifies a CycloneDX BoM conforms to the latest version of the specification
     * through XML validation.
     * @param file the CycloneDX BoM file to validate
     * @return a List of SAXParseExceptions. If the size of the list is 0, validation was successful
     * @since 1.1.0
     */
    public List<SAXParseException> validate(File file) throws IOException, SAXException {
        return validate(file, CycloneDxSchema.VERSION_LATEST);
    }

    /**
     * Verifies a CycloneDX BoM conforms to the specification through XML validation.
     * @param file the CycloneDX BoM file to validate
     * @param schemaVersion the schema version to validate against
     * @return a List of SAXParseExceptions. If the size of the list is 0, validation was successful
     * @since 2.0.0
     */
    public List<SAXParseException> validate(File file, CycloneDxSchema.Version schemaVersion) throws IOException, SAXException {
        final Source xmlFile = new StreamSource(file);
        final List<SAXParseException> exceptions = new LinkedList<>();
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
        return exceptions;
    }

    /**
     * Verifies a CycloneDX BoM conforms to the latest version of the specification
     * through XML validation.
     * @param file the CycloneDX BoM file to validate
     * @return true is the file is a valid BoM, false if not
     * @since 1.1.0
     */
    public boolean isValid(File file) throws IOException, SAXException {
        return validate(file).isEmpty();
    }

    /**
     * Verifies a CycloneDX BoM conforms to the specification through XML validation.
     * @param file the CycloneDX BoM file to validate
     * @param schemaVersion the schema version to validate against
     * @return true is the file is a valid BoM, false if not
     * @since 2.0.0
     */
    public boolean isValid(File file, CycloneDxSchema.Version schemaVersion) throws IOException, SAXException {
        return validate(file, schemaVersion).isEmpty();
    }

    private String identifySchemaVersion(final NodeList nodeList) {
        for (int i=0; i<nodeList.getLength(); i++) {
            final Node node = nodeList.item(i);
            for (final Version version: Version.values()) {
                if (version.getNamespace().equals(node.getNodeValue()))  {
                    return version.getVersionString();
                }
            }
        }
        return null;
    }

    private NodeList extractAllNamespaceDeclarations(InputSource in) throws XPathExpressionException {
        final XPathFactory xPathFactory = XPathFactory.newInstance();
        final XPath xPath = xPathFactory.newXPath();
        final XPathExpression xPathExpression = xPath.compile("//namespace::*");
        final NodeList nodeList = (NodeList) xPathExpression.evaluate(in, XPathConstants.NODESET);
        return nodeList;
    }

    private Map<String, Component.Type> getComponentTypeMapping() {
        Map<String, Component.Type> map = new HashMap<>();
        map.put(Component.Type.APPLICATION.getTypeName(), Component.Type.APPLICATION);
        map.put(Component.Type.FRAMEWORK.getTypeName(), Component.Type.FRAMEWORK);
        map.put(Component.Type.LIBRARY.getTypeName(), Component.Type.LIBRARY);
        map.put(Component.Type.CONTAINER.getTypeName(), Component.Type.CONTAINER);
        map.put(Component.Type.OPERATING_SYSTEM.getTypeName(), Component.Type.OPERATING_SYSTEM);
        map.put(Component.Type.FIRMWARE.getTypeName(), Component.Type.FIRMWARE);
        map.put(Component.Type.DEVICE.getTypeName(), Component.Type.DEVICE);
        map.put(Component.Type.FILE.getTypeName(), Component.Type.FILE);
        return map;
    }

    private Map<String, Component.Scope> getComponentScopeMapping() {
        Map<String, Component.Scope> map = new HashMap<>();
        map.put(Component.Scope.REQUIRED.getScopeName(), Component.Scope.REQUIRED);
        map.put(Component.Scope.EXCLUDED.getScopeName(), Component.Scope.EXCLUDED);
        map.put(Component.Scope.OPTIONAL.getScopeName(), Component.Scope.OPTIONAL);
        return map;
    }

    private Map<String, ExternalReference.Type> getExternalReferenceTypeMapping() {
        Map<String, ExternalReference.Type> map = new HashMap<>();
        map.put(ExternalReference.Type.VCS.getTypeName(), ExternalReference.Type.VCS);
        map.put(ExternalReference.Type.ISSUE_TRACKER.getTypeName(), ExternalReference.Type.ISSUE_TRACKER);
        map.put(ExternalReference.Type.WEBSITE.getTypeName(), ExternalReference.Type.WEBSITE);
        map.put(ExternalReference.Type.ADVISORIES.getTypeName(), ExternalReference.Type.ADVISORIES);
        map.put(ExternalReference.Type.BOM.getTypeName(), ExternalReference.Type.BOM);
        map.put(ExternalReference.Type.MAILING_LIST.getTypeName(), ExternalReference.Type.MAILING_LIST);
        map.put(ExternalReference.Type.SOCIAL.getTypeName(), ExternalReference.Type.SOCIAL);
        map.put(ExternalReference.Type.CHAT.getTypeName(), ExternalReference.Type.CHAT);
        map.put(ExternalReference.Type.DOCUMENTATION.getTypeName(), ExternalReference.Type.DOCUMENTATION);
        map.put(ExternalReference.Type.SUPPORT.getTypeName(), ExternalReference.Type.SUPPORT);
        map.put(ExternalReference.Type.DISTRIBUTION.getTypeName(), ExternalReference.Type.DISTRIBUTION);
        map.put(ExternalReference.Type.LICENSE.getTypeName(), ExternalReference.Type.LICENSE);
        map.put(ExternalReference.Type.BUILD_META.getTypeName(), ExternalReference.Type.BUILD_META);
        map.put(ExternalReference.Type.BUILD_SYSTEM.getTypeName(), ExternalReference.Type.BUILD_SYSTEM);
        map.put(ExternalReference.Type.OTHER.getTypeName(), ExternalReference.Type.OTHER);
        return map;
    }

    private XStream createXStream() {
        QName qname = new QName(CycloneDxSchema.NS_BOM_LATEST);
        QNameMap nsm = new QNameMap();
        nsm.registerMapping(qname, Bom.class);
        nsm.setDefaultNamespace(CycloneDxSchema.NS_BOM_LATEST);
        XStream xstream = new XStream(new StaxDriver(nsm)) {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        try {
                            return definedIn != Object.class || realClass(fieldName) != null;
                        } catch(CannotResolveClassException e) {
                            return false;
                        }
                    }
                    @Override
                    public Class realClass(String elementName) {
                        try {
                            return super.realClass(elementName);
                        } catch(CannotResolveClassException e) {
                            return null;
                        }
                    }
                };
            }
        };
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypesByWildcard(new String[] {
                "org.cyclonedx.model.**", "org.cyclonedx.model.ext.dependencyGraph.**"
        });
        return xstream;
    }

    private XStream mapDefaultObjectModel(XStream xstream) {
        xstream.alias("bom", Bom.class);
        xstream.aliasAttribute(Bom.class, "version", "version");
        xstream.aliasAttribute(Bom.class, "serialNumber", "serialNumber");

        xstream.alias("component", Component.class);
        xstream.aliasAttribute(Component.class, "type", "type");
        xstream.aliasAttribute(Component.class, "bomRef", "bom-ref");
        xstream.aliasAttribute(Component.class, "scope", "scope");
        xstream.registerConverter(new EnumToStringConverter<>(Component.Type.class, getComponentTypeMapping()));
        xstream.registerConverter(new EnumToStringConverter<>(Component.Scope.class, getComponentScopeMapping()));

        xstream.alias("hash", Hash.class);
        xstream.registerConverter(new HashConverter());

        xstream.alias("swid", Swid.class);
        xstream.aliasAttribute(Swid.class, "tagId", "tagId");
        xstream.aliasAttribute(Swid.class, "name", "name");
        xstream.aliasAttribute(Swid.class, "version", "version");
        xstream.aliasAttribute(Swid.class, "tagVersion", "tagVersion");
        xstream.aliasAttribute(Swid.class, "patch", "patch");
        xstream.registerConverter(new AttachmentTextConverter());
        xstream.aliasField("text", Swid.class, "attachmentText");

        xstream.alias("commit", Commit.class);

        xstream.alias("reference", ExternalReference.class);
        xstream.aliasAttribute(ExternalReference.class, "type", "type");
        xstream.registerConverter(new EnumToStringConverter<>(ExternalReference.Type.class, getExternalReferenceTypeMapping()));

        xstream.aliasField("licenses", Component.class, "licenseChoice");
        xstream.alias("license", License.class);
        xstream.addImplicitCollection(LicenseChoice.class, "licenses");
        xstream.registerConverter(new AttachmentTextConverter());
        xstream.aliasField("text", License.class, "attachmentText");

        xstream.alias("pedigree", Pedigree.class);

        xstream.addImplicitCollection(Dependency.class, "dependencies");
        xstream.alias("dependency", Dependency.class);
        xstream.aliasAttribute(Dependency.class, "ref", "ref");

        xstream.alias("metadata", Metadata.class);
        xstream.alias("tool", Tool.class);
        xstream.alias("author", OrganizationalContact.class);
        xstream.alias("manufacture", OrganizationalEntity.class);
        xstream.alias("supplier", OrganizationalEntity.class);

        return xstream;
    }
}
