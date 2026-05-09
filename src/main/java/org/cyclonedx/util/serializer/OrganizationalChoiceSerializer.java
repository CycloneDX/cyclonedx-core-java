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
import org.cyclonedx.model.OrganizationalChoice;
import org.cyclonedx.model.OrganizationalEntity;

import static org.cyclonedx.util.serializer.SerializerUtils.shouldSerializeField;

/**
 * Serializer for OrganizationalChoice in patent contexts where the JSON schema uses
 * unwrapped format (direct entity/contact/string ref) rather than the Licensing wrapped
 * format ({"organization": {...}} / {"individual": {...}}).
 *
 * <p>JSON output:
 * <ul>
 *   <li>Org with only bomRef: string "org-acme-inc"</li>
 *   <li>Org with other fields: direct entity object {"name": "...", "url": [...]}</li>
 *   <li>Individual: direct contact object {"name": "...", "email": "..."}</li>
 * </ul>
 *
 * <p>XML output (inside the parent element like &lt;asserter&gt; or &lt;patentAssignee&gt;):
 * <ul>
 *   <li>Org with only bomRef: &lt;ref&gt;bomRef&lt;/ref&gt;</li>
 *   <li>Org with other fields: &lt;organization&gt;...&lt;/organization&gt;</li>
 *   <li>Individual: &lt;individual&gt;...&lt;/individual&gt;</li>
 * </ul>
 */
public class OrganizationalChoiceSerializer extends JsonSerializer<OrganizationalChoice> {

    private final Version version;

    public OrganizationalChoiceSerializer() {
        this(null);
    }

    public OrganizationalChoiceSerializer(Version version) {
        this.version = version;
    }

    @Override
    public void serialize(OrganizationalChoice value, JsonGenerator gen, SerializerProvider provider)
        throws IOException
    {
        if (gen instanceof ToXmlGenerator) {
            serializeXml(value, (ToXmlGenerator) gen, provider);
        } else {
            serializeJson(value, gen, provider);
        }
    }

    private void serializeJson(OrganizationalChoice value, JsonGenerator gen, SerializerProvider provider)
        throws IOException
    {
        if (value.getOrganization() != null && shouldSerializeField(value, version, "organization")) {
            OrganizationalEntity org = value.getOrganization();
            if (isRefOnly(org)) {
                gen.writeString(org.getBomRef());
            } else {
                provider.defaultSerializeValue(org, gen);
            }
        } else if (value.getIndividual() != null && shouldSerializeField(value, version, "individual")) {
            provider.defaultSerializeValue(value.getIndividual(), gen);
        } else {
            gen.writeNull();
        }
    }

    private void serializeXml(OrganizationalChoice value, ToXmlGenerator gen, SerializerProvider provider)
        throws IOException
    {
        // In XML, the parent element (e.g. <asserter> or <patentAssignee>) wraps this serializer's output.
        // We write an object with a single child element: <ref>, <organization>, or <individual>.
        gen.writeStartObject();
        if (value.getOrganization() != null && shouldSerializeField(value, version, "organization")) {
            OrganizationalEntity org = value.getOrganization();
            if (isRefOnly(org)) {
                gen.writeStringField("ref", org.getBomRef());
            } else {
                gen.writeFieldName("organization");
                provider.defaultSerializeValue(org, gen);
            }
        } else if (value.getIndividual() != null && shouldSerializeField(value, version, "individual")) {
            gen.writeFieldName("individual");
            provider.defaultSerializeValue(value.getIndividual(), gen);
        }
        gen.writeEndObject();
    }

    private boolean isRefOnly(OrganizationalEntity org) {
        return org.getBomRef() != null
            && org.getName() == null
            && (org.getUrls() == null || org.getUrls().isEmpty())
            && (org.getContacts() == null || org.getContacts().isEmpty())
            && org.getAddress() == null;
    }
}
