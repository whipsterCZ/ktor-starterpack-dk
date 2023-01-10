package cz.danielkouba.ktorStarterpackDk.modules.logger

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory

class LoggerTest(val service: LoggerService) {
    fun testChild() {


        fun printLogger(name: String, logger: Logger) {
            println(name)
            println("  ${logger.name} ${logger.hashCode()}")
            println("  ${logger.name} ${logger.loggerContext} ${logger.loggerContext.hashCode()}")
            logger.debug(name)
            println()
        }

        val logger = service.createLogger("router")
        logger.also { printLogger("logger", it) }


        val loggerChild = logger.child("child")
        loggerChild.also { printLogger("loggerChild", it) }

        val loggerChildChild = loggerChild.child("child")
        loggerChildChild.also { printLogger("loggerChildChild", it) }

        val customLogger = (LoggerFactory.getLogger("ktor.application") as Logger)
        customLogger.also { printLogger("customLogger", it) }
        logger.info("test")

        return
    }
}