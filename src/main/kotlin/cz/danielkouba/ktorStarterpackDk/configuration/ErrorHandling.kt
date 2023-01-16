package cz.danielkouba.ktorStarterpackDk.configuration

import ch.qos.logback.classic.Logger
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Application.configureErrorHandling() {

    val loggerService: LoggerService by inject()

    val errorHandler = ErrorHandler(
        logger = loggerService.createLogger("unhandled-exception"),
        acceptTypeSensitive = true,
        stackTrace = config().isDevelopment()
    )

    install(StatusPages) {


        status(
            HttpStatusCode.BadRequest,
            HttpStatusCode.NotFound,
            HttpStatusCode.UnprocessableEntity,
            HttpStatusCode.Forbidden,
            HttpStatusCode.Unauthorized,
        ) { call, status ->
            errorHandler.respond(call, status, status.toString())
        }

        exception<NotFoundException> { call, cause ->
            errorHandler.logAndRespond(call, HttpStatusCode.NotFound, cause)
        }
        exception<IllegalArgumentException> { call, cause ->
            errorHandler.logAndRespond(call, HttpStatusCode.InternalServerError, cause)
        }
        exception<RequestValidationException> { call, cause ->
            errorHandler.logAndRespond(call, HttpStatusCode.UnprocessableEntity, cause.reasons.joinToString())
        }
        exception<BadRequestException> { call, cause ->
            errorHandler.logAndRespond(call, HttpStatusCode.BadRequest, cause)
        }
        exception<Throwable> { call, cause ->
            errorHandler.logAndRespond(call, HttpStatusCode.InternalServerError, cause)
        }
    }
}

@Serializable
data class ErrorResponse(val status: Int, val errorMessage: String? = null)

class ErrorHandler(val logger: Logger, val acceptTypeSensitive: Boolean, val stackTrace: Boolean = true) {

    suspend fun logAndRespond(call: ApplicationCall, status: HttpStatusCode, cause: Throwable? = null) {
        log(status, cause)
        respond(call, status, cause?.toString() ?: status.toString())
    }

    suspend fun logAndRespond(call: ApplicationCall, status: HttpStatusCode, message: String) {
        log(status, message)
        respond(call, status, message)
    }

    suspend fun log(status: HttpStatusCode, cause: Throwable? = null) {
        if (cause != null) {
            if (stackTrace) {
                logger.error("$status - ${cause.message}", cause)
            } else {
                logger.error("$status - ${cause.message}")
            }
        } else {
            logger.error("$status")
        }
    }

    suspend fun log(status: HttpStatusCode, message: String?) {
        if (message != null) {
            logger.error("$status - $message")
        } else {
            logger.error("$status")
        }
    }

    suspend fun respond(
        call: ApplicationCall,
        status: HttpStatusCode,
        message: String
    ) {
        if (acceptJson(call)) {
            call.respond(status, ErrorResponse(status.value, message))
        } else {
            call.respondText(
                text = "$status: $message",
                status = status,
            )
        }
    }

    fun acceptJson(call: ApplicationCall): Boolean {
        if (!acceptTypeSensitive) return true
        val accepts = call.request.acceptItems()
        if (accepts.any { it.value == ContentType.Application.Json.toString() }) return true
        return accepts.none { it.value == ContentType.Text.Plain.toString() }
    }
}
