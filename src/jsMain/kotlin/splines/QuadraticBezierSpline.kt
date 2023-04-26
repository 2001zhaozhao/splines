package splines

/**
 * A piecewise quadratic Bezier spline with C1 continuity between segments,
 * and each segment is controlled by three De Boor points
 */
class QuadraticBezierSpline : Spline() {
    // maxT=1 when there are three points, and each additional point adds another segment and hence adds 1 to maxT
    override val maxT: Double get() = (points.size - 2).toDouble()

    override fun calculate(isX: Boolean, pointCoords: List<Double>, t: Double): Double {
        // The index of the first control point affecting this curve segment, eg. segment = 0 -> curve starts at P0
        // Special case: t = maxT, in which case we cap the segment count at the max segment to avoid array overflow
        val segment = t.toInt().coerceAtMost(pointCoords.size-3)

        // De Boor points
        val p0 = pointCoords[segment]
        val p1 = pointCoords[segment+1]
        val p2 = pointCoords[segment+2]

        // Bezier points
        val b0 = (p0 + p1) / 2
        val b1 = p1
        val b2 = (p1 + p2) / 2

        // Return point on quadratic Bezier curve controlled by (b0, b1, b2)
        val u = t - segment
        return b0 * (1-u) * (1-u) + b1 * u * (1-u) * 2 + b2 * u * u
    }
}