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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.util.serializer.CustomDateSerializer;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A citation indicates which entity supplied information for specific fields within the BOM.
 *
 * @since 10.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"bomRef", "pointers", "expressions", "timestamp", "attributedTo", "process", "note"})
public class Citation extends ExtensibleElement {

    @JacksonXmlProperty(isAttribute = true, localName = "bom-ref")
    @JsonProperty("bom-ref")
    private String bomRef;

    @JacksonXmlElementWrapper(localName = "pointers")
    @JacksonXmlProperty(localName = "pointer")
    private List<String> pointers;

    @JacksonXmlElementWrapper(localName = "expressions")
    @JacksonXmlProperty(localName = "expression")
    private List<String> expressions;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date timestamp;

    private String attributedTo;

    private String process;

    private String note;

    public String getBomRef() {
        return bomRef;
    }

    public void setBomRef(String bomRef) {
        this.bomRef = bomRef;
    }

    public List<String> getPointers() {
        return pointers;
    }

    public void setPointers(List<String> pointers) {
        this.pointers = pointers;
    }

    public List<String> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<String> expressions) {
        this.expressions = expressions;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAttributedTo() {
        return attributedTo;
    }

    public void setAttributedTo(String attributedTo) {
        this.attributedTo = attributedTo;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Citation)) return false;
        Citation citation = (Citation) o;
        return Objects.equals(bomRef, citation.bomRef) &&
                Objects.equals(pointers, citation.pointers) &&
                Objects.equals(expressions, citation.expressions) &&
                Objects.equals(timestamp, citation.timestamp) &&
                Objects.equals(attributedTo, citation.attributedTo) &&
                Objects.equals(process, citation.process) &&
                Objects.equals(note, citation.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bomRef, pointers, expressions, timestamp, attributedTo, process, note);
    }
}
