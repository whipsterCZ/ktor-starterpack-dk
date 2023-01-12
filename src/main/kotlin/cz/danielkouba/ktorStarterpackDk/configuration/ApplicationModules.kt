package cz.danielkouba.ktorStarterpackDk.configuration

import cz.danielkouba.ktorStarterpackDk.modules.app.AppModule
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleModule
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import io.ktor.events.EventHandler
import io.ktor.server.application.*
import io.ktor.server.resources.*
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.koin.ktor.plugin.koin
import kotlin.system.measureTimeMillis

/**
 * Attach particular app modules (routing, handlers, DI, services, etc.)
 */
fun Application.configureApplicationModules() {

    // needed fot typed-safe routing with parameters
    install(Resources)

    // order of modules is important (DI is initialized in order of modules)
    val modules: List<ApplicationModule> = listOf(
        AppModule(),
        ArticleModule(),
    )

    // register dependency injection modules (expose services)
    modules.forEach {
        koin {
            modules(it.dependencyInjectionModules())
        }
    }

    // register routing
    modules.forEach {
        it.routing()
    }


    // graceful shutdown modules
    onShutdown {
        log.info("ApplicationModules.onShutdown started")

        val time = measureTimeMillis {
            val hooks = modules.map {
                async {
                    try {
                        it.onShutdown()
                    } catch (e: Exception) {
                        log.error(
                            "ApplicationModules.onShutdown Error during shutdown of module ${it.name} ${e.message}",
                            e
                        )
                    }
                }
            }

            runBlocking(Dispatchers.Default) {
                launch {
                    hooks.awaitAll()
                }
            }
        }
        log.info("ApplicationModules.onShutdown ended in ${time}ms")
    }

}

abstract class ApplicationModule : KoinComponent {
    val config by lazy { application.config() }
    val application: Application by inject()
    val loggerService: LoggerService by inject()
    val name = this::class.simpleName.toString()
    val logger by lazy {
        loggerService.createLogger(name)
    }

    /**
     * register routing
     */
    abstract fun routing(): Unit

    /**
     * register dependency injection modules (koin)
     */
    abstract fun dependencyInjectionModules(): List<Module>

    /**
     * On graceful shutdown release resources, close connections, close websockets etc.
     * All exceptions are handled in onShutdown hook itself
     */
    abstract suspend fun onShutdown()

    /**
     * Cant be static because it looks like abstract class doesn't have public static properties
     */

}