package splines

/**
 * A spline where each control point is weighted, e.g. rational Bezier curves and NURBS
 */
abstract class WeightedSpline : Spline() {
    /**
     * Stores the weights of each point.
     */
    val weights = ArrayList<Double>()

    /**
     * Adds a point to this [WeightedSpline] and sets its initial weight to 1.0.
     */
    override fun addPoint(point: Point) {
        super.addPoint(point)
        weights += 1.0
    }

    /**
     * Removes a point and its weight value from this [WeightedSpline].
     */
    override fun removePoint(index: Int) {
        super.removePoint(index)
        weights.removeAt(index)
    }
}