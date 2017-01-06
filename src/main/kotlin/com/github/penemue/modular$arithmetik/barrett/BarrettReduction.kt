package com.github.penemue.`modular$arithmetik`.barrett

import com.github.penemue.`modular$arithmetik`.shl
import com.github.penemue.`modular$arithmetik`.shr
import java.math.BigInteger
import java.util.concurrent.ConcurrentHashMap

object BarrettReduction {

    private val inverses = ConcurrentHashMap<BigInteger, BigInteger>()

    /**
     * @return `n` % `m` using Barrett reduction algorithm (https://en.wikipedia.org/wiki/Barrett_reduction)
     */
    fun barrettRemainder(n: BigInteger, m: BigInteger): BigInteger {
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