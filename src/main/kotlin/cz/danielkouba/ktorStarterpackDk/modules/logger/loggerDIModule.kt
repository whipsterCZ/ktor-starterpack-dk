package cz.danielkouba.ktorStarterpackDk.modules.logger

import cz.danielkouba.ktorStarterpackDk.configuration.Config
import org.koin.dsl.module

val loggerDIModule = module {
    single {
        LoggerService(
            rootLogger = LoggerService.createRootLogger(),
            environment = Config.environment,
            logLevel = Config.logLevel,
            logAppender = Config.logAppender,
            appName = Config.appName,
            appVersion = Config.appVersion
        )
    }
}