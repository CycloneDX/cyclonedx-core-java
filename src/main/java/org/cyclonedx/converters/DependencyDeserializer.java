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

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.cyclonedx.model.Dependency;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DependencyDeserializer implements ObjectDeserializer {

    @SuppressWarnings("unchecked")
    public <T> T deserialze(final DefaultJSONParser parser, final Type type, final Object fieldName) {
        final List<String> parseObjectList = parser.parseArray(String.class);
        if(parseObjectList != null) {
            final List<Dependency> dependencies = new ArrayList<>();
            for(String ref: parseObjectList) {
                dependencies.add(new Dependency(ref));
            }
            return (T)dependencies;
        }
        throw new IllegalStateException();
    }

    public int getFastMatchToken() {
        return 0;
    }
}
