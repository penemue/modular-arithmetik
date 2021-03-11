/**
 * Copyright 2016 - 2021 Vyacheslav Lukianov (https://github.com/penemue)
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

class PowModTest {

    @Test
    fun powModTest() {
        val mersenne521 = (BigInteger.ONE shl 521) - 1
        val pi = BigInteger.valueOf(31415926L)
        val e = BigInteger.valueOf(271828L)
        Assert.assertEquals(pi.modPow(e, mersenne521), pi exp e mod mersenne521)
        Assert.assertEquals(e.modPow(pi, mersenne521), e exp pi mod mersenne521)
    }
}