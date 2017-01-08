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
package com.github.penemue.modulararithmetik.barrett

import com.github.penemue.modulararithmetik.shl
import com.github.penemue.modulararithmetik.shr
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
        var result: BigInteger
        if (dividendLength - modLength < 3) {
            result = n
        } else {
            var inverse = inverses[m]
            if (inverse === null) {
                inverse = (BigInteger.ONE shl (modLength * 2)) / m
                inverses[m] = inverse
            }
            result = n - (((n shr modLength) * inverse) shr modLength) * m
        }
        while (result >= m) {
            result -= m
        }
        return result
    }
}