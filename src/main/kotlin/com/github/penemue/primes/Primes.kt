/**
 * Copyright 2016 - 2022 Vyacheslav Lukianov (https://github.com/penemue)
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
package com.github.penemue.primes

import java.util.*

fun first1MPrimes(): Iterable<Int> = Iterable {
    object : Iterator<Int> {

        var bit = -1

        override fun hasNext() = bit.bitToPrime <= 15485863 && bit < Eratosthenes.bitSet.size()

        override fun next() = (if (bit < 0) 2 else bit.bitToPrime).also {
            if (++bit < Eratosthenes.bitSet.size()) {
                bit = Eratosthenes.bitSet.nextClearBit(bit)
            }
        }
    }
}

// sieving 1 million of odd primes
private object Eratosthenes {

    val bitSet = BitSet(15485863.primeToBit + 1)

    init {
        var bit = -1
        while (true) {
            bit = bitSet.nextClearBit(bit + 1)
            val prime = bit.bitToPrime
            var sqr = prime * prime
            if (sqr.primeToBit >= bitSet.size()) break
            do {
                bitSet.set(sqr.primeToBit)
                sqr += prime
                sqr += prime
            } while (sqr.primeToBit < bitSet.size())
        }
    }
}

private val Int.primeToBit: Int get() = if ((this and 1) == 1) this / 2 - 1 else throw IllegalArgumentException("Odd integer expected")
private val Int.bitToPrime: Int get() = (this + 1) * 2 + 1
