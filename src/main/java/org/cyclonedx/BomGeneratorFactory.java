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
package org.cyclonedx;

import org.cyclonedx.model.Component;
import java.util.Set;

public class BomGeneratorFactory {

    private BomGeneratorFactory() {
    }

    public static BomGenerator create(CycloneDxSchema.Version version, Set<Component> components) {
        if (CycloneDxSchema.Version.VERSION_10 == version) {
            return new BomGenerator10(components);
        } else {
            return new BomGenerator11(components);
        }
    }
}
