package cz.danielkouba.ktorStarterpackDk.modules.app

import cz.danielkouba.ktorStarterpackDk.configuration.Config
import cz.danielkouba.ktorStarterpackDk.lib.interfaces.RouteHandlerInterface
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

const val API_VERSION = "v1"

@Serializable
data class ApiInfo(
    val app: String,
    val version: String,
    val environment: String,
    val description: String,
    val status: String,
    val api: ApiInfoUrl = mapOf()
)

typealias ApiInfoUrl = Map<String, String>

class ApiInfoHandler() : RouteHandlerInterface {
    override suspend fun respondTo(call: ApplicationCall) {
        val baseUri =
            "${call.request.origin.scheme}://${call.request.origin.serverHost}:${call.request.origin.serverPort}"

        val apiInfo = ApiInfo(
            app = APP_NAME,
            version = APP_VERSION,
            environment = Config.environment.toString(),
            description = "Kotlin KTOR StarterPack for API development",
            status = "UP",
            api = mapOf(
                "version" to API_VERSION,
                "root_url" to "$baseUri/v1",
                "metrics_url" to "$baseUri/metrics",
                "health_url" to "$baseUri/health",
                "swaggerUi_url" to "$baseUri/swagger",
                "swaggerFile_url" to "$baseUri/swagger/swagger.yml",
                "articles_url" to "$baseUri/v1/articles",
            )
        )

        call.respond(apiInfo)
    }
}
