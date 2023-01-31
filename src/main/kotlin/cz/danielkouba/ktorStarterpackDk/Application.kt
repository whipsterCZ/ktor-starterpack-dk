package cz.danielkouba.ktorStarterpackDk

import cz.danielkouba.ktorStarterpackDk.configuration.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(
        Netty,
        port = Config.port,
        host = Config.host,
        module = Application::bootstrap
    ).apply {
        addShutdownHook { gracefulShutdown(this) }
        start(true)
    }
}

fun Application.bootstrap() {

    /**
     * Core Application functionality (custom logic)
     */
    config() // access config to Application
    configureDependencyInjection() // Koin DI
    configureLogging() // logging requests and logger configuration
    configureReqContext() // access request context to routing
    attachApplicationModules() // register app modules (routing, services, business logic, etc.

    /**
     * Additional Application functionality (plugins and features)
     */
    configureHTTP() // CORS, Compression, ContentNegotiation, Headers, etc.
    configureErrorHandling() // global exception handling for API routes
    configureMonitoring() // expose metrics nad health check
    configureSwagger() // expose api documentation
    configureShutdown() // graceful shutdown
}

/*    ____     __  __        __  __     ______   ______     ______
    /\  __-.  /\ \/ /       /\ \/ /    /\__  _\ /\  __ \   /\  == \
    \ \ \/\ \ \ \  _"-.     \ \  _"-.  \/_/\ \/ \ \ \/\ \  \ \  __<
     \ \____-  \ \_\ \_\     \ \_\ \_\    \ \_\  \ \_____\  \ \_\ \_\
      \/____/   \/_/\/_/      \/_/\/_/     \/_/   \/_____/   \/_/ /_/
       GINGER    BEAVERS       STARTER-PACK FOR KOTLIN KTOR SERVERS    */
