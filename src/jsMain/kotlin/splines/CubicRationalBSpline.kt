package splines

/**
 * Cubic rational B-Spline with uniformly spaced knots.
 *
 * With all weights equal to one, this is the cubic B-Spline,
 * which behaves like the [CubicBezierSpline].
 */
class CubicRationalBSpline : WeightedSpline() {
    // maxT=1 when there are four points, and each additional point adds another segment and hence adds 1 to maxT
    override val maxT: Double get() = (points.size - 3).toDouble()

    override fun calculate(isX: Boolean, pointCoords: List<Double>, t: Double): Double {
        // The index of the first control point affecting this curve segment, eg. segment = 0 -> curve starts at P0
        // Special case: t = maxT, in which case we cap the segment count at the max segment to avoid array overflow
        val segment = t.toInt().coerceAtMost(pointCoords.size-4)

        // There are four segments that can contribute to the point weights at this value of t
        // e.g. 0 < t < 1 -> segments -3, -2, -1, 0
        val segmentOne = segment - 3
        val tOne = t - segmentOne
        // Fourth piece of the segment
        val weightOne = (4-tOne) * (4-tOne) * (4-tOne)

        val segmentTwo = segment - 2
        val tTwo = t - segmentTwo
        // Third piece of the segment
        val weightTwo = (4-tTwo) * (4-tTwo) * (4-tTwo) - 4 * ((4-tTwo)-1) * ((4-tTwo)-1) * ((4-tTwo)-1)

        val segmentThree = segment - 1
        val tThree = t - segmentThree
        // Second piece of the segment
        val weightThree = tThree * tThree * tThree - 4 * (tThree-1) * (tThree-1) * (tThree-1)

        val segmentFour = segment
        val tFour = t - segmentFour
        // First piece of the segment
        val weightFour = tFour * tFour * tFour

        // B-Spline parameter points
        val p0 = pointCoords[segment]
        val p1 = pointCoords[segment+1]
        val p2 = pointCoords[segment+2]
        val p3 = pointCoords[segment+3]

        // Multiply by point weight parameters to get final weight for NURBS
        val w0 = weightOne * weights[segment]
        val w1 = weightTwo * weights[segment+1]
        val w2 = weightThree * weights[segment+2]
        val w3 = weightFour * weights[segment+3]

        // Return weighted average of the points
        return (p0 * w0 + p1 * w1 + p2 * w2 + p3 * w3) / (w0 + w1 + w2 + w3)
    }

}