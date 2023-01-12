package cz.danielkouba.ktorStarterpackDk.lib.interfaces

import io.ktor.server.application.ApplicationCall

/**
 * Interface for all route handlers in application, that handle API JSON response
 */
interface RouteHandlerJson {

    /**
     * Handles the route and returns the [Serializable] result
     * @param call ApplicationCall
     */
    suspend fun respondTo(call: ApplicationCall): Unit
}
