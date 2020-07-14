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

import com.alibaba.fastjson.annotation.JSONField;
import org.cyclonedx.converters.DependencyDeserializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dependency {

    private String ref;
    @JSONField(name = "dependsOn", deserializeUsing = DependencyDeserializer.class)
    private List<Dependency> dependencies;

    public Dependency(final String ref) {
        this.ref = ref;
    }

    public Dependency() { }

    public String getRef() {
        return ref;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(final List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public void addDependency(final Dependency dependency) {
        if (dependencies == null) {
            dependencies = new ArrayList<>();
        }
        boolean found = dependencies.stream().anyMatch(d -> d.getRef().equals(dependency.getRef()));
        if (!found) {
            dependencies.add(dependency);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dependency)) return false;
        Dependency that = (Dependency) o;
        return Objects.equals(ref, that.ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ref);
    }
}
