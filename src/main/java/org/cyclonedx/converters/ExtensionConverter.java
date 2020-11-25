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
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.cyclonedx.model.ExtensibleExtension;
import org.cyclonedx.model.vulnerability.Vulnerability1_0;

public class ExtensionConverter
    implements Converter
{
    @Override
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {

    }

    @Override
    public boolean canConvert(Class clazz) {
        return AbstractMap.class.isAssignableFrom(clazz);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        ArrayList<ExtensibleExtension> list = new ArrayList<>();
        if(reader.getNodeName().equals("vulnerabilities")){
            Vulnerability1_0 vul=new Vulnerability1_0();
            String ref=reader.getAttribute(0);
            vul.setRef(ref);

            while(reader.hasMoreChildren()){
                reader.moveDown();
                reader.moveDown();
                vul.setId(reader.getValue());
                list.add(vul);
            }
            reader.moveUp();
        }
        Map<String, List<ExtensibleExtension>> map = new HashMap<>();
        map.put("vulnerabilities", list);
        return map;
    }
}
