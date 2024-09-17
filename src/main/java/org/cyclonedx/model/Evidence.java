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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.cyclonedx.Version;
import org.cyclonedx.model.component.evidence.Callstack;
import org.cyclonedx.model.component.evidence.Identity;
import org.cyclonedx.model.component.evidence.Occurrence;
import org.cyclonedx.util.deserializer.IdentityDeserializer;
import org.cyclonedx.util.deserializer.LicenseDeserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"identity", "occurrences", "callstack", "licenses", "copyright"})
public class Evidence
    extends ExtensibleElement
{
    private LicenseChoice licenses;

    private List<Copyright> copyright;

    @VersionFilter(Version.VERSION_15)
    private List<Identity> identities;

    @VersionFilter(Version.VERSION_15)
    private List<Occurrence> occurrences;

    @VersionFilter(Version.VERSION_15)
    private Callstack callstack;

    @Deprecated
    public LicenseChoice getLicenseChoice() {
        return getLicenses();
    }

    @Deprecated
    @JsonIgnore
    public void setLicenseChoice(LicenseChoice licenseChoice) {
        setLicenses(licenseChoice);
    }

    @JsonDeserialize(using = LicenseDeserializer.class)
    public LicenseChoice getLicenses() {
        return licenses;
    }

    @JacksonXmlElementWrapper (useWrapping = false)
    public void setLicenses(LicenseChoice licenses) {
        this.licenses = licenses;
    }

    @JacksonXmlElementWrapper(localName = "copyright")
    @JacksonXmlProperty(localName = "text")
    @JsonProperty("copyright")
    public List<Copyright> getCopyright() {
        return copyright;
    }

    public void setCopyright(List<Copyright> copyright) {
        this.copyright = copyright;
    }

    public void addCopyright(Copyright copyright) {
        if (this.copyright == null) {
            this.copyright = new ArrayList<>();
        }
        this.copyright.add(copyright);
    }

    @JsonProperty("occurrences")
    @JacksonXmlElementWrapper(localName = "occurrences")
    @JacksonXmlProperty(localName = "occurrence")
    @VersionFilter(Version.VERSION_15)
    public List<Occurrence> getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(final List<Occurrence> occurrences) {
        this.occurrences = occurrences;
    }

    public Callstack getCallstack() {
        return callstack;
    }

    public void setCallstack(final Callstack callstack) {
        this.callstack = callstack;
    }

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "identity")
    @JsonProperty("identity")
    @JsonDeserialize(using = IdentityDeserializer.class)
    @VersionFilter(Version.VERSION_15)
    public List<Identity> getIdentities() {
        return identities;
    }

    public void setIdentities(final List<Identity> identities) {
        this.identities = identities;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Evidence)) {
            return false;
        }
        Evidence evidence = (Evidence) object;
        return Objects.equals(licenses, evidence.licenses) &&
            Objects.equals(copyright, evidence.copyright) &&
            Objects.equals(identities, evidence.identities) &&
            Objects.equals(occurrences, evidence.occurrences) &&
            Objects.equals(callstack, evidence.callstack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licenses, copyright, identities, occurrences, callstack);
    }
}
