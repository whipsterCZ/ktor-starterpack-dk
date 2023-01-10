package cz.danielkouba.ktorStarterpackDk.configuration

import ch.qos.logback.classic.Level
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import org.koin.ktor.ext.inject
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.util.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

fun Application.configureLogging() {

    /**
     * @see [LoggerService.configureLoggerContext]
     */
    val loggerService: LoggerService by inject()


    install(CallId) {
        header(HttpHeaders.XRequestId)
        verify { callId: String ->
            callId.isNotEmpty()
        }
        generate {
            UUID.randomUUID().toString()
        }
    }

    val CALL_START_TIME = AttributeKey<LocalDateTime>("CallStartTime")

    intercept(ApplicationCallPipeline.Setup) {
        call.attributes.put(CALL_START_TIME, LocalDateTime.now())
    }

    install(CallLogging) {
        logger = loggerService.createLogger("router")
        filter { call ->
            // skip `/metrics` and `/health` etc..
            call.request.path().startsWith("/v1")
        }
        callIdMdc("requestId")
        disableDefaultColors()
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val path = call.request.path()
            val duration = when (val startTime = call.attributes.getOrNull(CALL_START_TIME)) {
                null -> "?ms" // just in case
                else -> "${startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS)}ms"
            }

            "$status $httpMethod $path $duration"
        }
    }
}