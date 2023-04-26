import kotlinx.browser.document
import react.create
import react.dom.client.createRoot
import splines.BezierCurve
import splines.Point
import splines.Spline
import splines.WeightedSpline

/**
 * The current spline to draw
 */
var currentSpline: Spline = BezierCurve().apply{
    addPoint(Point(300.0, 150.0))
    addPoint(Point(210.0, 240.0))
    addPoint(Point(420.0, 360.0))
}

fun main() {
    val canvas = SplinesCanvas.canvas
    // Click handler on canvas
    canvas.onclick = {e ->
        currentSpline.addPoint(Point(e.offsetX, e.offsetY))
        currentSpline.draw()
        Unit
    }
    // Prevent the context menu and set right click to remove point
    canvas.oncontextmenu = {e ->
        e.preventDefault()

        if(currentSpline.points.isNotEmpty()) {
            val nearest = currentSpline.points.minBy {
                val x = it.x - e.offsetX
                val y = it.y - e.offsetY
                x * x + y * y
            }
            val x = nearest.x - e.offsetX
            val y = nearest.y - e.offsetY
            // Can only remove point if within 10 pixels to the click
            if(x * x + y * y < 10.0 * 10.0) {
                currentSpline.removePoint(nearest)
                currentSpline.draw()
            }
        }
    }
    // Capture mouse scroll when weighted spline is detected
    canvas.onwheel = {e ->
        val spline = currentSpline
        if(spline is WeightedSpline) {
            e.preventDefault()

            if(spline.points.isNotEmpty()) {
                val nearest = spline.points.minBy {
                    val x = it.x - e.offsetX
                    val y = it.y - e.offsetY
                    x * x + y * y
                }
                val x = nearest.x - e.offsetX
                val y = nearest.y - e.offsetY
                // Can only remove point if within 10 pixels to the click
                if(x * x + y * y < 10.0 * 10.0) {
                    val index = spline.points.indexOf(nearest)
                    val change = if(e.deltaY < 0) 0.25 else -0.25
                    spline.weights[index] = (spline.weights[index] + change).coerceIn(0.0..20.0)
                    spline.draw()
                }
            }
        }
    }

    // Draw starting curve
    currentSpline.draw()

    val container = document.createElement("div")
    document.body!!.appendChild(container)

    val splinesControls = SplinesControls.create {
        splineType = "bezier"
    }
    createRoot(container).render(splinesControls)
}