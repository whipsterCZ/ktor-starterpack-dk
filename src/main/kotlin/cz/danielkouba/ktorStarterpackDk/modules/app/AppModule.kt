package cz.danielkouba.ktorStarterpackDk.modules.app

import cz.danielkouba.ktorStarterpackDk.lib.model.ApplicationModule
import io.ktor.server.application.*
import io.ktor.server.routing.*

const val APP_NAME = "ktor-starterpack-dk"
const val APP_VERSION = "1.0"

class AppModule : ApplicationModule() {

    init {
        logger.debug("$name init")
    }

    override fun registerRouting() = routing {
        get("/") {
            ApiInfoHandler().respondTo(call)
        }
    }


    override suspend fun onShutdown() {
        // nothing to do
    }

}
