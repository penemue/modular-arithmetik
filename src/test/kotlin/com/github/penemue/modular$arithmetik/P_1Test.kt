package com.github.penemue.`modular$arithmetik`

import org.junit.Test
import java.math.BigInteger

class P_1Test {

    /**
     * Single stage variant of Pollard's P - 1 method for integer factorization
     * (https://en.wikipedia.org/wiki/Pollard%27s_p_%E2%88%92_1_algorithm) applied to M101,
     * the 101st Mersenne number (https://en.wikipedia.org/wiki/Mersenne_prime).
     */
    @Test
    fun factorM101() {
        val M101 = (BigInteger.ONE shl 101) - 1
        val one = BigInteger.ONE
        var base = BigInteger.TEN
        var product = one
        println("Factoring $M101:")
        repeat(Int.MAX_VALUE, {
            if (it > 1) {
                base = base exp it mod M101
                product = (base - 1) * product mod M101
                if (it % 10 == 0) {
                    val gcd = gcd(product, M101)
                    if (gcd != one) {
                        println("\nM101 factor found: $gcd, iterations: $it")
                        return
                    }
                }
            }
        })
    }
}