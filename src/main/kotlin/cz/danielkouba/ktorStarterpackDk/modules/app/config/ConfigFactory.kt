package cz.danielkouba.ktorStarterpackDk.modules.app.config

var applicationConfigFactory: ConfigFactory = ConfigFactoryFromEnv()
    set(value) {
        field = value
    }

interface ConfigFactory {
    fun create(): ConfigModel
}

fun configEnvMissing(paramName: String): Nothing =
    throw IllegalArgumentException("Config - Environment arg '$paramName' is required but is missing")
