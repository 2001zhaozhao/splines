package splines

/**
 * A cubic cardinal spline with uniform parameterization.
 * It uses a tension parameter. If tension equals 0.5, the spline is a Catmull-Rom spline.
 */
class CubicCardinalSpline(val tension: Double) : Spline() {
    // maxT=1 when there are four points, and each additional point adds another segment and hence adds 1 to maxT
    override val maxT: Double get() = (points.size - 3).toDouble()

    override fun calculate(isX: Boolean, pointCoords: List<Double>, t: Double): Double {
        // The index of the first control point affecting this curve segment, eg. segment = 0 -> curve starts at P0
        // Special case: t = maxT, in which case we cap the segment count at the max segment to avoid array overflow
        val segment = t.toInt().coerceAtMost(pointCoords.size-4)

        // Note that in literature these points can be indexed -1,0,1,2 or -2,-1,0,1 instead
        // In the naming used here, cardinal spline segment goes from p1 to p2
        val p0 = pointCoords[segment]
        val p1 = pointCoords[segment+1]
        val p2 = pointCoords[segment+2]
        val p3 = pointCoords[segment+3]

        // Implement the cardinal spline matrix
        val constant = p1
        val linear = -(tension * p0) + (tension * p2)
        val quadratic = (2 * tension * p0) + ((tension - 3) * p1) + ((3 - 2 * tension) * p2) - (tension * p3)
        val cubic = -(tension * p0) + ((2 - tension) * p1) + ((tension - 2) * p2) + (tension * p3)

        // Multiply the above values by powers of u (in range 0 to 1)
        val u = t - segment
        return constant + u * linear + u * u * quadratic + u * u * u * cubic
    }
}