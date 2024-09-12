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
package org.cyclonedx.generators.json;

import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.cyclonedx.Format;
import org.cyclonedx.Version;
import org.cyclonedx.exception.GeneratorException;
import org.cyclonedx.generators.AbstractBomGenerator;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.BomReference;
import org.cyclonedx.util.introspector.VersionJsonAnnotationIntrospector;
import org.cyclonedx.util.mixin.MixInBomReference;
import org.cyclonedx.util.serializer.ComponentWrapperSerializer;
import org.cyclonedx.util.serializer.DependencySerializer;
import org.cyclonedx.util.serializer.TrimStringSerializer;

public class BomJsonGenerator extends AbstractBomGenerator
{
  private final DefaultPrettyPrinter prettyPrinter;

  /**
   * Constructs a new BomGenerator object.
   * @param bom the BOM to generate
   * @param version the version of the CycloneDX schema to use.
   */
  public BomJsonGenerator(Bom bom, final Version version) {
    super(version, bom, Format.JSON);
    Bom modifiedBom = null;
    try {
      modifiedBom = injectBomFormatAndSpecVersion(bom);
    }
    catch (GeneratorException e) {
    }
    this.bom = modifiedBom != null ? modifiedBom : bom;
    this.prettyPrinter = new DefaultPrettyPrinter();

    setupPrettyPrinter(this.prettyPrinter);

    this.mapper = new ObjectMapper();
    setupObjectMapper();
  }

  private void setupObjectMapper() {
    mapper.setAnnotationIntrospector(new VersionJsonAnnotationIntrospector(version));

    super.setupObjectMapper(false);

    SimpleModule depModule = new SimpleModule();
    SimpleModule componentWrapperModule = new SimpleModule();

    SimpleModule stringModule = new SimpleModule();
    stringModule.addSerializer(new TrimStringSerializer());
    mapper.registerModule(stringModule);

    depModule.addSerializer(new DependencySerializer(false, null));
    mapper.registerModule(depModule);

    componentWrapperModule.addSerializer(new ComponentWrapperSerializer(mapper));
    mapper.registerModule(componentWrapperModule);
  }

  private void setupPrettyPrinter(final DefaultPrettyPrinter prettyPrinter) {
    prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
  }

  private  Bom injectBomFormatAndSpecVersion(Bom bom) throws GeneratorException {
    try {
      Field field;
      field = Bom.class.getDeclaredField("bomFormat");
      field.setAccessible(true);
      field.set(bom, "CycloneDX");
      field = Bom.class.getDeclaredField("specVersion");
      field.setAccessible(true);
      field.set(bom, getSchemaVersion().getVersionString());
      return bom;
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new GeneratorException(e);
    }
  }

  /**
   * Creates a CycloneDX BOM from a set of Components.
   * @return an JSON Document representing a CycloneDX BoM
   * @since 7.0.0
   */
  public JsonNode toJsonNode() {
    try {
      return mapper.readTree(toJson(bom, false));
    } catch (GeneratorException | JsonProcessingException e) {
      return null;
    }
  }

  public String toJsonString() throws GeneratorException {
      return toJson(bom, true);
  }

  /**
   * Creates a text representation of a CycloneDX BoM Document. This method calls {@link #toJsonString()} and will return
   * an empty string if {@link #toJsonString()} throws an exception. It's preferred to call {@link #toJsonString()}
   * directly so that exceptions can be caught.
   *
   * @return a String of the BoM
   */
  @Override
  public String toString() {
    try {
      return toJson(bom, true);
    } catch (GeneratorException e) {
      return "";
    }
  }

  String toJson(final Bom bom, final boolean prettyPrint) throws GeneratorException {
    try {
      mapper.addMixIn(BomReference.class, MixInBomReference.class);
      if (prettyPrint) {
        return mapper.writer(prettyPrinter).writeValueAsString(bom);
      }
      return mapper.writeValueAsString(bom);
    }
    catch (JsonProcessingException e) {
      throw new GeneratorException(e);
    }
  }
}
