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

import org.cyclonedx.exception.BomLinkException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * Parses URNs that conform to the "cdx" namespace identifier
 * as defined by https://www.iana.org/assignments/urn-formal/cdx
 * @since 7.1.4
 */
public class BomLink {

    private static final String EXCEPTION_MESSAGE = "Invalid BOM-Link. URN syntax must conform to \"urn:cdx:serialNumber/version#bom-ref\" where serialNumber is a valid UUID (required), version is an integer (required), and bom-ref is optional.";
    private final UUID serialNumber;
    private final int version;
    private final String bomRef;

    public BomLink(final String urn) throws BomLinkException {
        if (urn != null && urn.startsWith("urn:cdx:")) {
            try {
                final URI uri = new URI(urn);
                final String[] parts = uri.getSchemeSpecificPart().split("/");
                if (parts.length == 2) {
                    this.serialNumber = UUID.fromString(parts[0].replace("cdx:", ""));
                    this.version = Integer.parseInt(parts[1]);
                    this.bomRef = uri.getFragment();
                } else {
                    throw new BomLinkException(EXCEPTION_MESSAGE);
                }
            } catch (URISyntaxException | IllegalArgumentException e) {
                throw new BomLinkException(EXCEPTION_MESSAGE, e);
            }
        } else {
            throw new BomLinkException(EXCEPTION_MESSAGE);
        }
    }

    public static boolean isBomLink(final String bomRef) {
        try {
            new BomLink(bomRef);
            return true;
        } catch (BomLinkException e) {
            return false;
        }
    }

    public UUID getSerialNumber() {
        return serialNumber;
    }

    public int getVersion() {
        return version;
    }

    public String getBomRef() {
        return bomRef;
    }
}
