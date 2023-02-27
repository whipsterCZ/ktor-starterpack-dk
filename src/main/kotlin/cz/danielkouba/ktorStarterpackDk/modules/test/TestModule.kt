package cz.danielkouba.ktorStarterpackDk.modules.test

import ch.qos.logback.classic.Level
import cz.danielkouba.ktorStarterpackDk.configuration.reqContext
import cz.danielkouba.ktorStarterpackDk.lib.model.ApplicationModule
import cz.danielkouba.ktorStarterpackDk.lib.model.ValidationError
import cz.danielkouba.ktorStarterpackDk.lib.model.ValidationException
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerTest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay

class TestModule : ApplicationModule() {

    init {
        logger.debug("$name init")
    }

    override fun registerRouting(): Routing {
        return routing {
            route("/test") {
                // be careful that correct environment is set - it can be used to expose sensitive data
                if (config.isDevelopment()) {
                    get("/config") {
                        call.respond(config)
                    }
                }

                get("/plain") {
                    call.respondText("Hello World!")
                }
                get("/json") {
                    call.respond(mapOf("hello" to "world"))
                }
                get("/logger") {
                    LoggerTest(loggerService).testChild()
                    call.respondText("Logger test - check console")
                }

                route("/throw") {
                    get("/not-found") {
                        throw NotFoundException("Not found")
                    }
                    get("/validation-error") {
                        throw ValidationException(
                            "Model",
                            arrayListOf(
                                ValidationError.Required("id"),
                                ValidationError.Format("date", "Custom error message")
                            )
                        )
                    }
                    get("/illegal-argument") {
                        throw IllegalArgumentException("Invalid argument")
                    }
                    get("/bad-request") {
                        throw BadRequestException("Bad request", cause = IllegalArgumentException("Invalid argument"))
                    }
                    get("/forbidden") {
                        call.respond(HttpStatusCode.Forbidden, "Forbidden")
                    }
                    get("/unauthorized") {
                        call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                    }
                }

                /**
                 * Request Context (Call Context) is a way to pass data between different layers of the application.
                 * it contains call specific data such as logger, meta, etc.
                 * it prevents to need of making Services for each request. (users has to have separate context)
                 *
                 * This also serves as an example of how to use it
                 */
                get("/req-context") {
                    val context = call.reqContext("reqContext") {
                        userId("userId")
                        logLevel(Level.INFO)
                    }
                    context.logger.info("testing reqContext | logger INFO")
                    context.logger.debug("testing reqContext | logger DEBUG - should not be logged")

                    val childContext = context.createChild("child") {
                        userId("userId:Child")
                        logLevel(Level.DEBUG)
                    }
                    childContext.logger.info("testing reqContext.child | logger INFO")
                    childContext.logger.debug("testing reqContext.child | logger DEBUG - should be logged")

                    context.logger.info("testing reqContext #2 | logger INFO")
                    context.logger.debug("testing reqContext #2 | logger DEBUG - should not be logged")

                    call.respond(context.toString())
                }

                route("/coroutines") {
                    get("/delay") {
                        delay(1000)
                        call.respondText("Delayed response")
                    }
                    get("/async-operation") {
                        val result = asyncOperationsList()
                        call.respondText("Async operation result: $result")
                    }
                    get("/sync-operation") {
                        val result = syncOperations()
                        call.respondText("Sync operation result: $result")
                    }
                }
            }
        }
    }

    override suspend fun onShutdown() {
        logger.info("TestModule shutdown (waiting 1s)")
        delay(1000)
        logger.info("TestModule shutdown finished")
    }
}
