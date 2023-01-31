package cz.danielkouba.ktorStarterpackDk.configuration

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

// import io.swagger.codegen.v3.generators.html.StaticHtmlCodegen

fun Application.configureSwagger() {
    routing {
        swaggerUI(path = "swagger", swaggerFile = "swagger.yml")
    }
}
