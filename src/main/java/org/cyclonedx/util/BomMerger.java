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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.cyclonedx.model.Bom;
import org.cyclonedx.model.BomReference;
import org.cyclonedx.model.Component;
import org.cyclonedx.model.Composition;
import org.cyclonedx.model.Dependency;
import org.cyclonedx.model.ExternalReference;
import org.cyclonedx.model.Metadata;
import org.cyclonedx.model.Service;
import org.cyclonedx.model.Tool;
import org.cyclonedx.model.vulnerability.Vulnerability;
import org.cyclonedx.model.vulnerability.Vulnerability.Affect;

public class BomMerger {

    public Bom hierarchicalMerge(Iterable<Bom> boms, Component bomSubject) {
        Bom result = new Bom();
        Metadata resultMetadata = new Metadata();
        result.setMetadata(resultMetadata);

        List<Dependency> bomSubjectDependencies = new ArrayList<>();

        if (bomSubject != null) {
            resultMetadata.setComponent(bomSubject);
            if (bomSubject.getBomRef() == null) {
                bomSubject.setBomRef(componentBomRefNamespace(bomSubject));
            }

            Dependency topLevelDependency = new Dependency(bomSubject.getBomRef());
            topLevelDependency.setDependencies(bomSubjectDependencies);
            result.addDependency(topLevelDependency);
        }

        for (Bom bom : boms) {
            Metadata bomMetadata = bom.getMetadata();
            Component bomComponent = bomMetadata == null ? null : bomMetadata.getComponent();
            if (bomComponent == null) {
                throw new IllegalArgumentException("Required metadata (top level) component is missing from BOM.");
            }

            List<Tool> bomTools = bomMetadata.getTools();
            if (bomTools != null) {
                bomTools.forEach(resultMetadata::addTool);
            }

            List<Component> bomComponents = bom.getComponents();
            if (bomComponents != null) {
                bomComponents.forEach(bomComponent::addComponent);
            }

            String bomRefNamespace = componentBomRefNamespace(bomComponent);

            namespaceComponentBomRefs(bomRefNamespace, bomComponent);

            if (bomComponent.getBomRef() == null) {
                bomComponent.setBomRef(bomRefNamespace);
            }

            bomSubjectDependencies.add(new Dependency(bomComponent.getBomRef()));

            result.addComponent(bomComponent);

            List<Service> bomServices = bom.getServices();
            if (bomServices != null) {
                for (Service service : bomServices) {
                    service.setBomRef(namespacedBomRef(bomRefNamespace, service.getBomRef()));
                    result.addService(service);
                }
            }

            List<ExternalReference> bomExternalRefs = bom.getExternalReferences();
            if (bomExternalRefs != null) {
                bomExternalRefs.forEach(result::addExternalReference);
            }

            List<Dependency> bomDependencies = bom.getDependencies();
            if (bomDependencies != null) {
                namespaceDependencyBomRefs(bomRefNamespace, bomDependencies);
                bomDependencies.forEach(result::addDependency);
            }

            List<Composition> bomCompositions = bom.getCompositions();
            if (bomCompositions != null) {
                namespaceCompositions(bomRefNamespace, bomCompositions);
                bomCompositions.forEach(result::addComposition);
            }

            List<Vulnerability> bomVulnerabilities = bom.getVulnerabilities();
            if (bomVulnerabilities != null) {
                namespaceVulnerabilityRefs(bomSubject.getBomRef(), bomVulnerabilities);
                bomVulnerabilities.forEach(result::addVulnerability);
            }
        }

        return result;
    }

    private String componentBomRefNamespace(Component component) {
        StringBuilder builder = new StringBuilder(256);
        String group = component.getGroup();
        if (group != null) {
            builder.append(group).append('.');
        }
        builder.append(component.getName()).append('@').append(component.getVersion());
        return builder.toString();
    }

    private String namespacedBomRef(String bomRefNamespace, String bomRef) {
        return StringUtils.isEmpty(bomRef) ? null : bomRefNamespace + ":" + bomRef;
    }

    private void namespaceBomRefs(String bomRefNamespace, List<BomReference> bomRefs) {
        if (bomRefs != null) {
            for (BomReference bomRef : bomRefs) {
                bomRef.setRef(namespacedBomRef(bomRefNamespace, bomRef.getRef()));
            }
        }
    }

    private void namespaceComponentBomRefs(String bomRefNamespace, Component topComponent) {
        Deque<Component> components = new ArrayDeque<>();
        components.push(topComponent);

        while (!components.isEmpty()) {
            Component currentComponent = components.pop();
            currentComponent.setBomRef(namespacedBomRef(bomRefNamespace, currentComponent.getBomRef()));

            List<Component> subComponents = currentComponent.getComponents();
            if (subComponents != null) {
                subComponents.forEach(components::push);
            }
        }
    }

    private void namespaceDependencyBomRefs(String bomRefNamespace, List<Dependency> dependencies) {
        Deque<Dependency> pendingDependencies = new ArrayDeque<>(dependencies);
        while (!pendingDependencies.isEmpty()) {
            Dependency dependency = pendingDependencies.pop();
            dependency.setRef(namespacedBomRef(bomRefNamespace, dependency.getRef()));

            List<Dependency> subDependencies = dependency.getDependencies();
            if (subDependencies != null) {
                subDependencies.forEach(pendingDependencies::push);
            }
        }
    }

    private void namespaceCompositions(String bomRefNamespace, List<Composition> compositions) {
        for (Composition composition : compositions) {
            namespaceBomRefs(bomRefNamespace, composition.getAssemblies());
            namespaceBomRefs(bomRefNamespace, composition.getDependencies());
        }
    }

    private void namespaceVulnerabilityRefs(String bomRefNamespace, List<Vulnerability> vulnerabilities) {
        for (Vulnerability vulnerability : vulnerabilities) {
            vulnerability.setBomRef(namespacedBomRef(bomRefNamespace, vulnerability.getBomRef()));

            List<Affect> affects = vulnerability.getAffects();
            if (affects != null) {
                for (Affect affect : affects) {
                    affect.setRef(bomRefNamespace);
                }
            }
        }
    }

}
