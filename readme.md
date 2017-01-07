#Modular Arithmetik

[![GitHub license](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![Repository Size](https://reposs.herokuapp.com/?path=penemue/modular-arithmetik)

Tiny [Kotlin](https//kotlinlang.org) DSL for [modular arithmetik](https://en.wikipedia.org/wiki/Modular_arithmetic).
Lets you use math-like notations in number theoretic algorithms: congruence relation `a mod N` instead of
`a % N` and `gcd(a, b)` instead of `a.gcd(b)`.

E.g., [Pollard's Monte Carlo *rho*-method for integer factorization](https://en.wikipedia.org/wiki/Pollard%27s_rho_algorithm)
written in Kotlin looks for `N: BigInteger` as follows:
```kotlin
val one = BigInteger.ONE
var x1 = one
var x2 = one
var product = one
repeat(Int.MAX_VALUE, {
    x1 = x1 * x1 + 2 mod N
    x2 = x2 * x2 + 2 mod N
    x2 = x2 * x2 + 2 mod N
    product = product * (x2 - x1 mod N) mod N
    if (it % 1000 == 0) {
        val gcd = gcd(product, N)
        if (gcd != one) {
            println("\Factor found: $gcd, iterations: $it")
            return
        }
    }
})
```

You can also express modular exponentiation as `base exp e mod M`. E.g., a variant of the single-stage 
[Pollard's P - 1 method for integer factorization](https://en.wikipedia.org/wiki/Pollard%27s_p_%E2%88%92_1_algorithm)
written in Kotlin looks for `N: BigInteger` as follows: 
```kotlin
val one = BigInteger.ONE
var base = BigInteger.TEN
var product = one
repeat(Int.MAX_VALUE, {
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
})
```

Modular exponentiation expression does not invoke `BigInteger.modPow()`, it uses its own
implementation of exponentiation without divisions using [Barrett reduction](https://en.wikipedia.org/wiki/Barrett_reduction).
As `BigInteger.modPow()` uses [Montgomery multiplication](https://en.wikipedia.org/wiki/Montgomery_modular_multiplication),
it's interesting to compare these reduction methods. It can be done implicitly by a benchmark for modular
exponentiation. For random 2048-bit moduli and 2000-bit exponents, the results are as follows:
```
Benchmark                                      Mode  Cnt   Score   Error  Units
ModularExponentiationBenchmark.barrettExp     thrpt   20  66.158 ± 0.861  ops/s
ModularExponentiationBenchmark.montgomeryExp  thrpt   20  55.026 ± 2.122  ops/s
```

To get benchmark results in your environment, run:

    ./gradlew clean jar jmh
    
Even though current implementation of Barrett reduction is rather ad hoc, modular exponentiation offered by the
`base exp e mod M` expression seems to be 15-20% faster than the one offered by JDK. This result was repeated for
random 512-bit and 1024-bit moduli as well.

## References

- [An improved Monte Carlo factorization algorithm](http://maths-people.anu.edu.au/~brent/pd/rpb051i.pdf)

> R. P. Brent, An improved Monte Carlo factorization algorithm, BIT 20 (1980), 176-184

- [Fast Modular Reduction](https://www.lirmm.fr/arith18/papers/hasenplaugh-FastModularReduction.pdf)

> W. Hasenplaugh, G. Gaubatz and V. Gopal, Fast Modular Reduction, 18th IEEE Symposium on Computer Arithmetic 2007