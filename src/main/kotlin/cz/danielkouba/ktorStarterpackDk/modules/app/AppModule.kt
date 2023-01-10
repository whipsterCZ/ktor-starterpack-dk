package cz.danielkouba.ktorStarterpackDk.modules.app

import cz.danielkouba.ktorStarterpackDk.configuration.ApplicationModule
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerTest
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.inject
import org.koin.core.module.Module

const val APP_NAME = "ktor-starterpack-dk"
const val APP_VERSION = "1.0"

class AppModule : ApplicationModule() {

    init {
        logger.info("App module init")
//        LoggerTest(loggerService).testChild()
    }

    override fun routing() {

        val loggerRouting by inject<LoggerService>()
        application.log.info("AppModule.routing init with : $loggerRouting")


        application.routing {
            get("/") {
                ApiInfoHandler().handle(call)
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
    }

    override fun dependencyInjectionModules(): List<Module> = listOf()

}
