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
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@SuppressWarnings("unused")
public class ExternalReference {

    @XmlEnum
    public enum Type {
        @XmlEnumValue("vcs")
        VCS("vcs"),

        @XmlEnumValue("issue-tracker")
        ISSUE_TRACKER("issue-tracker"),

        @XmlEnumValue("website")
        WEBSITE("website"),

        @XmlEnumValue("advisories")
        ADVISORIES("advisories"),

        @XmlEnumValue("bom")
        BOM("bom"),

        @XmlEnumValue("mailing-list")
        MAILING_LIST("mailing-list"),

        @XmlEnumValue("social")
        SOCIAL("social"),

        @XmlEnumValue("chat")
        CHAT("chat"),

        @XmlEnumValue("documentation")
        DOCUMENTATION("documentation"),

        @XmlEnumValue("support")
        SUPPORT("support"),

        @XmlEnumValue("distribution")
        DISTRIBUTION("distribution"),

        @XmlEnumValue("license")
        LICENSE("license"),

        @XmlEnumValue("build-meta")
        BUILD_META("build-meta"),

        @XmlEnumValue("build-system")
        BUILD_SYSTEM("build-system"),

        @XmlEnumValue("other")
        OTHER("other");

        private String name;

        public String getTypeName() {
            return this.name;
        }

        Type(String name) {
            this.name = name;
        }
    }

    private String url;
    private Type type;
    private String comment;

    public String getUrl() {
        return url;
    }

    @XmlElement(name = "url", namespace = CycloneDxSchema.NS_BOM_LATEST)
    public void setUrl(String url) {
        this.url = url;
    }

    public Type getType() {
        return type;
    }

    @XmlAttribute(name = "type")
    public void setType(Type type) {
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
