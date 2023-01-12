package cz.danielkouba.ktorStarterpackDk.modules.logger

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import ch.qos.logback.core.joran.spi.JoranException
import ch.qos.logback.core.util.StatusPrinter
import cz.danielkouba.ktorStarterpackDk.modules.app.config.ConfigEnvironment
import org.slf4j.LoggerFactory


/**
 * Provides logger service and factory for creating loggers.
 */
class LoggerService(
    val rootLogger: Logger, // TODO("private")
    private val environment: ConfigEnvironment,
    private val logLevel: Level,
    private val logAppender: Appender,
    private val appName: String,
    private val appVersion: String,
    private val logbackConfigFile: String = "${System.getProperty("user.dir")}/src/main/resources/logback.xml"
) {

    init {
        configureLoggerContext()
    }

    /**
     * Sets selected log appender
     */
    fun configureLoggerContext() {
        val loggerContext: LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext

        try {
            JoranConfigurator().apply {
                context = loggerContext
                loggerContext.reset()
                loggerContext.putProperty("appenderName", logAppender.toString())
                loggerContext.putProperty("environment", environment.name.lowercase())
                loggerContext.putProperty("appName", appName)
                loggerContext.putProperty("appVersion", appVersion)
                doConfigure(logbackConfigFile)
            }
        } catch (je: JoranException) {
            // StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext)
    }

    /**
     * create logger with app context (root logger child)
     * @param scope name of the logger (e.g. "article") Use camelCase
     * @param configure optional configuration of the logger
     */
    fun createLogger(scope: String, configure: ConfigureLogger? = null): Logger {
        val logger = rootLogger.child(scope) {
            level = logLevel
        }
        configure?.invoke(logger)
        return logger
    }

    enum class Appender {
        JSON, READABLE;

        override fun toString() = super.name.lowercase()

        companion object {
            fun from(string: String) = valueOf(string.uppercase())
        }
    }

    companion object {
        /**
         * factory for default root logger
         * @param configure optional configuration of the logger
         */
        fun createRootLogger(name: String = "app", configure: ConfigureLogger? = null): Logger {
            val rootLogger: Logger = (LoggerFactory.getLogger(name) as Logger)
            configure?.invoke(rootLogger)
            return rootLogger
        }
    }
}

/**
 * Logger extension which create child logger with optional configuration
 *
 * @param scope name of the logger (e.g. "article") Use camelCase
 * @param configure optional configuration of the logger
 */
fun Logger.child(scope: String, configure: ConfigureLogger? = null): Logger {
    val loggerName = "${name}.${scope}"
    val logger = loggerContext.getLogger(loggerName) as Logger
    logger.loggerContext.putProperty("scope", scope)
    configure?.invoke(logger)

    return logger
}

typealias ConfigureLogger = Logger.() -> Unit
