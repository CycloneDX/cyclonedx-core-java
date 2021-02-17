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
package org.cyclonedx.util;

import java.util.Arrays;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlAnnotationIntrospector;
import org.cyclonedx.model.JsonOnly;
import org.cyclonedx.model.VersionFilter;

public class VersionAnnotationIntrospector extends JacksonXmlAnnotationIntrospector
{
  private final String version;

  public VersionAnnotationIntrospector(final String version) {
    this.version = version;
  }

  @Override
  public boolean hasIgnoreMarker(final AnnotatedMember m) {
    if (m.hasAnnotation(VersionFilter.class)) {
      VersionFilter filter = m.getAnnotation(VersionFilter.class);
      if (Arrays.stream(filter.versions()).noneMatch(v -> v.equals(version))) {
        return true;
      }
    }
    if (m.hasAnnotation(JsonOnly.class)) {
      return true;
    }
    return super.hasIgnoreMarker(m);
  }
}
