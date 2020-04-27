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

public class Swid {

    private String tagId;
    private String name;
    private String version;
    private int tagVersion;
    private boolean patch;
    private AttachmentText attachmentText;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getTagVersion() {
        return tagVersion;
    }

    public void setTagVersion(int tagVersion) {
        this.tagVersion = tagVersion;
    }

    public boolean isPatch() {
        return patch;
    }

    public void setPatch(boolean patch) {
        this.patch = patch;
    }

    public AttachmentText getAttachmentText() {
        return attachmentText;
    }

    public void setAttachmentText(AttachmentText attachmentText) {
        this.attachmentText = attachmentText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Swid swid = (Swid) o;
        return Objects.equals(tagId, swid.tagId) &&
                Objects.equals(name, swid.name) &&
                Objects.equals(version, swid.version) &&
                Objects.equals(tagVersion, swid.tagVersion) &&
                Objects.equals(patch, swid.patch) &&
                Objects.equals(attachmentText, swid.attachmentText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, name, version, tagVersion, patch, attachmentText);
    }
}
