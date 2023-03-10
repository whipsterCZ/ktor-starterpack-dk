package cz.danielkouba.ktorStarterpackDk.configuration

import cz.danielkouba.ktorStarterpackDk.modules.app.config.ConfigFactory
import cz.danielkouba.ktorStarterpackDk.modules.app.config.ConfigModel
import cz.danielkouba.ktorStarterpackDk.modules.app.config.applicationConfigFactory
import io.ktor.server.application.*

/**
 * This is the main configuration.
 * it is proxy for real implementation [ConfigModel]
 * - it allows to switch to different implementation
 * - it serves as a single point of access to the configuration
 * - it support loose coupling (modules should be independent on each other - [ConfigModel] is part of AppModule)
 * - it allows to found easy configuration in both `/configuration` and `/modules/app/config` directory  (it is linked)
 *
 * please use [Config] only in scope of application `Module` and `DI` providers or `/configuration` directory
 * Avoid tight coupling of modules to configuration - Business Logic should be not aware of [Config]
 * @see [ArticleModule] for example
 */
val Config: ConfigModel by lazy {
    applicationConfigFactory.create().also {
        if (it.isDevelopment()) println("Config initialized: $it")
    }
}

/**
 * Get the config from the application
 */
fun Application.config() = Config

/**
 * Set the config factory for the application
 */
fun Application.setConfigFactory(configFactory: ConfigFactory) {
    applicationConfigFactory = configFactory
}
