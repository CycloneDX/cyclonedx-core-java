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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersionDetector;
import org.cyclonedx.generators.json.BomJsonGenerator;
import org.cyclonedx.generators.xml.BomXmlGenerator;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CycloneDxSchema is a base class that provides schema information to {@link BomXmlGenerator},
 * {@link BomJsonGenerator}, and {@link org.cyclonedx.parsers.Parser}. The class can be extended for other
 * implementations as well.
 *
 * @since 1.1.0
 */
public abstract class CycloneDxSchema
{
  public static final String NS_BOM_10 = "http://cyclonedx.org/schema/bom/1.0";

  public static final String NS_BOM_11 = "http://cyclonedx.org/schema/bom/1.1";

  public static final String NS_BOM_12 = "http://cyclonedx.org/schema/bom/1.2";

  public static final String NS_BOM_13 = "http://cyclonedx.org/schema/bom/1.3";

  public static final String NS_BOM_14 = "http://cyclonedx.org/schema/bom/1.4";

  public static final String NS_BOM_15 = "http://cyclonedx.org/schema/bom/1.5";

  public static final String NS_DEPENDENCY_GRAPH_10 = "http://cyclonedx.org/schema/ext/dependency-graph/1.0";

  public static final String NS_BOM_LATEST = NS_BOM_15;

  public static final Version VERSION_LATEST = Version.VERSION_15;

  public static final List<Version> ALL_VERSIONS = Arrays.asList(Version.values());

  public enum Version
  {
    VERSION_10(CycloneDxSchema.NS_BOM_10, "1.0", 1.0),
    VERSION_11(CycloneDxSchema.NS_BOM_11, "1.1", 1.1),
    VERSION_12(CycloneDxSchema.NS_BOM_12, "1.2", 1.2),
    VERSION_13(CycloneDxSchema.NS_BOM_13, "1.3", 1.3),
    VERSION_14(CycloneDxSchema.NS_BOM_14, "1.4", 1.4),
    VERSION_15(CycloneDxSchema.NS_BOM_15, "1.5", 1.5);

    private final String namespace;

    private final String versionString;

    private final double version;

    public String getNamespace() {
      return this.namespace;
    }

    public String getVersionString() {
      return versionString;
    }

    public double getVersion() {
      return version;
    }

    Version(String namespace, String versionString, double version) {
      this.namespace = namespace;
      this.versionString = versionString;
      this.version = version;
    }
  }

