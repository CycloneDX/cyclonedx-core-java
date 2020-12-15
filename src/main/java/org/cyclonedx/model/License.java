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
package org.cyclonedx.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@SuppressWarnings("unused")
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"id", "text", "url"})
@JsonRootName("license")
public class License extends ExtensibleElement {

    @JacksonXmlProperty(localName = "id")
    @JsonProperty("id")
    private String id;
    private String name;

    @JacksonXmlProperty(localName = "text")
    @JsonProperty("text")
    private AttachmentText attachmentText;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AttachmentText getAttachmentText() {
        return attachmentText;
    }

    public void setLicenseText(AttachmentText attachmentText) {
        this.attachmentText = attachmentText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        License license = (License) o;
        return Objects.equals(id, license.id) &&
                Objects.equals(name, license.name) &&
                Objects.equals(url, license.url) &&
                Objects.equals(attachmentText, license.attachmentText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, attachmentText);
    }
}
