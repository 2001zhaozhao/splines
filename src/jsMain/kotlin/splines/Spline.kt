package splines

import SplinesCanvas
import org.w3c.dom.CanvasRenderingContext2D
import kotlin.math.PI

/**
 * A spline controlled by some points and/or other state. The spline starts out empty when first created.
 */
abstract class Spline {
    /**
     * Current list of points in the spline.
     *
     * Not meant to be mutable by public API. Use [addPoint] and [removePoint] to modify points on the spline.
     */
    val points: List<Point> get() = pointsPrivate

    /**
     * The mutable internal list of points in the spline
     */
    private val pointsPrivate = ArrayList<Point>()

    /**
     * Adds a point to the spline.
     *
     * Subclasses can override this to add additional functionality tied to each point.
     */
    open fun addPoint(point: Point) {
        pointsPrivate.add(point)
    }

    /**
     * Removes a point at the given index from the spline.
     *
     * Subclasses can override this to add additional functionality tied to each point.
     */
    open fun removePoint(index: Int) {
        pointsPrivate.removeAt(index)
    }

    /**
     * Removes the given point from the spline or does nothing if the point does not exist.
     */
    fun removePoint(point: Point) {
        val index = points.indexOf(point)
        if(index == -1) return
        removePoint(index)
    }

    /**
     * Step size of t to draw each line segment, 0.001 by default
     */
    open val stepSize: Double get() = 0.001

    /**
     * The maximum T. When t >= this value, the drawing is stopped
     *
     * Note that the value returned by this property could depend on the number of points ([points].size)
     */
    open val maxT: Double get() = 1.0

    /**
     * Calculate either x or y coordinate of curve at specified parameter value
     *
     * Note: will only be called if there are at least two points in the spline, and [maxT] returns a positive value
     */
    abstract fun calculate(isX: Boolean, pointCoords: List<Double>, t: Double): Double

    /**
     * Draws this spline as well as its control points on the canvas
     */
    open fun draw() {
        val canvas = SplinesCanvas.canvas
        val context = canvas.getContext("2d") as CanvasRenderingContext2D
        context.clearRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())

        // Draw curve if at least 2 points
        if(points.size >= 2) {
            val pointsX = points.map{it.x}
            val pointsY = points.map{it.y}

            val maxT = this.maxT
            if(maxT > 0) {
                var t = 0.0
                var x = calculate(true, pointsX, t)
                var y = calculate(false, pointsY, t)

                // Invariant: x = calculate(pointsX, t), y = calculate(pointsY, t)
                val stepSize = this.stepSize

                context.strokeStyle = "red"
                context.beginPath()
                while(t < maxT) {
                    val t2 = t + stepSize
                    val x2 = calculate(true, pointsX, t2.coerceAtMost(maxT))
                    val y2 = calculate(false, pointsY, t2.coerceAtMost(maxT))

                    // Draw line
                    context.moveTo(x,y)
                    context.lineTo(x2,y2)

                    t = t2
                    x = x2
                    y = y2
                }
                context.stroke()
            }
        }

        // Draw control points
        context.strokeStyle = "black"

        for(i in points.indices) {
            val point = points[i]
            context.beginPath()
            if(this is WeightedSpline) {
                // WeightedSpline point color depends on current point weight
                val weight = weights[i]
                context.strokeStyle = if(weight <= 1) "rgba(${(weight*255).toInt()},0,0,1)"
                    else if(weight <= 5) "rgba(255,${((weight-1)/4*255).toInt()},0,1)"
                    else if(weight <= 10) "rgba(${((10-weight)/5*255).toInt()},255,0,1)"
                    else if(weight <= 15) "rgba(0,255,${((weight-10)/5*255).toInt()},1)"
                    else if(weight <= 20) "rgba(0,${((20-weight)/5*255).toInt()},255,1)"
                    else "rgba(0,0,255,1)"
                // Also draw weight as text
                context.fillText(weight.toString(), point.x, point.y - 10.0)
            }
            context.arc(point.x, point.y, 2.0, 0.0, PI * 2, true)
            context.stroke()
        }

        // Draw lines between control points
        context.strokeStyle = "black"
        context.beginPath()
        for(i in 0 until points.size - 1) {
            val point1 = points[i]
            val point2 = points[i+1]
            context.moveTo(point1.x,point1.y)
            context.lineTo(point2.x,point2.y)
        }
        context.stroke()
    }
}