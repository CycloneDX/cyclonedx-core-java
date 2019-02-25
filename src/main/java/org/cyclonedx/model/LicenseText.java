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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.Objects;

@SuppressWarnings("unused")
@XmlRootElement(namespace = CycloneDxSchema.NS_BOM_LATEST)
public class LicenseText {

    private String contentType;
    private String encoding;
    private String text;

    public String getContentType() {
        return contentType;
    }

    @XmlAttribute(name = "content-type")
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getEncoding() {
        return encoding;
    }

    @XmlAttribute(name = "encoding")
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getText() {
        return text;
    }

    @XmlValue
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LicenseText licenseText = (LicenseText) o;
        return Objects.equals(contentType, licenseText.contentType) &&
                Objects.equals(encoding, licenseText.encoding) &&
                Objects.equals(text, licenseText.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, encoding, text);
    }
}
