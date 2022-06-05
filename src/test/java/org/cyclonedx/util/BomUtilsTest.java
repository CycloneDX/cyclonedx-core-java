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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.model.Hash;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.io.FileUtils.ONE_KB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cyclonedx.model.Hash.Algorithm.MD5;
import static org.cyclonedx.model.Hash.Algorithm.SHA1;
import static org.cyclonedx.model.Hash.Algorithm.SHA3_256;
import static org.cyclonedx.model.Hash.Algorithm.SHA3_384;
import static org.cyclonedx.model.Hash.Algorithm.SHA3_512;
import static org.cyclonedx.model.Hash.Algorithm.SHA_256;
import static org.cyclonedx.model.Hash.Algorithm.SHA_384;
import static org.cyclonedx.model.Hash.Algorithm.SHA_512;

public class BomUtilsTest {

    @Test
    public void calculateHashesTest() throws Exception {
        final File file = new File(this.getClass().getResource("/hashtest.txt").toURI());

        final List<Hash> hashes = BomUtils.calculateHashes(file, CycloneDxSchema.Version.VERSION_12);

        assertThatHashIsComputed(hashes, MD5, "5dd39cab1c53c2c77cd352983f9641e1");
        assertThatHashIsComputed(hashes, SHA1, "b56df8ed5365fca1419818aa384ba3b5e7756047");
        assertThatHashIsComputed(hashes, SHA_256, "c87e2ca771bab6024c269b933389d2a92d4941c848c52f155b9b84e1f109fe35");
        assertThatHashIsComputed(hashes, SHA_384, "1600a408df6f0775d5d3d2f13d8355a7a668ffc1be13810041e883f510b05dba0662a55c0b6b9a49c51293fa892d00d7");
        assertThatHashIsComputed(hashes, SHA_512, "3de78a913cb8896f8f08ce3374b726b49ed00cc569621c5161c31eb80fca4d2f5e4443d42676dfc79743f345de7f0b95dbb2c97b2bc1a438a5a49c5f1b5298ac");
        if (supportsSha3Algorithms()) {
            assertThatHashIsComputed(hashes, SHA3_256, "d79acc39c4f826d0a0abb614866b0347ab7a782473f666fa49582cec7acb57c6");
            assertThatHashIsComputed(hashes, SHA3_384, "c557b693f910c4cab2ca5ba55083bbe08a1362038c8217179955af7686ae79f3547e383d0d63cca5e85240aed7fe8c0b");
            assertThatHashIsComputed(hashes, SHA3_512, "6a2825f3e8889f60ba965894a15d09b7f3958e0bd896ad293d3a04cda2cfa1aa9764567da61a62b4af55191d1108d9ca8b9e926411c9adc2d7b0f35f6fb11633");
        }
    }

    @Test
    public void calculateHashesForBigFileTest() throws Exception {
        final File file = generateBigFileWithReproductiveContent();

        final List<Hash> hashes = BomUtils.calculateHashes(file, CycloneDxSchema.Version.VERSION_12);

        assertThatHashIsComputed(hashes, MD5, "10be767d4f5874017ca03f3a9fe6627b");
        assertThatHashIsComputed(hashes, SHA1, "ae3c58e2a2d5e897b141c6552232976b99d91c9b");
        assertThatHashIsComputed(hashes, SHA_256, "0d797f9a8794ff2a00be343e012403620750609067bbf5899c340959159b86b3");
        assertThatHashIsComputed(hashes, SHA_384, "be5785a76b67105066f65fa9d2f85bd5f438149396eb039a4a89d1ae0822de34bd6502f242de992d29a253d3e5093001");
        assertThatHashIsComputed(hashes, SHA_512, "fed86c6ab6b75f044dd0aa8bc60468d4070cf081e26040daf61fd0d4c1d74ff6cead44b32b258483041d11eac6b80edafa2d5a0d0d59d5d7e750f775feac01f2");

        if (supportsSha3Algorithms()) {
            assertThatHashIsComputed(hashes, SHA3_256, "6afc5eea1405183f2a273deb87795bbdabfa9a23c07ec56b080b29f9a357931e");
            assertThatHashIsComputed(hashes, SHA3_384, "3bebb987e764240cca59e03f29d0c6f38221fa01295b84e0fe85e8057220f0ac173171c9a5ded67ef9b109cb63f12e51");
            assertThatHashIsComputed(hashes, SHA3_512, "eec26946678ae2d09db65ac618f4ddf150c517c06864296eeb5f9dc6138bf695341353527c058cf82eb6a8bacd8907ef1494c108c71f2f31f201ef521c6991ea");
        }
    }

    private void assertThatHashIsComputed(final List<Hash> hashes, final Hash.Algorithm algorithm, final String expectedHashValue) {
        assertThat(hashes)
                .as("Invalid hash for "+algorithm.getSpec()+" algorithm.")
                .filteredOn(hash-> algorithm.getSpec().equals(hash.getAlgorithm()))
                .singleElement()
                .extracting(Hash::getValue)
                .isEqualTo(expectedHashValue);
    }

    private File generateBigFileWithReproductiveContent() throws IOException {
        final File file = new File("target" + File.separator + "generateBigFileWithReproductiveContent.bin");
        if (file.exists() && file.isFile() && file.length() == 10 * ONE_KB * ONE_KB) {
            return file;
        }
        FileUtils.deleteQuietly(file);
        final byte[] partial = new byte[(int) ONE_KB];
        for (int i = 0; i < 10 * ONE_KB; i++) {
            Arrays.fill(partial, (byte)i);
            FileUtils.writeByteArrayToFile(file, partial, true);
        }
        return file;
    }

    /** Need to run these tests with JDK17 (for eg.ie. not JDK8) in order to have SHA3 algorithms support */
    static boolean supportsSha3Algorithms() {
        try {
            DigestUtils.getSha3_256Digest().update(new byte []{1});
            return true;
        } catch (IllegalArgumentException e) {
            if (e.getCause() instanceof NoSuchAlgorithmException) {
                return false;
            }
            throw e;
        }
    }

}
