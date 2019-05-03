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

@SuppressWarnings("unused")
public class ExternalReference {

    public enum Type {
        VCS("vcs"),
        ISSUE_TRACKER("issue-tracker"),
        WEBSITE("website"),
        ADVISORIES("advisories"),
        BOM("bom"),
        MAILING_LIST("mailing-list"),
        SOCIAL("social"),
        CHAT("chat"),
        DOCUMENTATION("documentation"),
        SUPPORT("support"),
        DISTRIBUTION("distribution"),
        LICENSE("license"),
        BUILD_META("build-meta"),
        BUILD_SYSTEM("build-system"),
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

    public void setUrl(String url) {
        this.url = url;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
