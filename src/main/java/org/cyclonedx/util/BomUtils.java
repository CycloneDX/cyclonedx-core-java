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

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.model.Hash;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class BomUtils {

    private BomUtils() {

    }

    /**
     * Calculates the hashes of the specified file.
     * @param file the File to calculate hashes on
     * @param schemaVersion enum denoting the schema version in use. Affects hash algorithm selection.
     * @return a List of Hash objets
     * @throws IOException an IOException
     * @since 1.0.0
     */
    public static List<Hash> calculateHashes(final File file, final CycloneDxSchema.Version schemaVersion) throws IOException {
        if (file == null || !file.exists() || !file.canRead() || !file.isFile()) {
            return null;
        }
        long start = System.currentTimeMillis();
        final List<Hash> hashes = new LinkedList<>();
        final List<MessageDigest> digests = new LinkedList<>(Arrays.asList(DigestUtils.getMd5Digest()
                , DigestUtils.getSha1Digest()
                , DigestUtils.getSha256Digest()  , DigestUtils.getSha512Digest()));
        if (schemaVersion.getVersion() >= 1.2) {
            digests.add(DigestUtils.getSha384Digest());
            try {
                digests.add(DigestUtils.getSha3_384Digest());
            } catch (Exception | NoSuchMethodError e) { /* Not available in Java 8 and only available in later versions of DigestUtils */ }

        }
        try {
            digests.add(DigestUtils.getSha3_256Digest());
        } catch (Exception | NoSuchMethodError e) { /* Not available in Java 8 and only available in later versions of DigestUtils */ }
        try {
            digests.add(DigestUtils.getSha3_512Digest());
        } catch (Exception | NoSuchMethodError e) { /* Not available in Java 8 and only available in later versions of DigestUtils */ }

        final int bufSize = 8192;
        try (InputStream fis = new BufferedInputStream(Files.newInputStream(file.toPath()),bufSize)) {
            final byte[] buf = new byte[bufSize];
            while (fis.available() > 0) {
                final int read = fis.read(buf);
                digests.stream().parallel().forEach(d-> d.update(buf, 0, read));
            }
        }
        digests.stream().map(d->new Hash(toAlgorithm(d), Hex.encodeHexString(d.digest()))).forEach(hashes::add);
        return hashes;
    }

    private static Hash.Algorithm toAlgorithm(MessageDigest digest) {
        for (Hash.Algorithm value : Hash.Algorithm.values()) {
            if (value.getSpec().equals(digest.getAlgorithm())) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unable to find algorithm matching '"+digest.getAlgorithm()+"'. Known algorithms: "+ Arrays.stream(Hash.Algorithm.values()).map(Hash.Algorithm::getSpec).sorted().collect(Collectors.joining()));
    }

    @Deprecated
    public static boolean validateUrlString(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    public static boolean validateUriString(String uri) {
        try {
            new URI(uri);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
