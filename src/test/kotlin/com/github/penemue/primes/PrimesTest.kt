package com.github.penemue.primes

import org.junit.Assert.assertEquals
import org.junit.Test

class PrimesTest {

    @Test
    fun testFirst1MPrimes() {
        val primes = first1MPrimes()
        assertEquals(1000000, primes.size)
        assertEquals(2, primes.first())
        assertEquals(15485863, primes.last())
    }
}