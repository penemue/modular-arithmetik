/**
 * Copyright 2016 - 2024 Vyacheslav Lukianov (https://github.com/penemue)
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
import java.util.*

private const val THE_20TH_HIGHLY_COMPOSITE = 7560

@ArithmeticDsl
val BigInteger.canBeSquare: Boolean get() = SquareBits.hasBit((this % SquareBits.mod).intValueExact())

private object SquareBits {

    val mod = BigInteger.valueOf(THE_20TH_HIGHLY_COMPOSITE.toLong())
    private val bitSet = BitSet(THE_20TH_HIGHLY_COMPOSITE)

    init {
        for(i in 0 until THE_20TH_HIGHLY_COMPOSITE) {
            bitSet.set((i * i) % THE_20TH_HIGHLY_COMPOSITE)
        }
    }

    fun hasBit(i: Int) = bitSet.get(i)
}