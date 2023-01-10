package cz.danielkouba.ktorStarterpackDk.configuration

import io.ktor.server.application.*
import io.ktor.server.engine.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun Application.configureShutdown() {

    // this is probably not good idea to use in production (attack vector)
    val enableShutdownUrl = false
    if (enableShutdownUrl) {
        install(ShutDownUrl.ApplicationCallPlugin) {
            shutDownUrl = "/shutdown"
            exitCodeSupplier = { 0 }
        }
    }

    environment.monitor.subscribe(ApplicationStopped) { application ->
        /**
         * TODO  What should be done when application is stopped
         *   - probably every module should have it's own shutdown hook  (application.environment.monitor.subscribe)
         *   - i presume that server is not receiving any new requests (must be tested)
         *   - so we just wait for current calls to be finished
         *   - should be runBlocking scope ?
         */

    }

    // this is just test
//    Executors.newSingleThreadScheduledExecutor()
//        .scheduleAtFixedRate({ println(".") }, 0, 2, TimeUnit.SECONDS)

}

/**
 * Has to be called from main() function to ensure graceful shutdown working properly
 * in other case, application will be terminated immediately without [ApplicationStopped] event raised
 */
fun gracefulShutdown(server: ApplicationEngine) {
    server.stop(
        Config.shutdownGracefulPeriodSec,
        Config.shutdownTimeoutSec,
        TimeUnit.SECONDS
    )
}

