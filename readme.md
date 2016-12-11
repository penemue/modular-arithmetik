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