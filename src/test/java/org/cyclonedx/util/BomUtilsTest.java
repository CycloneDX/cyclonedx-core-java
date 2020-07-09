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
package org.cyclonedx.util;

import org.cyclonedx.model.Hash;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.util.List;

public class BomUtilsTest {

    @Test
    public void calculateHashesTest() throws Exception {
        File file = new File(this.getClass().getResource("/hashtest.txt").toURI());
        List<Hash> hashes = BomUtils.calculateHashes(file);
        Assert.assertTrue(hashes.size() > 0);
        for (Hash hash: hashes) {
            if (hash.getAlgorithm().equals(Hash.Algorithm.MD5.getSpec())) {
                Assert.assertEquals("5dd39cab1c53c2c77cd352983f9641e1", hash.getValue());
            } else if (hash.getAlgorithm().equals(Hash.Algorithm.SHA1.getSpec())) {
                Assert.assertEquals("b56df8ed5365fca1419818aa384ba3b5e7756047", hash.getValue());
            } else if (hash.getAlgorithm().equals(Hash.Algorithm.SHA_256.getSpec())) {
                Assert.assertEquals("c87e2ca771bab6024c269b933389d2a92d4941c848c52f155b9b84e1f109fe35", hash.getValue());
            } else if (hash.getAlgorithm().equals(Hash.Algorithm.SHA_384.getSpec())) {
                Assert.assertEquals("1600a408df6f0775d5d3d2f13d8355a7a668ffc1be13810041e883f510b05dba0662a55c0b6b9a49c51293fa892d00d7", hash.getValue());
            } else if (hash.getAlgorithm().equals(Hash.Algorithm.SHA_512.getSpec())) {
                Assert.assertEquals("3de78a913cb8896f8f08ce3374b726b49ed00cc569621c5161c31eb80fca4d2f5e4443d42676dfc79743f345de7f0b95dbb2c97b2bc1a438a5a49c5f1b5298ac", hash.getValue());
            } else if (hash.getAlgorithm().equals(Hash.Algorithm.SHA3_256.getSpec())) {
                Assert.assertEquals("d79acc39c4f826d0a0abb614866b0347ab7a782473f666fa49582cec7acb57c6", hash.getValue());
            } else if (hash.getAlgorithm().equals(Hash.Algorithm.SHA3_384.getSpec())) {
                Assert.assertEquals("c557b693f910c4cab2ca5ba55083bbe08a1362038c8217179955af7686ae79f3547e383d0d63cca5e85240aed7fe8c0b", hash.getValue());
            } else if (hash.getAlgorithm().equals(Hash.Algorithm.SHA3_512.getSpec())) {
                Assert.assertEquals("6a2825f3e8889f60ba965894a15d09b7f3958e0bd896ad293d3a04cda2cfa1aa9764567da61a62b4af55191d1108d9ca8b9e926411c9adc2d7b0f35f6fb11633", hash.getValue());
            }
        }
    }

}
