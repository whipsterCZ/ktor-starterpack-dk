package cz.danielkouba.ktorStarterpackDk.configuration

import cz.danielkouba.ktorStarterpackDk.modules.logger.loggerDIModule
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection() {

    val applicationModule = module {
        single<Application> { this@configureDependencyInjection }
    }

    // Dependency injection via Koin (https://insert-koin.io/docs/reference/koin-ktor/ktor)
    install(Koin) {
        slf4jLogger()

        modules(
            listOf(
                applicationModule,
                loggerDIModule
                // all other are registered in particular application module
            )
        )

    }

}