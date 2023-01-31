package cz.danielkouba.ktorStarterpackDk.modules.app.config

import ch.qos.logback.classic.Level
import cz.danielkouba.ktorStarterpackDk.lib.serializers.LogLevelSerializer
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

const val DEFAULT_PORT = 8080
const val DEFAULT_HOST = "0.0.0.0"
const val DEFAULT_SHUTDOWN_GRACEFUL_PERIOD = 2L
const val DEFAULT_SHUTDOWN_TIMEOUT = 5L

/**
 * @param shutdownGracefulPeriod How long (ms) to wait for graceful shutdown (server activity cool down)
 * @param shutdownTimeout Timeout (ms) to complete shutdown hooks (App will be shut down forcefully after that)
 * @param logAppender Which channel and format will be used for logging @see /resources/logback.xml
 * @param logLevel Filter outputted Logs to certain severity level
 */
@Serializable
data class ConfigModel constructor(
    val appName: String,
    val appVersion: String,
    val apiVersion: String,
    val environment: ConfigEnvironment = ConfigEnvironment.PRODUCTION,
    val port: Int = DEFAULT_PORT,
    val host: String = DEFAULT_HOST,
    @Contextual
    @Serializable(with = LogLevelSerializer::class)
    val logLevel: Level,
    val logAppender: LoggerService.Appender = LoggerService.Appender.JSON,
    val shutdownGracefulPeriod: Long = DEFAULT_SHUTDOWN_GRACEFUL_PERIOD,
    val shutdownTimeout: Long = DEFAULT_SHUTDOWN_TIMEOUT,
    val shutdownHooksAsync: Boolean = true,
    val articleApi: ConfigApi,
    val metricsPrefix: String = "http_request_duration",
) {
    fun isDevelopment() = environment.isDevelopment()
    fun isProduction() = environment.isProduction()
    fun isTest() = environment.isTest()
    fun isUnitTest() = environment.isUnitTest()

    @Serializable
    public data class ConfigApi(val url: String, val authToken: String?)
}
