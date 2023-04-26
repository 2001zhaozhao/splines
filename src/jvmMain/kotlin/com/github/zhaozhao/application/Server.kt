package com.github.zhaozhao.application

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.routing.*
import kotlinx.html.*

/**
 * Function to build the HTML page
 */
fun HTML.index() {
    head {
        title("Splines app")
    }
    body {
        div {
            +"Splines app project to study for exam. Left click to add point, right click to remove point, scroll wheel to change point weight for weighted splines (by 0.25 per scroll)"
        }
        div {
            id = "root"
        }
        canvas {
            id = "splinesCanvas"
            width = "1280"
            height = "720"
            style = "border=1px solid #000000;"
        }
        script(src = "/static/splines.js") {}
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::myApplicationModule).start(wait = true)
}

fun Application.myApplicationModule() {
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        static("/static") {
            resources()
        }
    }
}