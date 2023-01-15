package cz.danielkouba.ktorStarterpackDk.modules.app.config

import ch.qos.logback.classic.Level
import cz.danielkouba.ktorStarterpackDk.modules.app.API_VERSION
import cz.danielkouba.ktorStarterpackDk.modules.app.APP_NAME
import cz.danielkouba.ktorStarterpackDk.modules.app.APP_VERSION
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import io.github.cdimascio.dotenv.dotenv

fun ConfigFactoryFromEnv(file: String = ".env", dir: String = "./"): ConfigModel {
    val dotenv = dotenv {
        directory = dir
        filename = file
        ignoreIfMalformed = false
        ignoreIfMissing = true
        systemProperties = true
    }

    return ConfigModel(
        environment = ConfigEnvironment.from(dotenv["ENVIRONMENT"] ?: "production"),
        port = dotenv["PORT"]?.toInt() ?: DEFAULT_PORT,
        host = dotenv["HOST"] ?: DEFAULT_HOST,
        appName = dotenv["APP_NAME"] ?: APP_NAME,
        appVersion = dotenv["APP_VERSION"] ?: APP_VERSION,
        apiVersion = API_VERSION,
        logLevel = dotenv["LOG_LEVEL"]?.let { Level.toLevel(it) } ?: Level.INFO,
        logAppender = dotenv["LOG_APPENDER"]?.let { LoggerService.Appender.from(it) } ?: LoggerService.Appender.JSON,
        shutdownGracefulPeriod = dotenv["SHUTDOWN_GRACEFUL_PERIOD"]?.toLong() ?: DEFAULT_SHUTDOWN_GRACEFUL_PERIOD,
        shutdownTimeout = dotenv["SHUTDOWN_TIMEOUT"]?.toLong() ?: DEFAULT_SHUTDOWN_TIMEOUT,
        shutdownHooksAsync = dotenv["SHUTDOWN_HOOKS_ASYNC"]?.toBoolean() ?: true,
        articleApi = ConfigModel.ConfigApi(
            url = dotenv["ARTICLE_API_URL"]
                ?: configEnvMissing("ARTICLE_API_URL"),
            authToken = dotenv["ARTICLE_API_AUTH_TOKEN"]
        )
    )
}

fun configEnvMissing(paramName: String): Nothing =
    throw IllegalArgumentException("Config - Environment arg '$paramName' is required but is missing")
