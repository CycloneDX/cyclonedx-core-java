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
package org.cyclonedx.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.cyclonedx.util.serializer.CustomDateSerializer;

@JacksonXmlRootElement(localName = "annotations")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(
    {"bom-ref",
     "subjects",
     "annotator",
     "timestamp",
     "text",
     "signature"
    })
public class Annotation extends ExtensibleElement
{
    @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
    @JsonProperty("bom-ref")
    private String bomRef;

    private List<BomReference> subjects;

    private Annotator annotator;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date timestamp;

    private String text;

    private Signature signature;

    public String getBomRef() {
        return bomRef;
    }

    public void setBomRef(final String bomRef) {
        this.bomRef = bomRef;
    }

    @JacksonXmlElementWrapper(localName = "subjects")
    @JacksonXmlProperty(localName = "subject")
    public List<BomReference> getSubjects() {
        return subjects;
    }

    public void setSubjects(final List<BomReference> subjects) {
        this.subjects = subjects;
    }

    public Annotator getAnnotator() {
        return annotator;
    }

    public void setAnnotator(final Annotator annotator) {
        this.annotator = annotator;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(final Signature signature) {
        this.signature = signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Annotation)) return false;
        Annotation that = (Annotation) o;
        return Objects.equals(bomRef, that.bomRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bomRef);
    }
}
