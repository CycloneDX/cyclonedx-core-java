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
package org.cyclonedx.generators.json;

import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

abstract class AbstractBomJsonGenerator extends CycloneDxSchema implements BomJsonGenerator {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset

    JSONObject doc = OrderedJSONObjectFactory.create();

    void createMetadataNode(final JSONObject doc, final Metadata metadata) {
        if (metadata != null) {
            final JSONObject mdo = OrderedJSONObjectFactory.create();
            if (metadata.getTimestamp() != null) {
                mdo.put("timestamp", dateFormat.format(metadata.getTimestamp()));
            } else {
                mdo.put("timestamp", dateFormat.format(new Date()));
            }
            if (metadata.getTools() != null && metadata.getTools().size() > 0) {
                final JSONArray jsonTools = new JSONArray();
                for (final Tool tool: metadata.getTools()) {
                    final JSONObject jsonTool = OrderedJSONObjectFactory.create();
                    jsonTool.put("vendor", tool.getVendor());
                    jsonTool.put("name", tool.getName());
                    jsonTool.put("version", tool.getVersion());
                    // TODO add hashes
                    jsonTools.put(jsonTool);
                }
                mdo.put("tools", jsonTools);
            }
            if (metadata.getAuthors() != null && metadata.getAuthors().size() > 0) {
                final JSONArray jsonAuthors = new JSONArray();
                for (final OrganizationalContact author: metadata.getAuthors()) {
                    final JSONObject jsonAuthor = OrderedJSONObjectFactory.create();
                    jsonAuthor.put("name", author.getName());
                    jsonAuthor.put("email", author.getEmail());
                    jsonAuthor.put("phone", author.getPhone());
                    jsonAuthors.put(jsonAuthor);
                }
                mdo.put("authors", jsonAuthors);
            }
            if (metadata.getComponent() != null) {
                mdo.put("component", createComponentNode(metadata.getComponent()));
            }
            doc.put("metadata", mdo);
        }
    }

    void createComponentsNode(final JSONObject doc, final List<Component> components) {
        if (components != null && components.size() > 0) {

        }
    }

    JSONObject createComponentNode(final Component component) {
        final JSONObject json = OrderedJSONObjectFactory.create();
        json.put("type", component.getType().getTypeName());
        json.put("bom-ref", component.getBomRef());
        //json.put("supplier", component.getSupplier());
        json.put("author", component.getAuthor());
        json.put("publisher", component.getPublisher());
        json.put("group", component.getGroup());
        json.put("name", component.getName());
        json.put("version", component.getVersion());
        json.put("description", component.getDescription());
        json.put("scope", component.getScope());
        //json.put("hashes", component.getHashes());
        //json.put("licenses", component.getLicenseChoice());
        json.put("copyright", component.getCopyright());
        json.put("cpe", component.getCpe());
        json.put("purl", component.getPurl());
        //json.put("swid", component.getSwid());
        json.put("modified", component.isModified()); // TODO
        //json.put("pedigree", component.getPedigree());
        //json.put("externalReferences", component.getExternalReferences());
        //json.put("components", component.getComponents());
        return json;
    }

    void createExternalReferencesNode(final JSONObject doc, final List<ExternalReference> references) {
        if (references != null && references.size() > 0) {

        }
    }

    public String toJsonString() {
        return doc.toString(2);
    }

    @Override
    public String toString() {
        return doc.toString(0);
    }

}
