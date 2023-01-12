package cz.danielkouba.ktorStarterpackDk.configuration

import io.ktor.events.EventHandler
import io.ktor.server.application.*
import io.ktor.server.engine.*
import org.koin.core.KoinApplication
import org.koin.ktor.plugin.KoinApplicationStopPreparing
import java.util.concurrent.TimeUnit

fun Application.configureShutdown() {

    // this is probably not good idea to use in production (attack vector)
    val unsafeEnableShutdownUrl = false

    if (unsafeEnableShutdownUrl) {
        install(ShutDownUrl.ApplicationCallPlugin) {
            shutDownUrl = "/shutdown"
            exitCodeSupplier = { 0 }
        }
    }
}


/**
 * Shortcut for registering shutdown hooks
 *   - every module should handle its cleanup by itself
 *   - I presume that server is not receiving any new requests
 *   - TODO(need to test if app doesnt receive any new requests but handle existing ones)
 *
 * Note:  It is not possible  to listen KTOR [ApplicationStopPreparing] event
 *  - because KOIN is already stopped and DI doesn't work at this point
 *  - It is listening to [KoinApplicationStopPreparing] event, which is fired before the KoinApplication is stopped.
 */
fun Application.onShutdown(handler: EventHandler<KoinApplication>) =
    environment.monitor.subscribe(KoinApplicationStopPreparing, handler)


/**
 * Has to be called from main() function to ensure graceful shutdown working properly
 * in other case, application will be terminated immediately without [ApplicationStopped] event raised
 */
fun gracefulShutdown(server: ApplicationEngine) {
    server.environment.log.info("Graceful shutdown. Server is stopping ...")
    server.stop(
        Config.shutdownGracefulPeriodSec,
        Config.shutdownTimeoutSec,
        TimeUnit.SECONDS
    )
}

