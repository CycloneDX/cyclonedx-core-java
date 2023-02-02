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
package org.cyclonedx.generators.xml;

import javax.xml.parsers.ParserConfigurationException;

import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.w3c.dom.Document;

/**
 * BomGenerator creates a CycloneDX bill-of-material document from a set of {@link Component}s. Proper usage assumes
 * {@link #generate()} is called after construction and optionally followed by {@link #toXmlString()}.
 *
 * @since 8.0.0
 */
public class BomXmlGenerator15
    extends AbstractBomXmlGenerator
    implements BomXmlGenerator
{
  private final Bom bom;

  /**
   * Constructs a new BomGenerator object.
   *
   * @param bom the BOM to generate
   */
  public BomXmlGenerator15(final Bom bom) {
    bom.setXmlns(CycloneDxSchema.NS_BOM_15);

    this.bom = bom;
  }

  /**
   * Returns the version of the CycloneDX schema used by this instance
   *
   * @return a CycloneDxSchemaVersion enum
   */
  public Version getSchemaVersion() {
    return Version.VERSION_15;
  }

  /**
   * Creates a CycloneDX BoM from a set of Components.
   *
   * @return an XML Document representing a CycloneDX BoM
   */
  public Document generate() throws ParserConfigurationException {
    return generateDocument(this.bom);
  }

  public String toXmlString() throws GeneratorException {
    return toXML(this.bom, true);
  }
}
