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
exponentiation.

## Benchmarks

The project ships a JMH suite ([`src/jmh/kotlin`](src/jmh/kotlin)) that measures throughput of two modular
exponentiation flavors against three different 2048-/2053-bit moduli:

| Benchmark      | What is measured                                                          |
|----------------|---------------------------------------------------------------------------|
| `barrettExp`   | `BigInteger.TEN exp e mod M` — own implementation based on Barrett reduction |
| `montgomeryExp`| `BigInteger.TEN.modPow(e, M)` — JDK's built-in Montgomery multiplication  |

The exponent `e` is a fresh random integer of length `M.bitLength - 1` regenerated on every invocation
(`Level.Invocation`), and Barrett's reduction cache is cleared before each invocation to keep the comparison
fair. Each benchmark runs `5 warmup + 5 measurement` iterations across `4 forks` (20 samples total, throughput
mode, units: `ops/s`).

The three benchmarked moduli are:

- **Random** — a random 2048-bit very probable prime; the typical RSA-style case where the modulus has no
  special structure.
- **Fermat F11** — the 11th Fermat number `2^2048 + 1`. Composite, with a few small special-form factors.
- **Mersenne M2053** — `2^2053 − 1`. Composite, very close to a power of two, which is the best case for
  reduction-by-shift tricks inside JDK's Montgomery implementation.

Results (Kotlin 2.3.21, JDK 17, Gradle 9.5.1, JMH 1.37):

```
Benchmark                                              Mode  Cnt    Score    Error  Units
RandomModulusExponentiationBenchmark.barrettExp       thrpt   20   83.212 ±  9.395  ops/s
RandomModulusExponentiationBenchmark.montgomeryExp    thrpt   20  211.575 ± 25.176  ops/s
FermatModulusExponentiationBenchmark.barrettExp       thrpt   20  410.544 ± 22.540  ops/s
FermatModulusExponentiationBenchmark.montgomeryExp    thrpt   20  370.912 ± 16.854  ops/s
MersenneModulusExponentiationBenchmark.barrettExp     thrpt   20  433.751 ±  9.880  ops/s
MersenneModulusExponentiationBenchmark.montgomeryExp  thrpt   20  206.651 ± 33.041  ops/s
```

### Interpretation

- **Random 2048-bit modulus** — JDK's `modPow` is **~2.5× faster** (211.6 vs 83.2 ops/s). On generic moduli
  the heavily-tuned, native-accelerated Montgomery path inside `BigInteger.modPow()` clearly dominates the
  pure-Kotlin Barrett implementation. This is the realistic case for cryptographic workloads such as RSA.
- **Fermat F11 (`2^2048 + 1`)** — Barrett is **~11% faster** (410.5 vs 370.9 ops/s). The modulus has a
  near-power-of-two shape, which lets the precomputed Barrett quotient stay extremely simple; the
  reduction-cache amortizes well over the exponent loop.
- **Mersenne M2053 (`2^2053 − 1`)** — Barrett is **~2.1× faster** (433.8 vs 206.7 ops/s). Reduction modulo
  `2^k − 1` reduces to add-and-shift, which Barrett's path exploits naturally; the JDK's Montgomery path
  carries fixed setup/teardown costs that don't pay off here.

**Takeaway**: `base exp e mod M` is a good fit when `M` has special structure (near `2^k ± 1`, sparse, etc.),
or when many exponentiations share the same modulus and the Barrett reduction cache can be reused. For
generic large random moduli, JDK's `BigInteger.modPow()` remains the better choice.

To reproduce the benchmark in your environment, run:

```bash
./gradlew clean jar jmh
```

## References

- [An improved Monte Carlo factorization algorithm](http://maths-people.anu.edu.au/~brent/pd/rpb051i.pdf)

> R. P. Brent, An improved Monte Carlo factorization algorithm, BIT 20 (1980), 176-184

- [Fast Modular Reduction](https://www.lirmm.fr/arith18/papers/hasenplaugh-FastModularReduction.pdf)

> W. Hasenplaugh, G. Gaubatz and V. Gopal, Fast Modular Reduction, 18th IEEE Symposium on Computer Arithmetic 2007
