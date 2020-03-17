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

@SuppressWarnings("unused")
public class Commit extends ExtensibleElement {

    private String uid;
    private String url;
    private IdentifiableActionType author;
    private IdentifiableActionType committer;
    private String message;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public IdentifiableActionType getAuthor() {
        return author;
    }

    public void setAuthor(IdentifiableActionType author) {
        this.author = author;
    }

    public IdentifiableActionType getCommitter() {
        return committer;
    }

    public void setCommitter(IdentifiableActionType committer) {
        this.committer = committer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commit)) return false;
        Commit commit = (Commit) o;
        return Objects.equals(uid, commit.uid) &&
                Objects.equals(url, commit.url) &&
                Objects.equals(author, commit.author) &&
                Objects.equals(committer, commit.committer) &&
                Objects.equals(message, commit.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, url, author, committer, message);
    }
}
