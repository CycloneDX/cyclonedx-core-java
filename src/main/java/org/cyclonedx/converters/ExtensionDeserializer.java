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
package org.cyclonedx.converters;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.cyclonedx.model.ExtensibleExtension.ExtensionType;
import org.cyclonedx.model.vulnerability.Vulnerability1_0;

public class ExtensionDeserializer
    implements ObjectDeserializer {

    @Override
    public <T> T deserialze(final DefaultJSONParser parser, final Type type, final Object fieldName) {
        if (fieldName.equals(ExtensionType.VULNERABILITIES.getTypeName())) {
            final List<Vulnerability1_0> vulnerabilityList = parser.parseArray(Vulnerability1_0.class);
            if (vulnerabilityList != null) {
                final HashMap<String, List<Vulnerability1_0>> extensions = new HashMap<>();
                extensions.put((String) fieldName, vulnerabilityList);
                return (T) extensions;
            }
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
