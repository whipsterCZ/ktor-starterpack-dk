package cz.danielkouba.ktorStarterpackDk.modules.app

import cz.danielkouba.ktorStarterpackDk.lib.model.ApplicationModule
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val APP_NAME = "ktor-starterpack-dk"
const val APP_VERSION = "1.0"

class AppModule : ApplicationModule() {

    init {
        logger.debug("${this::class.simpleName} init")
//        LoggerTest(loggerService).testChild()
    }

    override fun registerRouting() = routing {
        get("/") {
            ApiInfoHandler().respondTo(call)
        }
        route("/test") {
            get("/plain") {
                call.respondText("Hello World!")
            }
            get("/json") {
                call.respond(mapOf("hello" to "world"))
            }
            if (config.isDevelopment()) {
                get("/config") {
                    call.respond(config)
                }
            }
        }
    }


    override suspend fun onShutdown() {
        // nothing to do
    }

}
