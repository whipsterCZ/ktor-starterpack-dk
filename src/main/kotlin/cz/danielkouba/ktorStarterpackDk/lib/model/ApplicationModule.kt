package cz.danielkouba.ktorStarterpackDk.lib.model

import cz.danielkouba.ktorStarterpackDk.configuration.config
import cz.danielkouba.ktorStarterpackDk.configuration.enableResourceRouting
import cz.danielkouba.ktorStarterpackDk.configuration.initApplicationModules
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.koin.ktor.plugin.koin

/**
 * Application module is self-contained business logic categorized by domain
 *
 * it serves as bridge between Ktor application and particular business logic
 *
 * Application module provides
 *  - dependency injection [ApplicationModule.registerDiProviders]
 *  - routing [ApplicationModule.routing]
 *  - access to application [ApplicationModule.application]
 *  - access to configuration [ApplicationModule.config]
 *  - access to logger [ApplicationModule.logger]
 *  - shutdown hook [ApplicationModule.onShutdown]
 *
 */
abstract class ApplicationModule : KoinComponent {
    val config by lazy { application.config() }
    val application: Application by inject()
    val loggerService: LoggerService by inject()
    val name = this::class.simpleName.toString()
    val logger by lazy {
        loggerService.createLogger(name)
    }

    /**
     * Register dependency injection providers for KOIN
     * it should be called in init block
     */
    fun registerDiProvider(vararg provider: Module) {
        application.koin {
            modules(*provider)
        }
    }

    /** Routing for this module
     * it should be called from [Application.initApplicationModules] */
    abstract fun registerRouting(): Routing

    /**
     * Register routing for Ktor application
     * it should be called in  block
     */
    fun routing(configuration: Routing.() -> Unit): Routing = application.routing(configuration)

    /**
     * On graceful shutdown release resources, close connections, close websockets etc.
     * This is mandatory method to be implemented (even if empty) - encourages to release resources
     * All exceptions are handled in onShutdown hook itself
     */
    abstract suspend fun onShutdown()

    /**
     * Optional hook is called when all modules are initialized.
     */
    fun onStart() {}

    fun enableResourceRouting() = application.enableResourceRouting()
}
