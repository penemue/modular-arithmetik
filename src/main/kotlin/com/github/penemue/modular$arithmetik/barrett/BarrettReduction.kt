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
package com.github.penemue.`modular$arithmetik`.barrett

import com.github.penemue.`modular$arithmetik`.shl
import com.github.penemue.`modular$arithmetik`.shr
import java.math.BigInteger
import java.util.concurrent.ConcurrentHashMap

object BarrettReduction {

    private val inverses = ConcurrentHashMap<BigInteger, BigInteger>()

    /**
     * @return `n` % `m` using Barrett reduction (https://en.wikipedia.org/wiki/Barrett_reduction)
     */
    fun remainder(n: BigInteger, m: BigInteger): BigInteger {
        val dividendLength = n.bitLength()
        val modLength = m.bitLength()
        if (dividendLength - modLength < 4) {
            var result = n
            while (result >= m) {
                result -= m
            }
            return result
        }
        var inverse = inverses[m]
        if (inverse === null) {
            inverse = (BigInteger.ONE shl (modLength * 2 + 1)) / m
            inverses[m] = inverse
        }
        var result = ((n * inverse) shr (modLength * 2 + 1)) * m
        result = n - result
        return if (result >= m) result - m else result
    }
}