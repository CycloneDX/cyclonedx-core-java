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
import org.cyclonedx.Version;
import org.cyclonedx.model.Hash;
import org.cyclonedx.model.VersionFilter;

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
import java.security.NoSuchAlgorithmException;
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
    public static List<Hash> calculateHashes(final File file, final Version schemaVersion) throws IOException {
        final List<Hash.Algorithm> algorithms = new LinkedList<>(Arrays.asList(
                Hash.Algorithm.MD5,
                Hash.Algorithm.SHA1,
                Hash.Algorithm.SHA_256,
                Hash.Algorithm.SHA_512,
                Hash.Algorithm.SHA3_256,
                Hash.Algorithm.SHA3_512
        ));
        if (schemaVersion.getVersion() >= 1.2) {
            algorithms.add(Hash.Algorithm.SHA_384);
            algorithms.add(Hash.Algorithm.SHA3_384);
        }
        return calculateHashes(file, schemaVersion, algorithms);
    }

    /**
     * Calculates the hashes of the specified file using only the specified algorithms.
     * @param file the File to calculate hashes on
     * @param schemaVersion enum denoting the schema version in use. Affects hash algorithm selection.
     * @param algorithms the list of algorithms to calculate hashes for
     * @return a List of Hash objects
     * @throws IOException an IOException
     * @throws IllegalArgumentException if an algorithm is not supported by the schema version
     * @since 9.2.0
     */
    public static List<Hash> calculateHashes(final File file, final Version schemaVersion,
                                             final List<Hash.Algorithm> algorithms) throws IOException {
        if (file == null || !file.exists() || !file.canRead() || !file.isFile()) {
            return null;
        }
        if (algorithms == null || algorithms.isEmpty()) {
            return new LinkedList<>();
        }

        final List<Hash> hashes = new LinkedList<>();
        final List<MessageDigest> digests = new LinkedList<>();

        for (Hash.Algorithm algorithm : algorithms) {
            validateAlgorithmForVersion(algorithm, schemaVersion);
            MessageDigest digest = getDigestForAlgorithm(algorithm);
            if (digest != null) {
                digests.add(digest);
            }
        }

        final int bufSize = 8192;
        try (InputStream fis = new BufferedInputStream(Files.newInputStream(file.toPath()), bufSize)) {
            final byte[] buf = new byte[bufSize];
            while (fis.available() > 0) {
                final int read = fis.read(buf);
                digests.stream().parallel().forEach(d -> d.update(buf, 0, read));
            }
        }
        digests.stream().map(d -> new Hash(toAlgorithm(d), Hex.encodeHexString(d.digest()))).forEach(hashes::add);
        return hashes;
    }

    private static void validateAlgorithmForVersion(Hash.Algorithm algorithm, Version schemaVersion) {
        try {
            java.lang.reflect.Field field = Hash.Algorithm.class.getField(algorithm.name());
            VersionFilter versionFilter = field.getAnnotation(VersionFilter.class);
            if (versionFilter != null) {
                Version minVersion = versionFilter.value();
                if (schemaVersion.getVersion() < minVersion.getVersion()) {
                    throw new IllegalArgumentException(
                            "Algorithm " + algorithm.getSpec() + " is not supported in schema version " +
                                    schemaVersion + ". Minimum required version: " + minVersion);
                }
            }
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
    }

    private static MessageDigest getDigestForAlgorithm(Hash.Algorithm algorithm) {
        try {
            switch (algorithm) {
                case MD5:
                    return DigestUtils.getMd5Digest();
                case SHA1:
                    return DigestUtils.getSha1Digest();
                case SHA_256:
                    return DigestUtils.getSha256Digest();
                case SHA_384:
                    return DigestUtils.getSha384Digest();
                case SHA_512:
                    return DigestUtils.getSha512Digest();
                case SHA3_256:
                    return DigestUtils.getSha3_256Digest();
                case SHA3_384:
                    return DigestUtils.getSha3_384Digest();
                case SHA3_512:
                    return DigestUtils.getSha3_512Digest();
                case BLAKE2b_256:
                case BLAKE2b_384:
                case BLAKE2b_512:
                case BLAKE3:
                    return MessageDigest.getInstance(algorithm.getSpec());
                default:
                    throw new IllegalArgumentException("Unsupported algorithm: " + algorithm.getSpec());
            }
        } catch (NoSuchAlgorithmException | NoSuchMethodError e) {
            throw new IllegalArgumentException("Algorithm not available: " + algorithm.getSpec(), e);
        }
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
