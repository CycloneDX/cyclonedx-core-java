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
package org.cyclonedx.util.introspector;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.cyclonedx.Version;
import org.cyclonedx.model.VersionFilter;
import org.cyclonedx.model.XmlOnly;

public class VersionJsonAnnotationIntrospector extends JacksonAnnotationIntrospector
{
    private final Version version;

    public VersionJsonAnnotationIntrospector(final Version version) {
        this.version = version;
    }

    @Override
    public boolean hasIgnoreMarker(final AnnotatedMember m) {
        // Check if the field has the VersionFilter annotation
        if (m.hasAnnotation(VersionFilter.class)) {
            // Get the VersionFilter annotation from the field
            VersionFilter filter = m.getAnnotation(VersionFilter.class);
            // Check if the version specified in the annotation is greater than the current version
            if (filter.value().getVersion() > version.getVersion()) {
                // If true, it means the field was introduced after the current version, so we should ignore it
                return true;
            }
        }

        // Check if the field has the XmlOnly annotation
        if (m.hasAnnotation(XmlOnly.class)) {
            // If true, the field should be ignored for XML serialization
            return true;
        }

        // If none of the above conditions are met, delegate to the superclass's hasIgnoreMarker method
        return super.hasIgnoreMarker(m);
    }
}
