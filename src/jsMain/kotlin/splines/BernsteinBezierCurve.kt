package splines

import kotlinx.js.*

/**
 * A Bezier curve implemented using Bernstein polynomials.
 *
 * This should be the same curve as [BezierCurve] (except numerical precision/range issues)
 * but using a different method of calculating it.
 *
 * Note that this curve begins to really slow down with a lot of points.
 */
class BernsteinBezierCurve : Spline() {
    companion object {
        /**
         * Calculate the factorial of a number into BigInt
         */
        fun factorial(k: Int): BigInt {
            // Constant one
            val one = BigInt(1)

            var factorial = BigInt(1)
            var kBigInt = BigInt(k)
            var kVar = k // Invariant: kVar = kBigInt numerically
            while(kVar > 1) {
                factorial *= kBigInt
                kBigInt -= one
                kVar -= 1
            }

            return factorial
        }

        /**
         * Calculate n choose k of a number into BigInt
         */
        fun nChooseK(n: Int, k: Int): BigInt {
            return factorial(n) / factorial(k) / factorial(n-k)
        }

        /**
         * Gets an array of weights for each point calculated from the Bernstein polynomials of degree n (n = `size - 1`)
         */
        fun getBernsteinWeights(pointCoords: List<Double>, t: Double): DoubleArray {
            // Bernstein polynomial degree = size - 1
            // since there are n+1 Bernstein polynomials of degree n
            val n = pointCoords.size - 1
            val weights = DoubleArray(n+1)
            for(i in pointCoords.indices) {
                // Bernstein(n,i)(t) = (n choose i) * t ^ i * (1-t) ^ (n-i)
                val tPower = i
                val oneMinusTPower = n-i

                var value = 1.0
                for(j in 1..tPower) value *= t
                for(j in 1..oneMinusTPower) value *= 1-t

                // Multiply by the factorial (converting BigInt to Double is kind of complicated...)
                @Suppress("UNUSED_VARIABLE")
                val nChooseI = nChooseK(n, i)
                value *= js("Number(nChooseI)") as Double

                weights[i] = value
            }
            return weights
        }
    }

    override fun calculate(isX: Boolean, pointCoords: List<Double>, t: Double): Double {
        // Gets the weight of each point from the Bernstein polynomial
        val weights = getBernsteinWeights(pointCoords, t)

        // Get weighted-average of points
        var result = 0.0
        for(i in pointCoords.indices) {
            result += weights[i] * pointCoords[i]
        }
        return result
    }

}