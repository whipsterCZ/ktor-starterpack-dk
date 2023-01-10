package cz.danielkouba.ktorStarterpackDk.configuration

import cz.danielkouba.ktorStarterpackDk.modules.app.AppModule
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleModule
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import io.ktor.events.EventHandler
import io.ktor.server.application.*
import io.ktor.server.resources.*
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.koin.ktor.plugin.koin

// Attach particular app modules (routing, handlers, DI, services, etc.)
fun Application.configureApplicationModules() {

    // needed fot typed-safe routing with parameters
    install(Resources)

    // order of modules is important (DI is initialized in order of modules)
    val modules: List<ApplicationModule> = listOf(
//        AppModule(),
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
    environment.monitor.subscribe(ApplicationStopped) {
        runBlocking {
            for(module in modules) {
                println("onGracefulShutdown ${module.name} 1 ")
                module.onShutdown()
                println("onGracefulShutdown ${module.name} 2")
                module.onShutdown()
                println("onGracefulShutdown ${module.name} 3")
                module.onShutdown()
                println("onGracefulShutdown ${module.name} end")
                module.onShutdown()
            }
            modules.forEach {
            }
        }
        println("configreGracefulShutdown end")
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
     * on graceful shutdown release resources, close connections, close websockets etc.
     */
    open suspend fun onShutdown(): Unit {}

    /**
     * Another way how to register graceful shutdown
     * TODO:  decide which way is better
     */
    fun onGracefulShutdown(handler: EventHandler<Application>) {
        application.environment.monitor.subscribe(ApplicationStopped, handler)
    }

}