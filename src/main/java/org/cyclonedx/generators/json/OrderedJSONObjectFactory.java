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

import org.json.JSONObject;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class OrderedJSONObjectFactory {

    /**
     * Changes the internal implementation of an unordered HashMap to an ordered
     * LinkedHashMap. An unmodified JSONObject will be returned in the event of
     * a reflection exception.
     * @return a JSONObject
     */
    public static JSONObject create() {
        try {
            Class<JSONObject> cls = (Class<JSONObject>) Class.forName("org.json.JSONObject");
            JSONObject jsonObject = new JSONObject();
            Field field = cls.getDeclaredField("map");
            field.setAccessible(true);
            field.set(jsonObject, new LinkedHashMap<String, Object>());
            field.setAccessible(false);
            return jsonObject;
        } catch (Exception e) {
            return new JSONObject();
        }
    }
}