  /**
   * Returns the CycloneDX JsonSchema for the specified schema version.
   *
   * @param schemaVersion The version to return the schema for
   * @param mapper        is to provide a Jackson ObjectMapper
   * @return a Schema
   * @throws IOException when errors are encountered
   * @since 6.0.0
   */
  public JsonSchema getJsonSchema(CycloneDxSchema.Version schemaVersion, final ObjectMapper mapper)
      throws IOException
  {
    final InputStream spdxInstream = getJsonSchemaAsStream(schemaVersion);
    final SchemaValidatorsConfig config = new SchemaValidatorsConfig();
    final Map<String, String> offlineMappings = new HashMap<>();
    offlineMappings.put("http://cyclonedx.org/schema/spdx.schema.json",
        getClass().getClassLoader().getResource("spdx.schema.json").toExternalForm());
    offlineMappings.put("http://cyclonedx.org/schema/bom-1.2.schema.json",
        getClass().getClassLoader().getResource("bom-1.2-strict.schema.json").toExternalForm());
    offlineMappings.put("http://cyclonedx.org/schema/bom-1.3.schema.json",
        getClass().getClassLoader().getResource("bom-1.3-strict.schema.json").toExternalForm());
    offlineMappings.put("http://cyclonedx.org/schema/bom-1.4.schema.json",
        getClass().getClassLoader().getResource("bom-1.4.schema.json").toExternalForm());
    offlineMappings.put("http://cyclonedx.org/schema/bom-1.5.schema.json",
        getClass().getClassLoader().getResource("bom-1.5.schema.json").toExternalForm());
    config.setUriMappings(offlineMappings);
    JsonNode schemaNode = mapper.readTree(spdxInstream);
    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersionDetector.detect(schemaNode));
    return factory.getSchema(schemaNode, config);
  }

  private InputStream getJsonSchemaAsStream(final CycloneDxSchema.Version schemaVersion) {
    if (CycloneDxSchema.Version.VERSION_12 == schemaVersion) {
      return this.getClass().getClassLoader().getResourceAsStream("bom-1.2-strict.schema.json");
    }
    else if (CycloneDxSchema.Version.VERSION_13 == schemaVersion) {
      return this.getClass().getClassLoader().getResourceAsStream("bom-1.3-strict.schema.json");
    }
    else if (CycloneDxSchema.Version.VERSION_14 == schemaVersion) {
      return this.getClass().getClassLoader().getResourceAsStream("bom-1.4.schema.json");
    }
    else {
      return this.getClass().getClassLoader().getResourceAsStream("bom-1.5.schema.json");
    }
  }

  /**
   * Returns the CycloneDX XML Schema for the specified schema version.
   *
   * @param schemaVersion The version to return the schema for
   * @return a Schema
   * @throws SAXException a SAXException
   * @since 2.0.0
   */
  public Schema getXmlSchema(CycloneDxSchema.Version schemaVersion) throws SAXException {
    if (CycloneDxSchema.Version.VERSION_10 == schemaVersion) {
      return getXmlSchema10();
    }
    else if (CycloneDxSchema.Version.VERSION_11 == schemaVersion) {
      return getXmlSchema11();
    }
    else if (CycloneDxSchema.Version.VERSION_12 == schemaVersion) {
      return getXmlSchema12();
    }
    else if (CycloneDxSchema.Version.VERSION_13 == schemaVersion) {
      return getXmlSchema13();
    }
    else if (CycloneDxSchema.Version.VERSION_14 == schemaVersion) {
      return getXmlSchema14();
    }
    else {
      return getXmlSchema15();
    }
  }

  /**
   * Returns the CycloneDX XML Schema from the specifications XSD.
   *
   * @return a Schema
   * @throws SAXException a SAXException
   * @since 1.1.0
   */
  private Schema getXmlSchema10() throws SAXException {
    // Use local copies of schemas rather than resolving from the net. It's faster, and less prone to errors.
    return getXmlSchema(
        this.getClass().getClassLoader().getResourceAsStream("spdx.xsd"),
        this.getClass().getClassLoader().getResourceAsStream("bom-1.0.xsd")
    );
  }

  /**
   * Returns the CycloneDX XML Schema from the specifications XSD.
   *
   * @return a Schema
   * @throws SAXException a SAXException
   * @since 2.0.0
   */
  private Schema getXmlSchema11() throws SAXException {
    // Use local copies of schemas rather than resolving from the net. It's faster, and less prone to errors.
    return getXmlSchema(
        this.getClass().getClassLoader().getResourceAsStream("spdx.xsd"),
        this.getClass().getClassLoader().getResourceAsStream("bom-1.1.xsd")
    );
  }

  /**
   * Returns the CycloneDX XML Schema from the specifications XSD.
   *
   * @return a Schema
   * @throws SAXException a SAXException
   * @since 2.8.0
   */
  private Schema getXmlSchema12() throws SAXException {
    // Use local copies of schemas rather than resolving from the net. It's faster, and less prone to errors.
    return getXmlSchema(
        this.getClass().getClassLoader().getResourceAsStream("spdx.xsd"),
        this.getClass().getClassLoader().getResourceAsStream("bom-1.2.xsd")
    );
  }

  /**
   * Returns the CycloneDX XML Schema from the specifications XSD.
   *
   * @return a Schema
   * @throws SAXException a SAXException
   * @since 5.0.0
   */
  private Schema getXmlSchema13() throws SAXException {
    // Use local copies of schemas rather than resolving from the net. It's faster, and less prone to errors.
    return getXmlSchema(
        this.getClass().getClassLoader().getResourceAsStream("spdx.xsd"),
        this.getClass().getClassLoader().getResourceAsStream("bom-1.3.xsd")
    );
  }

  /**
   * Returns the CycloneDX XML Schema from the specifications XSD.
   *
   * @return a Schema
   * @throws SAXException a SAXException
   * @since 5.1.0
   */
  private Schema getXmlSchema14() throws SAXException {
    // Use local copies of schemas rather than resolving from the net. It's faster, and less prone to errors.
    return getXmlSchema(
        this.getClass().getClassLoader().getResourceAsStream("spdx.xsd"),
        this.getClass().getClassLoader().getResourceAsStream("bom-1.4.xsd")
    );
  }

  /**
   * Returns the CycloneDX XML Schema from the specifications XSD.
   *
   * @return a Schema
   * @throws SAXException a SAXException
   * @since TBD
   */
  private Schema getXmlSchema15() throws SAXException {
    // Use local copies of schemas rather than resolving from the net. It's faster, and less prone to errors.
    return getXmlSchema(
        this.getClass().getClassLoader().getResourceAsStream("spdx.xsd"),
        this.getClass().getClassLoader().getResourceAsStream("bom-1.5.xsd")
    );
  }

  public Schema getXmlSchema(InputStream... inputStreams) throws SAXException {
    final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    final Source[] schemaFiles = new Source[inputStreams.length];
    for (int i = 0; i < inputStreams.length; i++) {
      schemaFiles[i] = new StreamSource(inputStreams[i]);
    }
    return schemaFactory.newSchema(schemaFiles);
  }
}
