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
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.cyclonedx;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

/**
 * NamespaceStreamReaderDelegate makes it possible to use a single object
 * model with multiple namespaces as long as the namespaces are backward
 * compatible. The model package contains POJOs that will have the latest
 * namespace defined for each element. NamespaceStreamReaderDelegate will
 * dynamically modify (if needed) the namespace of the bom it's reading
 * and ensure each attribute and element is using the latest schema.
 *
 * @since 2.0.0
 */
public class NamespaceStreamReaderDelegate extends StreamReaderDelegate {

    public NamespaceStreamReaderDelegate(XMLStreamReader xsr) {
        super(xsr);
    }

    @Override
    public String getAttributeNamespace(int index) {
        String ns = super.getAttributeNamespace(index);
        if (CycloneDxSchema.NS_BOM_10.equals(ns)) {
            return CycloneDxSchema.NS_BOM_LATEST;
        }
        return ns;
    }

    public String getNamespacePrefix(int index) {
        String ns = super.getNamespacePrefix(index);
        if (CycloneDxSchema.NS_BOM_10.equals(ns)) {
            return CycloneDxSchema.NS_BOM_LATEST;
        }
        return ns;
    }

    @Override
    public String getNamespaceURI(int index) {
        String ns = super.getNamespaceURI(index);
        if (CycloneDxSchema.NS_BOM_10.equals(ns)) {
            return CycloneDxSchema.NS_BOM_LATEST;
        }
        return ns;
    }

    @Override
    public String getNamespaceURI() {
        String ns = super.getNamespaceURI();
        if (CycloneDxSchema.NS_BOM_10.equals(ns)) {
            return CycloneDxSchema.NS_BOM_LATEST;
        }
        return ns;
    }
}
