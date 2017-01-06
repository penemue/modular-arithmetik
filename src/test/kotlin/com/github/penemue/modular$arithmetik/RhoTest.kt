/**
 * Copyright 2016 - 2017 Vyacheslav Lukianov (https://github.com/penemue)
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
package com.github.penemue.`modular$arithmetik`

import org.junit.Test
import java.math.BigInteger

class RhoTest {

    /**
     * Pollard's Monte Carlo *rho*-method for integer factorization applied to F8, the 8th Fermat number.
     * In 1980, a faster version of the algorithm was used to factor F8, and the factorization took 2 hours
     * on Univac 1100/42 ([http://maths-people.anu.edu.au/~brent/pd/rpb061.pdf]).
     * Nowadays it takes less than half a minute on a commodity computer.
     */
    @Test
    fun factorF8() {
        val F8 = (BigInteger.ONE shl 256) + 1
        val one = BigInteger.ONE
        var x1 = one
        var x2 = one
        var product = one
        println("Factoring $F8:")
        repeat(Int.MAX_VALUE, {
            x1 = x1 * x1 + 2 mod F8
            x2 = x2 * x2 + 2 mod F8
            x2 = x2 * x2 + 2 mod F8
            product = product * (x2 - x1 mod F8) mod F8
            if (it % 1000 == 0) {
                val gcd = gcd(product, F8)
                if (gcd != one) {
                    println("\nF8 factor found: $gcd, iterations: $it")
                    return
                }
            }
        })
    }
}