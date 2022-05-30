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
package org.cyclonedx.util;

import org.cyclonedx.model.Bom;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.vulnerability.Vulnerability;
import java.util.Collections;
import java.util.List;

public class ObjectLocator {

    private final Bom bom;
    private final String bomRef;
    private Object object;
    private boolean isMetadataComponent;

    public ObjectLocator(final Bom bom, final String bomRef) {
        this.bom = bom;
        this.bomRef = bomRef;
    }

    public Object getObject() {
        return object;
    }

    public boolean found() {
        return this.object != null;
    }

    public boolean isMetadataComponent() {
        return isMetadataComponent;
    }

    public boolean isComponent() {
        return this.object instanceof Component;
    }

    public boolean isService() {
        return this.object instanceof Service;
    }

    public boolean isVulnerability() {
        return this.object instanceof Vulnerability;
    }

    public ObjectLocator locate() {
        if (this.bom == null || this.bomRef == null) return this;

        // TODO is BOM-Link


        if (this.bom.getMetadata() != null && this.bom.getMetadata().getComponent() != null) {
            Component c = findComponent(Collections.singletonList(this.bom.getMetadata().getComponent()), this.bomRef);
            if (c != null) {
                this.isMetadataComponent = true;
                this.object = c;
            }
            c = findComponent(this.bom.getComponents(), this.bomRef);
            if (c != null) {
                this.object = c;
            }
            final Service s = findService(this.bom.getServices(), this.bomRef);
            if (s != null) {
                this.object = s;
            }
            final Vulnerability v = findVulnerability(this.bom.getVulnerabilities(), this.bomRef);
            if (v != null) {
                this.object = v;
            }
        }
        return this;
    }

    private static Component findComponent(final List<Component> components, final String bomRef) {
        if (components == null) return null;
        for (final Component component: components) {
            if (bomRef.equals(component.getBomRef())) {
                return component;
            } else if (component.getComponents() != null) {
                final Component child = findComponent(component.getComponents(), bomRef);
                if (child != null) return child;
            }
        }
        return null;
    }

    private static Service findService(final List<Service> services, final String bomRef) {
        if (services == null) return null;
        for (final Service service: services) {
            if (bomRef.equals(service.getBomRef())) {
                return service;
            } else if (service.getServices() != null) {
                final Service child = findService(service.getServices(), bomRef);
                if (child != null) return child;
            }
        }
        return null;
    }

    private static Vulnerability findVulnerability(final List<Vulnerability> vulnerabilities, final String bomRef) {
        if (vulnerabilities == null) return null;
        for (final Vulnerability vulnerability: vulnerabilities) {
            if (bomRef.equals(vulnerability.getBomRef())) {
                return vulnerability;
            }
        }
        return null;
    }
}
