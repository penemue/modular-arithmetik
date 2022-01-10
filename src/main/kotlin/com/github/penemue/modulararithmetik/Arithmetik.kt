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
package com.github.penemue.modulararithmetik

import java.math.BigInteger

@DslMarker
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class ArithmeticDsl

@ArithmeticDsl
val Long.toBigInteger: BigInteger
    get() = BigInteger.valueOf(this)

@ArithmeticDsl
val Int.toBigInteger: BigInteger
    get() = BigInteger.valueOf(this.toLong())

@ArithmeticDsl
val BigInteger.isNegative: Boolean
    get() = this.signum() < 0

@ArithmeticDsl
val BigInteger.bitLength: Int
    get() = this.bitLength()

@ArithmeticDsl
val BigInteger.isZero: Boolean
    get() = this == BigInteger.ZERO

@ArithmeticDsl
operator fun BigInteger.plus(i: Long) = this + BigInteger.valueOf(i)

@ArithmeticDsl
operator fun BigInteger.minus(i: Long) = this - BigInteger.valueOf(i)

@ArithmeticDsl
infix fun BigInteger.mod(m: BigInteger): BigInteger = if (this.isNegative) (this + m) mod m else ModularDivision.mod(this, m)

@ArithmeticDsl
fun gcd(a: BigInteger, b: BigInteger): BigInteger = a.gcd(b)

@ArithmeticDsl
fun gcd(a: Long, b: BigInteger): BigInteger = b.gcd(BigInteger.valueOf(a))

@ArithmeticDsl
fun gcd(a: BigInteger, b: Long): BigInteger = a.gcd(BigInteger.valueOf(b))

@ArithmeticDsl
fun gcd(a: Int, b: BigInteger): BigInteger = gcd(a.toLong(), b)

@ArithmeticDsl
fun gcd(a: BigInteger, b: Int): BigInteger = gcd(a, b.toLong())

@ArithmeticDsl
infix fun BigInteger.and(that: BigInteger): BigInteger = this.and(that)

@ArithmeticDsl
infix fun BigInteger.or(that: BigInteger): BigInteger = this.or(that)

@ArithmeticDsl
infix fun BigInteger.xor(that: BigInteger): BigInteger = this.xor(that)

@ArithmeticDsl
infix fun BigInteger.shr(shift: Int): BigInteger = this.shiftRight(shift)

@ArithmeticDsl
infix fun BigInteger.shl(shift: Int): BigInteger = this.shiftLeft(shift)

@ArithmeticDsl
infix fun BigInteger.inverse_mod(m: BigInteger) = this.modInverse(m)

@ArithmeticDsl
infix fun Long.inverse_mod(m: BigInteger) = BigInteger.valueOf(this).modInverse(m)

@ArithmeticDsl
infix fun Int.inverse_mod(m: BigInteger) = this.toLong() inverse_mod m

@ArithmeticDsl
fun sqrt(i: BigInteger): BigInteger {
    when (i.signum()) {
        -1 -> throw ArithmeticException("Cannot return imaginary number")
        0 -> return BigInteger.ZERO
    }
    var low = BigInteger.ONE shl (i.bitLength - 1) / 2
    var high = low shl 1
    while (low < high) {
        val mid = (low + high) shr 1
        when ((mid * mid).compareTo(i)) {
            0 -> return mid
            1 -> high = mid
            -1 -> {
                if (low == mid) return mid
                low = mid
            }
        }
    }
    return low
}

@ArithmeticDsl
fun productOf(vararg integers: BigInteger): BigInteger {
    var result = BigInteger.ONE
    integers.forEach { result *= it }
    return result
}

@ArithmeticDsl
fun productOf(vararg integers: Int): BigInteger {
    var result = BigInteger.ONE
    integers.forEach { result *= BigInteger.valueOf(it.toLong()) }
    return result
}

@ArithmeticDsl
infix fun BigInteger.exp(exp: BigInteger): Exponent {
    return Exponent(this, exp)
}

@ArithmeticDsl
infix fun BigInteger.exp(exp: Long): Exponent {
    return exp(BigInteger.valueOf(exp))
}

@ArithmeticDsl
infix fun BigInteger.exp(exp: Int): Exponent {
    return exp(exp.toLong())
}

/**
 * Container of the exponent expression which then should be reduced by the mod infix function
 */
data class Exponent(val base: BigInteger, val exp: BigInteger)

@ArithmeticDsl
infix fun Exponent.mod(m: BigInteger): BigInteger {
    var result = BigInteger.ONE
    val expLen = exp.bitLength
    for (i in 1..expLen) {
        result = (result * result) mod m
        if (exp.testBit(expLen - i)) {
            result = (result * base) mod m
        }
    }
    return result
}