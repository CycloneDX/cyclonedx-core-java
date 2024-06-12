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

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Lifecycles
{
    @JacksonXmlProperty(localName = "lifecycle")
    private List<LifecycleChoice> lifecycleChoice;

    public List<LifecycleChoice> getLifecycleChoice() {
        return lifecycleChoice;
    }

    public void setLifecycleChoice(final List<LifecycleChoice> lifecycleChoice) {
        this.lifecycleChoice = lifecycleChoice;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Lifecycles)) {
            return false;
        }
        Lifecycles that = (Lifecycles) object;
        return Objects.equals(lifecycleChoice, that.lifecycleChoice);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lifecycleChoice);
    }
}
