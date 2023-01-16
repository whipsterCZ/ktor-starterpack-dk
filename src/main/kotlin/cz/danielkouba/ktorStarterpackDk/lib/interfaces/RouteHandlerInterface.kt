package cz.danielkouba.ktorStarterpackDk.lib.interfaces

import cz.danielkouba.ktorStarterpackDk.configuration.configureErrorHandling
import io.ktor.http.*
import io.ktor.server.application.*

/**
 * Interface for all route handlers in application
 */
interface RouteHandlerInterface {

    /**
     * Handles the route request and respond with [HttpStatusCode] and [Any] result
     * @param call ApplicationCall
     * Exceptions will be handled automatically by the app exception handler (see [configureErrorHandling])
     */
    suspend fun respondTo(call: ApplicationCall): Unit
}
