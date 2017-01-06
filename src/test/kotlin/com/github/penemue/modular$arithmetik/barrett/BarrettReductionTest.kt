package com.github.penemue.`modular$arithmetik`.barrett

import org.junit.Assert
import org.junit.Test
import java.math.BigInteger

class BarrettReductionTest {

    @Test
    fun barrett() {
        val pi = BigInteger.valueOf(31415926L)
        val e = BigInteger.valueOf(271828L)
        Assert.assertEquals(pi % e, BarrettReduction.barrettRemainder(pi, e))
        Assert.assertEquals(BigInteger.ONE % e, BarrettReduction.barrettRemainder(BigInteger.ONE, e))
        Assert.assertEquals(BigInteger.TEN % e, BarrettReduction.barrettRemainder(BigInteger.TEN, e))
    }
}