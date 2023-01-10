package cz.danielkouba.ktorStarterpackDk.modules.app.config

import ch.qos.logback.classic.Level
import cz.danielkouba.ktorStarterpackDk.lib.serializers.LogLevelSerializer
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import kotlinx.serialization.*

const val DEFAULT_PORT = 8080
const val DEFAULT_HOST = "0.0.0.0"

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
    val articleApi: ConfigApi,
    val shutdownGracefulPeriodSec: Long = 2,
    val shutdownTimeoutSec: Long = 5,
) {
    fun isDevelopment() = environment.isDevelopment()
    fun isProduction() = environment.isProduction()
    fun isTest() = environment.isTest()
    fun isUnitTest() = environment.isUnitTest()

    @Serializable
    public data class ConfigApi(val url: String, val authToken: String?)
}

