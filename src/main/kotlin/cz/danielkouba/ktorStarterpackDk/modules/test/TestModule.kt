package cz.danielkouba.ktorStarterpackDk.modules.test

import cz.danielkouba.ktorStarterpackDk.lib.model.ApplicationModule
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerTest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class TestModule : ApplicationModule() {

    init {
        logger.debug("$name init")

    }

    override fun registerRouting(): Routing {
        return routing {
            route("/test") {
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
                        throw RequestValidationException("Validation error", arrayListOf("error1", "error2"))
                    }
                    get("/illegal-argument") {
                        throw IllegalArgumentException("Invalid argument")
                    }
                    get("/bad-request") {
                        throw BadRequestException("Bad request")
                    }
                    get("/forbidden") {
                        call.respond(HttpStatusCode.Forbidden, "Forbidden")
                    }
                    get("/unauthorized") {
                        call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                    }
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

                if (config.isDevelopment()) {
                    get("/config") {
                        call.respond(config)
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