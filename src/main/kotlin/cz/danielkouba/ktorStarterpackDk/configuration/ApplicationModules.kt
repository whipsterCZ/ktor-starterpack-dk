package cz.danielkouba.ktorStarterpackDk.configuration

import cz.danielkouba.ktorStarterpackDk.lib.model.ApplicationModule
import cz.danielkouba.ktorStarterpackDk.modules.app.AppModule
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleModule
import cz.danielkouba.ktorStarterpackDk.modules.test.TestModule
import io.ktor.server.application.*
import io.ktor.server.resources.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * Attach particular app modules (routing, handlers, DI, services, etc.)
 * @param modules Modules to be attached. Order of modules is important (DI is initialized in order of modules)
 *
 * - register dependency injection modules (expose services) @see [ApplicationModule.registerDiProvider]
 * - register routing - @see [ApplicationModule.routing]
 */
fun Application.attachApplicationModules() {

    enableResourceRouting()

    val modules: List<ApplicationModule> = listOf(
        AppModule(),
        ArticleModule(),
        TestModule(),
        // add more modules here
    )

    modules.forEach { module ->
        module.registerRouting()
    }

    modules.forEach { module -> module.onStart() }

    /**
     * graceful shutdown modules @see [ApplicationModule.onShutdown]
     */
    onShutdown {
        val asyncShutdown = config().shutdownHooksAsync
        val asyncLabel = if (asyncShutdown) "async" else "sync"
        log.info("ApplicationModules.onShutdown started ($asyncLabel)")

        val time = measureTimeMillis {
            val hooks = modules.reversed().map {
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
                    if (asyncShutdown) {
                        hooks.awaitAll()
                    } else {
                        hooks.forEach { it.await() }
                    }
                }
            }
        }
        log.info("ApplicationModules.onShutdown ended in ${time}ms")
    }

}

fun Application.enableResourceRouting() = pluginOrNull(Resources) ?: install(Resources)

