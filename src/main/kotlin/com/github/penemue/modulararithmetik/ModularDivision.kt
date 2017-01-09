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
package com.github.penemue.modulararithmetik

import java.math.BigInteger
import java.util.concurrent.ConcurrentHashMap

internal object ModularDivision {

    private val reductions = ConcurrentHashMap<BigInteger, Reduction>()

    /**
     * returns `n` % `m` using [Barrett reduction][https://en.wikipedia.org/wiki/Barrett_reduction] for
     * random modulus and special reduction for moduli in the form 2^k - 1 and 2^k + 1
     */
    fun mod(n: BigInteger, m: BigInteger): BigInteger {
        if (n.bitLength < m.bitLength + 3) {
            return trivialReduction(n, m)
        }
        var reduction = reductions[m]
        if (reduction == null) {
            if (isLikeMersenneNumber(m)) {
                reduction = PowerOf2Minus1Reduction(m)
            } else if (isLikeFermatNumber(m)) {
                reduction = PowerOf2Plus1Reduction(m)
            } else {
                reduction = BarrettReduction(m)
            }
            reductions[m] = reduction
        }
        return reduction.remainder(n)
    }

    /**
     * returns `true` is the specified BigInteger is in the form 2^k - 1
     */
    private fun isLikeMersenneNumber(i: BigInteger): Boolean {
        return i == (BigInteger.ONE shl i.bitLength) - 1
    }

    /**
     * returns `true` is the specified BigInteger is in the form 2^k + 1
     */
    private fun isLikeFermatNumber(i: BigInteger): Boolean {
        return i == (BigInteger.ONE shl (i.bitLength - 1)) + 1
    }

    /**
     * Trivial reduction using comparisons and additions/subtractions
     */
    private fun trivialReduction(n: BigInteger, m: BigInteger): BigInteger {
        var result = n
        if (result.isNegative) {
            do {
                result += m
            } while (result.isNegative)
        } else {
            while (result >= m) {
                result -= m
            }
        }
        return result
    }

    private interface Reduction {

        fun remainder(n: BigInteger): BigInteger
    }

    /**
     * [Barrett reduction][https://en.wikipedia.org/wiki/Barrett_reduction] for arbitrary modulus
     */
    private class BarrettReduction(private val m: BigInteger) : Reduction {

        private val modLength = m.bitLength
        private val inverse = (BigInteger.ONE shl (modLength * 2)) / m

        override fun remainder(n: BigInteger): BigInteger {
            return trivialReduction(n - (((n shr modLength) * inverse) shr modLength) * m, m)
        }
    }

    /**
     * Reduction for modulus in the form 2^k + 1
     */
    private open class PowerOf2Plus1Reduction(private val m: BigInteger) : Reduction {

        private val modLength = m.bitLength - 1
        private val mask = m - 2

        override fun remainder(n: BigInteger): BigInteger {
            return trivialReduction((n and mask) - (n shr modLength), m)
        }
    }

    /**
     * Reduction for modulus in the form 2^k - 1
     */
    private open class PowerOf2Minus1Reduction(private val m: BigInteger) : Reduction {

        private val modLength = m.bitLength

        override fun remainder(n: BigInteger): BigInteger {
            return trivialReduction((n and m) + (n shr modLength), m)
        }
    }
}