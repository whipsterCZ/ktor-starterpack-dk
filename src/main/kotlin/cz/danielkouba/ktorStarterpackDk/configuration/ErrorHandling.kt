package cz.danielkouba.ktorStarterpackDk.configuration

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

fun Application.configureErrorHandling() {

    suspend fun responseTo(call: ApplicationCall, status: HttpStatusCode, message: String? = null) {
        @Serializable
        data class ErrorResponse(val status: Int, val errorMessage: String? = null)

        if (call.request.acceptItems().any { it.value == "application/json" }) {
            call.respond(
                ErrorResponse(status.value, message ?: status.description)
            )
        } else {
            call.respondText(
                text = if (message == null) "$status" else "$status: $message",
                status = status,
            )
        }
    }


    install(StatusPages) {
        exception<Throwable> { call, cause ->
            responseTo(call, HttpStatusCode.InternalServerError, cause.message)
        }
        exception<NotFoundException> { call, cause ->
            responseTo(call, HttpStatusCode.NotFound, cause.message)
        }
        status(HttpStatusCode.NotAcceptable) { call, status ->
            responseTo(call, status)
        }
        status(HttpStatusCode.NotFound) { call, status ->
            responseTo(call, status)
        }
    }
}
