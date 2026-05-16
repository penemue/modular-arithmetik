# Modular Arithmetik

[![Apache License 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Pure Kotlin](https://img.shields.io/badge/100%25-kotlin-orange.svg)](https://kotlinlang.org)

Tiny [Kotlin](https://kotlinlang.org) DSL for [modular arithmetic](https://en.wikipedia.org/wiki/Modular_arithmetic).
Lets you use math-like notation in number-theoretic algorithms: modular reduction `a mod N` instead of
`a % N`, and `gcd(a, b)` instead of `a.gcd(b)`.

For example, [Pollard's *ρ*-algorithm for integer factorization](https://en.wikipedia.org/wiki/Pollard%27s_rho_algorithm)
looks for a factor of `N: BigInteger` as follows:
```kotlin
val one = BigInteger.ONE
var x1 = one
var x2 = one
var product = one
repeat(Int.MAX_VALUE) {
    x1 = x1 * x1 + 2 mod N
    x2 = x2 * x2 + 2 mod N
    x2 = x2 * x2 + 2 mod N
    product = product * (x2 - x1 mod N) mod N
    if (it % 1000 == 0) {
        val gcd = gcd(product, N)
        if (gcd != one) {
            println("\nFactor found: $gcd, iterations: $it")
            return
        }
    }
}
```

Modular exponentiation can also be expressed as `base exp e mod M`. For example, a variant of the single-stage
[Pollard's P − 1 method for integer factorization](https://en.wikipedia.org/wiki/Pollard%27s_p_%E2%88%92_1_algorithm)
looks for a factor of `N: BigInteger` as follows:
```kotlin
val one = BigInteger.ONE
var base = BigInteger.TEN
var product = one
repeat(Int.MAX_VALUE) {
    if (it > 1) {
        base = base exp it mod N
        product = (base - 1) * product mod N
        if (it % 10 == 0) {
            val gcd = gcd(product, N)
            if (gcd != one) {
                println("\nFactor found: $gcd, iterations: $it")
                return
            }
        }
    }
}
```

The modular exponentiation expression does not invoke `BigInteger.modPow()`; it uses its own
implementation of exponentiation without divisions, based on [Barrett reduction](https://en.wikipedia.org/wiki/Barrett_reduction).
Since `BigInteger.modPow()` uses [Montgomery multiplication](https://en.wikipedia.org/wiki/Montgomery_modular_multiplication),
it is interesting to compare these reduction methods. This can be done implicitly via a benchmark for modular
exponentiation. For a 2048-bit modulus and a 2000-bit exponent (both random), the results are as follows:
```
Benchmark                                      Mode  Cnt   Score   Error  Units
ModularExponentiationBenchmark.barrettExp     thrpt   20  66.158 ± 0.861  ops/s
ModularExponentiationBenchmark.montgomeryExp  thrpt   20  55.026 ± 2.122  ops/s
```

To get benchmark results in your environment, run:

```bash
./gradlew clean jar jmh
```

Even though the current implementation of Barrett reduction is rather ad hoc, the `base exp e mod M` expression
appears to be 15–20% faster than JDK's `modPow` for random moduli. The same holds for random 512-bit and
1024-bit moduli.

## References

- [An improved Monte Carlo factorization algorithm](http://maths-people.anu.edu.au/~brent/pd/rpb051i.pdf)

> R. P. Brent, An improved Monte Carlo factorization algorithm, BIT 20 (1980), 176-184

- [Fast Modular Reduction](https://www.lirmm.fr/arith18/papers/hasenplaugh-FastModularReduction.pdf)

> W. Hasenplaugh, G. Gaubatz and V. Gopal, Fast Modular Reduction, 18th IEEE Symposium on Computer Arithmetic 2007
