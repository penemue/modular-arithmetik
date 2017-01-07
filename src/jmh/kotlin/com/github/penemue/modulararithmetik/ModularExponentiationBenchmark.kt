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

import com.github.penemue.`modular$arithmetik`.exp
import com.github.penemue.`modular$arithmetik`.mod
import com.github.penemue.`modular$arithmetik`.plus
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.math.BigInteger
import java.util.*
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.SECONDS)
open class ModularExponentiationBenchmark {

    companion object Rnd : Random()

    // module is a 2048-bit random very probably prime number
    private val m = BigInteger(2048, 100, Rnd)
    // exponent is a 2000-bit random number
    private var exp = BigInteger(2000, Rnd)

    @Setup(Level.Invocation)
    fun prepare() {
        exp += 1
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