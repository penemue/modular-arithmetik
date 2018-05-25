/**
 * Copyright 2016 - 2018 Vyacheslav Lukianov (https://github.com/penemue)
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

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.math.BigInteger
import java.util.*
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.SECONDS)
abstract class ModularExponentiationBenchmark(private val m: BigInteger) {

    protected companion object Rnd : Random()

    private var exp: BigInteger = BigInteger.ZERO

    @Setup(Level.Invocation)
    fun prepare() {
        // exponent is a random integer with length adjusted to modulus' length
        exp = BigInteger(m.bitLength - 1, Rnd)
        ModularDivision.clearReductionsCache()
    }

    @Benchmark
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 5, time = 1)
    @Fork(4)
    fun montgomeryExp(bh: Blackhole) {
        bh.consume(BigInteger.TEN.modPow(exp, m))
    }

    @Benchmark
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 5, time = 1)
    @Fork(4)
    fun barrettExp(bh: Blackhole) {
        bh.consume(BigInteger.TEN exp exp mod m)
    }
}

open class RandomModulusExponentiationBenchmark : ModularExponentiationBenchmark(
        // modulus is a 2048-bit random very probable prime number
        BigInteger(2048, 100, Rnd))

/**
 * Exponentiation benchmark modulo F11, the 11th Fermat number (2^2048 + 1)
 */
open class FermatModulusExponentiationBenchmark : ModularExponentiationBenchmark((BigInteger.ONE shl 2048) + 1)

/**
 * Exponentiation benchmark modulo M2053, the 2053rd Mersenne number (2^2053 - 1)
 */
open class MersenneModulusExponentiationBenchmark : ModularExponentiationBenchmark((BigInteger.ONE shl 2053) - 1)