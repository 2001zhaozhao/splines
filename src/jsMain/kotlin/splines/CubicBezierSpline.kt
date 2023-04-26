package splines

/**
 * A piecewise cubic Bezier spline with C2 continuity between segments,
 * and each segment is controlled by four De Boor points
 */
class CubicBezierSpline : Spline() {
    // maxT=1 when there are four points, and each additional point adds another segment and hence adds 1 to maxT
    override val maxT: Double get() = (points.size - 3).toDouble()

    override fun calculate(isX: Boolean, pointCoords: List<Double>, t: Double): Double {
        // The index of the first control point affecting this curve segment, eg. segment = 0 -> curve starts at P0
        // Special case: t = maxT, in which case we cap the segment count at the max segment to avoid array overflow
        val segment = t.toInt().coerceAtMost(pointCoords.size-4)

        // De Boor points
        val p0 = pointCoords[segment]
        val p1 = pointCoords[segment+1]
        val p2 = pointCoords[segment+2]
        val p3 = pointCoords[segment+3]

        // Bezier points
        val b0 = (p0 + p1 * 4 + p2) / 6
        val b1 = (p1 * 2 + p2) / 3
        val b2 = (p1 + p2 * 2) / 3
        val b3 = (p1 + p2 * 4 + p3) / 6

        // Return point on cubic Bezier curve controlled by (b0, b1, b2, b3)
        val u = t - segment
        return b0 * (1-u) * (1-u) * (1-u) + b1 * u * (1-u) * (1-u) * 3 + b2 * u * u * (1-u) * 3 + b3 * u * u * u
    }
}