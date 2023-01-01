/**
 * Copyright 2016 - 2023 Vyacheslav Lukianov (https://github.com/penemue)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.penemue.modulararithmetik

import org.junit.Assert
import org.junit.Test
import java.math.BigInteger

class SqrtTest {

    @Test
    fun sqrt() {
        Assert.assertEquals(BigInteger.ZERO, sqrt(BigInteger.ZERO))
        Assert.assertEquals(BigInteger.ONE, sqrt(BigInteger.ONE))
        Assert.assertEquals(BigInteger.ONE, sqrt(BigInteger.valueOf(2)))
        Assert.assertEquals(BigInteger.ONE, sqrt(BigInteger.valueOf(3)))
        Assert.assertEquals(BigInteger.valueOf(2), sqrt(BigInteger.valueOf(4)))
        Assert.assertEquals(BigInteger.valueOf(2), sqrt(BigInteger.valueOf(5)))
        Assert.assertEquals(BigInteger.valueOf(2), sqrt(BigInteger.valueOf(8)))
        Assert.assertEquals(BigInteger.valueOf(3), sqrt(BigInteger.valueOf(9)))
        Assert.assertEquals(BigInteger.valueOf(99), sqrt(BigInteger.valueOf(9998)))
        Assert.assertEquals(BigInteger.valueOf(99), sqrt(BigInteger.valueOf(9999)))
        Assert.assertEquals(BigInteger.valueOf(100), sqrt(BigInteger.valueOf(10000)))
        Assert.assertEquals(BigInteger.valueOf(100), sqrt(BigInteger.valueOf(10001)))
        Assert.assertEquals(BigInteger.valueOf(100), sqrt(BigInteger.valueOf(10002)))
    }
}