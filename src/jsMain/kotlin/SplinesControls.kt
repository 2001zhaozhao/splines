import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.input
import react.useState
import splines.*

external interface SplinesControlsProps : Props {
    var splineType: String
}

val SplinesControls = FC {props: SplinesControlsProps ->
    var splineType by useState(props.splineType)

    /**
     * A radio button for each spline type
     */
    fun splineRadio(displayName: String, splineType1: String) {
        input {
            type = InputType.radio
            value = splineType1
            checked = splineType == splineType1
            onClick = {
                splineType = splineType1
                println("Switched spline type to $displayName")
                updateSpline(splineType1)
            }
        }
        +displayName
    }

    splineRadio("De Casteljau's Bezier Curve", "bezier")
    splineRadio("Bernstein Bezier Curve", "bernsteinBezier")
    splineRadio("Degree Elevation x1", "degreeElevationX1")
    splineRadio("DE x2", "degreeElevationX2")
    splineRadio("DE x5", "degreeElevationX5")
    splineRadio("DE x10", "degreeElevationX10")
    splineRadio("DE x100", "degreeElevationX100")
    splineRadio("DE x1000", "degreeElevationX1000")
    splineRadio("Quadratic Bezier Spline", "quadraticBezierSpline")
    splineRadio("Cubic Bezier Spline", "cubicBezierSpline")
    splineRadio("Rational Bezier Curve", "rationalBezierCurve")
    splineRadio("Quadratic Rational B-Spline", "quadraticRationalBSpline")
    splineRadio("Cubic Rational B-Spline", "cubicRationalBSpline")
    splineRadio("Cubic Cardinal Spline τ=0.2", "cubicCardinalSpline0_2")
    splineRadio("CCS τ=0.35", "cubicCardinalSpline0_35")
    splineRadio("CCS τ=0.5 (Catmull-Rom)", "cubicCardinalSpline0_5")
    splineRadio("CCS τ=0.65", "cubicCardinalSpline0_65")
    splineRadio("CCS τ=0.8", "cubicCardinalSpline0_8")
    splineRadio("CCS τ=1", "cubicCardinalSpline1")
    splineRadio("CCS τ=2", "cubicCardinalSpline2")
}

/**
 * Update spline displayed in the canvas with a new spline type
 */
fun updateSpline(splineType: String) {
    when(splineType) {
        "bezier" -> {
            currentSpline = BezierCurve()
        }
        "bernsteinBezier" -> {
            currentSpline = BernsteinBezierCurve()
        }
        "degreeElevationX1" -> {
            currentSpline = DegreeElevationBezierCurve(1)
        }
        "degreeElevationX2" -> {
            currentSpline = DegreeElevationBezierCurve(2)
        }
        "degreeElevationX5" -> {
            currentSpline = DegreeElevationBezierCurve(5)
        }
        "degreeElevationX10" -> {
            currentSpline = DegreeElevationBezierCurve(10)
        }
        "degreeElevationX100" -> {
            currentSpline = DegreeElevationBezierCurve(100)
        }
        "degreeElevationX1000" -> {
            currentSpline = DegreeElevationBezierCurve(1000)
        }
        "quadraticBezierSpline" -> {
            currentSpline = QuadraticBezierSpline()
        }
        "cubicBezierSpline" -> {
            currentSpline = CubicBezierSpline()
        }
        "rationalBezierCurve" -> {
            currentSpline = RationalBezierCurve()
        }
        "quadraticRationalBSpline" -> {
            currentSpline = QuadraticRationalBSpline()
        }
        "cubicRationalBSpline" -> {
            currentSpline = CubicRationalBSpline()
        }
        "cubicCardinalSpline0_2" -> {
            currentSpline = CubicCardinalSpline(0.2)
        }
        "cubicCardinalSpline0_35" -> {
            currentSpline = CubicCardinalSpline(0.35)
        }
        "cubicCardinalSpline0_5" -> {
            currentSpline = CubicCardinalSpline(0.5)
        }
        "cubicCardinalSpline0_65" -> {
            currentSpline = CubicCardinalSpline(0.65)
        }
        "cubicCardinalSpline0_8" -> {
            currentSpline = CubicCardinalSpline(0.8)
        }
        "cubicCardinalSpline1" -> {
            currentSpline = CubicCardinalSpline(1.0)
        }
        "cubicCardinalSpline2" -> {
            currentSpline = CubicCardinalSpline(2.0)
        }
    }
    // Redraw canvas
    currentSpline.draw()
}