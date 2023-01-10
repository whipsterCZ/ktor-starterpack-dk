package cz.danielkouba.ktorStarterpackDk.lib.interfaces

import io.ktor.server.application.ApplicationCall

interface RouteHandler {
    suspend fun handle(call: ApplicationCall): Unit
}
