package splines

/**
 * A Bezier curve implemented by applying degree elevation a customizable amount of times.
 *
 * @param degreeElevationTimes Number of times to apply the degree elevation
 */
class DegreeElevationBezierCurve(
    var degreeElevationTimes: Int = 1
) : Spline() {
    override val stepSize: Double get() = 1.0
    override val maxT: Double
        get() = (degreeElevationTimes + points.size).toDouble()

    lateinit var degreeElevatedXs: DoubleArray
    lateinit var degreeElevatedYs: DoubleArray

    // Calculate the degree elevations in-place before drawing
    override fun draw() {
        // Create array
        val n = points.size
        val arraySize = degreeElevationTimes + points.size
        degreeElevatedXs = DoubleArray(arraySize)
        degreeElevatedYs = DoubleArray(arraySize)

        for(i in points.indices) {
            val point = points[i]
            degreeElevatedXs[i] = point.x
            degreeElevatedYs[i] = point.y
        }

        // Perform degree elevation steps
        for(i in 0 until degreeElevationTimes) {
            // Expand from n+i values (n+i-1 degree Bezier curve) to n+i+1 values (n+i degree Bezier curve)
            val startDegree = n + i - 1
            val endDegree = n + i
            // First set the final value in the array
            degreeElevatedXs[startDegree + 1] = degreeElevatedXs[startDegree]
            degreeElevatedYs[startDegree + 1] = degreeElevatedYs[startDegree]
            // Interpolate the rest of the array except for the first value, going backwards at each step
            for(j in (1..startDegree).reversed()) {
                degreeElevatedXs[j] =
                    degreeElevatedXs[j-1] * j / endDegree + degreeElevatedXs[j] * (endDegree - j) / endDegree
                degreeElevatedYs[j] =
                    degreeElevatedYs[j-1] * j / endDegree + degreeElevatedYs[j] * (endDegree - j) / endDegree
            }
        }

        // After degree elevation we should have the points required to draw the Bezier curve
        // Finally call the drawing code
        super.draw()
    }

    override fun calculate(isX: Boolean, pointCoords: List<Double>, t: Double): Double {
        // Simply return the precomputed degree elevation values
        return if(isX) degreeElevatedXs[t.toInt()] else degreeElevatedYs[t.toInt()]
    }
}