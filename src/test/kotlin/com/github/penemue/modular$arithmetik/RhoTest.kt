package com.github.penemue.`modular$arithmetik`

import org.junit.Test
import java.math.BigInteger

class RhoTest {

    /**
     * Pollard's Monte Carlo *rho*-method for integer factorization applied to F8, the 8th Fermat number.
     * In 1980, a faster version of the algorithm was used to factor F8, and the factorization took 2 hours
     * on Univac 1100/42 ([http://maths-people.anu.edu.au/~brent/pd/rpb061.pdf]).
     * Nowadays it takes ~40 seconds on a commodity computer.
     */
    @Test
    fun factorF8() {
        val F8 = BigInteger.ONE.shiftLeft(256) + 1
        val one = BigInteger.ONE
        var x1 = one
        var x2 = one
        var product = one
        println("Factoring $F8:")
        repeat(Int.MAX_VALUE, {
            x1 = x1 * x1 + 2 mod F8
            x2 = x2 * x2 + 2 mod F8
            x2 = x2 * x2 + 2 mod F8
            product = product * (x2 - x1 mod F8) mod F8
            if (it % 1000 == 0) {
                val gcd = gcd(product, F8)
                if (gcd != one) {
                    println("\nF8 factor found: $gcd, iterations: $it")
                    return
                }
            }
        })
    }
}