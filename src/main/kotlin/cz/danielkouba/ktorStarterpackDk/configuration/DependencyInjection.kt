package cz.danielkouba.ktorStarterpackDk.configuration

import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerProviderDI
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

/**
 * Dependency injection initialization for Ktor application [Koin](https://insert-koin.io/docs/reference/koin-ktor/ktor)
 */
fun Application.configureDependencyInjection() {

    val config = this.config()
    val loggerDiProvider = LoggerProviderDI(config)
    val applicationDiProvider = module {
        single<Application> { this@configureDependencyInjection }
    }

    /**
     * Dependency injection via [Koin](https://insert-koin.io/docs/reference/koin-ktor/ktor)
     */
    install(Koin) {
        slf4jLogger()

        modules(
            listOf(
                applicationDiProvider,
                loggerDiProvider, // it has to be registered before all other modules
                // all other services are registered in particular application module
            )
        )
    }
}
