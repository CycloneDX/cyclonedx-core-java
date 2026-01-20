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
package org.cyclonedx.util.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.cyclonedx.Version;
import org.cyclonedx.model.Component;

import java.util.List;

/**
 * Bean serializer modifier for Component.authors field.
 * Applies the AuthorsSerializer only to the authors field in Component class.
 */
public class AuthorsBeanSerializerModifier extends BeanSerializerModifier {
    private final Version version;

    public AuthorsBeanSerializerModifier(Version version) {
        this.version = version;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(
            SerializationConfig config,
            BeanDescription beanDesc,
            List<BeanPropertyWriter> beanProperties) {

        // Only modify Component class
        if (Component.class.isAssignableFrom(beanDesc.getBeanClass())) {
            java.util.Iterator<BeanPropertyWriter> iterator = beanProperties.iterator();
            while (iterator.hasNext()) {
                BeanPropertyWriter writer = iterator.next();
                // Find the authors property
                if ("authors".equals(writer.getName())) {
                    // Check if the current version supports the authors field (v1.6+)
                    if (version.getVersion() < Version.VERSION_16.getVersion()) {
                        // Remove the property for versions earlier than 1.6
                        iterator.remove();
                    } else {
                        // Assign the custom serializer for v1.6+
                        JsonSerializer<?> serializer = new AuthorsSerializer();
                        writer.assignSerializer((JsonSerializer<Object>) serializer);
                    }
                    break;
                }
            }
        }
        return beanProperties;
    }
}
