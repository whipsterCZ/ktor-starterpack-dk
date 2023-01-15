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
            errorHandler.respond(call, status)
        }

        exception<NotFoundException> { call, cause ->
            errorHandler.logAndRespond(call, HttpStatusCode.NotFound, cause)
        }
        exception<IllegalArgumentException> { call, cause ->
            errorHandler.logAndRespond(call, HttpStatusCode.UnprocessableEntity, cause)
        }
        exception<RequestValidationException> { call, cause ->
            errorHandler.log( HttpStatusCode.UnprocessableEntity, cause.reasons.joinToString())
            errorHandler.respond(call, HttpStatusCode.UnprocessableEntity, cause)
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
        respond(call, status, cause)
    }

    suspend fun log(status: HttpStatusCode, cause: Throwable? = null) {
        if (cause != null) {
            if (stackTrace) {
                logger.error("$status - Unhandled exception ${cause.message}", cause)
            } else {
                logger.error("$status - Unhandled exception ${cause.message}")
            }
        } else {
            logger.error("$status")
        }
    }

    suspend fun log(status: HttpStatusCode, message: String?) {
        if (message != null) {
            logger.error("$status - Unhandled error $message")
        } else {
            logger.error("$status")
        }
    }

    suspend fun respond(
        call: ApplicationCall,
        status: HttpStatusCode,
        cause: Throwable? = null,
    ) {
        if (acceptJson(call)) {
            respondJson(call, status)
        } else {
            respondText(call, status, cause)
        }
    }

    suspend fun respond(
        call: ApplicationCall,
        status: HttpStatusCode,
        message: String
    ) {
        if (acceptJson(call)) {
            respondJson(call, status)
        } else {
            respondText(call, status, message)
        }
    }

    suspend fun respondJson(call: ApplicationCall, status: HttpStatusCode, cause: Throwable? = null) {
        val message = if (cause != null) cause.message else status.toString()
        call.respond(status, ErrorResponse(status.value, message))
    }

    suspend fun respondJson(call: ApplicationCall, status: HttpStatusCode, message: String) {
        call.respond(status, ErrorResponse(status.value, message))
    }

    suspend fun respondText(call: ApplicationCall, status: HttpStatusCode, cause: Throwable? = null) {
        call.respondText(
            text = if (cause == null) "$status" else "$status: $cause",
            status = status,
        )
    }

    suspend fun respondText(call: ApplicationCall, status: HttpStatusCode, message: String) {
        call.respondText(
            text = "$status: $message",
            status = status,
        )
    }

    fun acceptJson(call: ApplicationCall): Boolean {
        if (!acceptTypeSensitive) return true
        val accepts = call.request.acceptItems()
        if (accepts.any { it.value == ContentType.Application.Json.toString() }) return true
        return accepts.none { it.value == ContentType.Text.Plain.toString() }
    }
}
