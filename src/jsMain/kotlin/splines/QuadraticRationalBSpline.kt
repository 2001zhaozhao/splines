package splines

/**
 * Quadratic rational B-Spline with uniformly spaced knots.
 *
 * With all weights equal to one, this is the quadratic B-Spline,
 * which behaves like the [QuadraticBezierSpline].
 */
class QuadraticRationalBSpline : WeightedSpline() {
    // maxT=1 when there are three points, and each additional point adds another segment and hence adds 1 to maxT
    override val maxT: Double get() = (points.size - 2).toDouble()

    override fun calculate(isX: Boolean, pointCoords: List<Double>, t: Double): Double {
        // The index of the first control point affecting this curve segment, eg. segment = 0 -> curve starts at P0
        // Special case: t = maxT, in which case we cap the segment count at the max segment to avoid array overflow
        val segment = t.toInt().coerceAtMost(pointCoords.size-3)

        // There are three segments that can contribute to the point weights at this value of t
        // e.g. 0 < t < 1 -> segments -2, -1, 0
        val segmentOne = segment - 2
        val tOne = t - segmentOne
        val weightOne = (3-tOne) * (3-tOne) // Third piece of the segment

        val segmentTwo = segment - 1
        val tTwo = t - segmentTwo
        val weightTwo = tTwo * tTwo - 3 * (tTwo-1) * (tTwo-1) // Second piece of the segment

        val segmentThree = segment
        val tThree = t - segmentThree
        val weightThree = tThree * tThree // First piece of the segment

        // B-Spline parameter points
        val p0 = pointCoords[segment]
        val p1 = pointCoords[segment+1]
        val p2 = pointCoords[segment+2]

        // Multiply by point weight parameters to get final weight for NURBS
        val w0 = weightOne * weights[segment]
        val w1 = weightTwo * weights[segment+1]
        val w2 = weightThree * weights[segment+2]

        // Return weighted average of the points
        return (p0 * w0 + p1 * w1 + p2 * w2) / (w0 + w1 + w2)
    }

}