package splines

/**
 * A bezier curve implemented using De Casteljau's algorithm
 */
class BezierCurve : Spline() {
    override fun calculate(isX: Boolean, pointCoords: List<Double>, t: Double): Double {
        // Convert to mutable array
        val coords = pointCoords.toDoubleArray()

        // Cascade down until only 1 value remaining
        var targetSize = coords.size - 1
        while(targetSize >= 1) {
            for(i in 0 until targetSize) {
                // De Casteljau's algorithm step here
                // P = (1-t) * P1 + t * P2
                coords[i] = coords[i] * (1-t) + coords[i+1] * t
            }
            targetSize--
        }

        // Return final coordinate
        return coords[0]
    }
}