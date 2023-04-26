import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement

/**
 * Controls the canvas in the splines app
 */
object SplinesCanvas {
    val canvas = document.getElementById("splinesCanvas") as HTMLCanvasElement
}