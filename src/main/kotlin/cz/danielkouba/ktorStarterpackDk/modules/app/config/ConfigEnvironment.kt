package cz.danielkouba.ktorStarterpackDk.modules.app.config

enum class ConfigEnvironment {
    PRODUCTION, DEVELOPMENT, TEST, UNIT_TEST;

    companion object {
        fun from(value: String): ConfigEnvironment = when (value.lowercase()) {
            "dev", "development" -> DEVELOPMENT
            "prod", "prd", "production" -> PRODUCTION
            "test", "tst" -> TEST
            "testing", "unitTest" -> UNIT_TEST
            else -> throw IllegalArgumentException("Unknown environment: $value")
        }
    }

    fun isProduction() = this == ConfigEnvironment.PRODUCTION
    fun isDevelopment() = this == ConfigEnvironment.DEVELOPMENT
    fun isTest() = this == ConfigEnvironment.TEST
    fun isUnitTest() = this == ConfigEnvironment.UNIT_TEST
}
