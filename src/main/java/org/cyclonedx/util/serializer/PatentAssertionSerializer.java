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
package org.cyclonedx.util.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.cyclonedx.Version;
import org.cyclonedx.model.PatentAssertion;

import javax.xml.namespace.QName;

import static org.cyclonedx.util.serializer.SerializerUtils.shouldSerializeField;

/**
 * Custom serializer for PatentAssertion that handles the bom-ref attribute/element name
 * conflict in XML. In XML, "bom-ref" appears as both an attribute on &lt;patentAssertion&gt;
 * and as child elements inside &lt;patentRefs&gt;. Jackson XML cannot distinguish these,
 * so this serializer handles the XML output manually.
 */
public class PatentAssertionSerializer extends JsonSerializer<PatentAssertion> {

    private final Version version;

    private final OrganizationalChoiceSerializer choiceSerializer;

    public PatentAssertionSerializer() {
        this(null);
    }

    public PatentAssertionSerializer(Version version) {
        this.version = version;
        this.choiceSerializer = new OrganizationalChoiceSerializer(version);
    }

    @Override
    public void serialize(PatentAssertion value, JsonGenerator gen, SerializerProvider provider)
        throws IOException
    {
        if (gen instanceof ToXmlGenerator) {
            serializeXml(value, (ToXmlGenerator) gen, provider);
        } else {
            serializeJson(value, gen, provider);
        }
    }

    private void serializeJson(PatentAssertion value, JsonGenerator gen, SerializerProvider provider)
        throws IOException
    {
        gen.writeStartObject();
        if (value.getBomRef() != null && shouldSerializeField(value, version, "bomRef")) {
            gen.writeStringField("bom-ref", value.getBomRef());
        }
        if (value.getAssertionType() != null && shouldSerializeField(value, version, "assertionType")) {
            gen.writeStringField("assertionType", value.getAssertionType().getValue());
        }
        if (value.getPatentRefs() != null && !value.getPatentRefs().isEmpty() && shouldSerializeField(value, version, "patentRefs")) {
            gen.writeArrayFieldStart("patentRefs");
            for (String ref : value.getPatentRefs()) {
                gen.writeString(ref);
            }
            gen.writeEndArray();
        }
        if (value.getAsserter() != null && shouldSerializeField(value, version, "asserter")) {
            gen.writeFieldName("asserter");
            choiceSerializer.serialize(value.getAsserter(), gen, provider);
        }
        if (value.getNotes() != null && shouldSerializeField(value, version, "notes")) {
            gen.writeStringField("notes", value.getNotes());
        }
        gen.writeEndObject();
    }

    private void serializeXml(PatentAssertion value, ToXmlGenerator gen, SerializerProvider provider)
        throws IOException
    {
        gen.writeStartObject();

        // Write bom-ref as XML attribute
        if (value.getBomRef() != null && shouldSerializeField(value, version, "bomRef")) {
            gen.setNextIsAttribute(true);
            gen.setNextName(new QName("bom-ref"));
            gen.writeFieldName("bom-ref");
            gen.writeString(value.getBomRef());
            gen.setNextIsAttribute(false);
        }

        // assertionType
        if (value.getAssertionType() != null && shouldSerializeField(value, version, "assertionType")) {
            gen.writeStringField("assertionType", value.getAssertionType().getValue());
        }

        // patentRefs wrapper with bom-ref child elements
        if (value.getPatentRefs() != null && !value.getPatentRefs().isEmpty() && shouldSerializeField(value, version, "patentRefs")) {
            gen.writeFieldName("patentRefs");
            gen.writeStartObject();
            for (String ref : value.getPatentRefs()) {
                gen.writeStringField("bom-ref", ref);
            }
            gen.writeEndObject();
        }

        // asserter (delegates to OrganizationalChoiceSerializer)
        if (value.getAsserter() != null && shouldSerializeField(value, version, "asserter")) {
            gen.writeFieldName("asserter");
            choiceSerializer.serialize(value.getAsserter(), gen, provider);
        }

        // notes
        if (value.getNotes() != null && shouldSerializeField(value, version, "notes")) {
            gen.writeStringField("notes", value.getNotes());
        }

        gen.writeEndObject();
    }
}
