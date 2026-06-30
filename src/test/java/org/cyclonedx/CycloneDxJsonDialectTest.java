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
package org.cyclonedx;

import com.networknt.schema.dialect.Draft7;
import com.networknt.schema.keyword.Keyword;
import com.networknt.schema.keyword.NonValidationKeyword;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that the CycloneDX JSON dialect registers the CycloneDX-specific
 * {@code meta:enum} and {@code deprecated} annotation keywords.
 * <p>
 * The CycloneDX 1.6 JSON schema declares the draft-07 dialect but uses these
 * two keywords, which are not part of draft-07. Without registering them the
 * underlying {@code json-schema-validator} logs a "Unknown keyword" warning
 * for each one. Treating them as {@link NonValidationKeyword}s keeps validation
 * behaviour unchanged while suppressing the warnings.
 */
class CycloneDxJsonDialectTest {

    @Test
    void registersCycloneDxAnnotationKeywordsAsNonValidating() {
        final Map<String, Keyword> keywords = CycloneDxSchema.getCycloneDxJsonDialect().getKeywords();

        assertThat(keywords).containsKey("meta:enum");
        assertThat(keywords).containsKey("deprecated");
        assertThat(keywords.get("meta:enum")).isInstanceOf(NonValidationKeyword.class);
        assertThat(keywords.get("deprecated")).isInstanceOf(NonValidationKeyword.class);
    }

    @Test
    void stockDraft7DialectDoesNotKnowCycloneDxKeywords() {
        // Guards against the fix becoming redundant: if upstream ever adds these
        // keywords to draft-07 this assertion fails, signalling the custom dialect
        // can be removed.
        final Map<String, Keyword> keywords = Draft7.getInstance().getKeywords();

        assertThat(keywords).doesNotContainKey("meta:enum");
        assertThat(keywords).doesNotContainKey("deprecated");
    }
}
