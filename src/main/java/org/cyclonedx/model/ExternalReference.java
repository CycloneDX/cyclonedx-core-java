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
package org.cyclonedx.model;

import org.cyclonedx.CycloneDxSchema;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@SuppressWarnings("unused")
public class ExternalReference {

    private String url;
    private String type;
    private String comment;

    public String getUrl() {
        return url;
    }

    @XmlElement(name = "url", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    @XmlElement(name = "comment", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setComment(String comment) {
        this.comment = comment;
    }
}
