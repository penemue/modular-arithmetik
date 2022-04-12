package com.github.penemue.primes

import java.io.InputStreamReader
import java.util.*

fun first1MPrimes(): List<Int> {
    InputStreamReader(Any().javaClass.getResourceAsStream("/first1MPrimes.txt"))
    Scanner(Any().javaClass.getResourceAsStream("/first1MPrimes.txt")).use { scanner ->
        return ArrayList<Int>().apply {
            while (scanner.hasNextInt()) {
                add(scanner.nextInt())
            }
        }
    }
}