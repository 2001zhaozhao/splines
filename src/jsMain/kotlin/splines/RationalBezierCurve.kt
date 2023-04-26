package splines

/**
 * A rational Bezier curve controlled by a set of points.
 * It is similar to [BernsteinBezierCurve] but also takes a weight value of each point into account.
 */
class RationalBezierCurve : WeightedSpline() {
    override fun calculate(isX: Boolean, pointCoords: List<Double>, t: Double): Double {
        // Gets the Bernstein polynomial value of each point
        // (i.e. how each point would be weighted in a regular Bernstein Bezier curve)
        val bernsteinValues = BernsteinBezierCurve.getBernsteinWeights(pointCoords, t)

        // Find total weight (this is the denominator)
        // Note that the weight of each point is the bernstein polynomial value times the weight parameter
        var totalWeight = 0.0
        for(i in pointCoords.indices) {
            totalWeight += weights[i] * bernsteinValues[i]
        }

        // Find weighted average of points
        var result = 0.0
        for(i in pointCoords.indices) {
            result += weights[i] * bernsteinValues[i] * pointCoords[i]
        }
        return result / totalWeight
    }
}