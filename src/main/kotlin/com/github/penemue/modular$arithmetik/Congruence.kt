package com.github.penemue.`modular$arithmetik`

import com.github.penemue.`modular$arithmetik`.barrett.BarrettReduction
import java.math.BigInteger

val BigInteger.isNegative: Boolean get() = this.signum() < 0

operator fun BigInteger.plus(i: Long) = this + BigInteger.valueOf(i)

operator fun BigInteger.minus(i: Long) = this - BigInteger.valueOf(i)

infix fun BigInteger.mod(m: BigInteger): BigInteger = if (this.isNegative) (this + m) mod m else BarrettReduction.barrettRemainder(this, m)

fun gcd(a: BigInteger, b: BigInteger) = a.gcd(b)

infix fun BigInteger.shr(shift: Int) = this.shiftRight(shift)

infix fun BigInteger.shl(shift: Int) = this.shiftLeft(shift)