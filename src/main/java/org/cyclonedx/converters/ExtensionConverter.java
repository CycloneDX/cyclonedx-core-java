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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.cyclonedx.model.ExtensibleType;
import org.cyclonedx.model.Extension;
import org.cyclonedx.model.Extension.ExtensionType;

public class ExtensionConverter
    implements Converter
{
    private final Class type;
    private final ExtensionType extensionType;

    public ExtensionConverter(final Class type,
                              final ExtensionType extensionType)
    {
        this.type = type;
        this.extensionType = extensionType;
    }

    @Override
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {

    }

    @Override
    public boolean canConvert(Class clazz) {
        return AbstractMap.class.isAssignableFrom(clazz);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        ArrayList<ExtensibleType> list = new ArrayList<>();
        if (reader.getNodeName().equals(extensionType.getTypeName())) {
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                list.add((ExtensibleType) context.convertAnother(reader, type));
                reader.moveUp();
            }

            if(!list.isEmpty()) {
                Map<String, Extension> map = new HashMap<>();
                Extension ext = new Extension(extensionType, list);
                map.put(extensionType.getTypeName(), ext);
                return map;
            }
        }
        return null;
    }
}
