package cz.danielkouba.ktorStarterpackDk.modules.logger

import cz.danielkouba.ktorStarterpackDk.modules.app.config.ConfigModel
import org.koin.dsl.module

fun LoggerProviderDI(config: ConfigModel) = module {
    single {
        LoggerService(
            rootLogger = LoggerService.createRootLogger(),
            environment = config.environment,
            logLevel = config.logLevel,
            logAppender = config.logAppender,
            appName = config.appName,
            appVersion = config.appVersion
        )
    }
}