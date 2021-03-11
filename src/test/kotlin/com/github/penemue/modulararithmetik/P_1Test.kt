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

import org.junit.Test
import java.math.BigInteger

class P_1Test {

    /**
     * Single stage variant of Pollard's P - 1 method for integer factorization
     * (https://en.wikipedia.org/wiki/Pollard%27s_p_%E2%88%92_1_algorithm) applied to M101,
     * the 101st Mersenne number (https://en.wikipedia.org/wiki/Mersenne_prime).
     */
    @Test
    fun factorM101() {
        val M101 = (BigInteger.ONE shl 101) - 1
        val one = BigInteger.ONE
        var base = BigInteger.TEN
        var product = one
        println("Factoring $M101:")
        repeat(Int.MAX_VALUE) {
            if (it > 1) {
                base = base exp it mod M101
                product = (base - 1) * product mod M101
                if (it % 10 == 0) {
                    val gcd = gcd(product, M101)
                    if (gcd != one) {
                        println("\nM101 factor found: $gcd, iterations: $it")
                        return
                    }
                }
            }
        }
    }
}