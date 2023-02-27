package cz.danielkouba.ktorStarterpackDk

import cz.danielkouba.ktorStarterpackDk.configuration.Config
import cz.danielkouba.ktorStarterpackDk.modules.app.APP_NAME
import cz.danielkouba.ktorStarterpackDk.modules.app.APP_VERSION
import cz.danielkouba.ktorStarterpackDk.modules.app.ApiInfo
import cz.danielkouba.ktorStarterpackDk.modules.app.config.ConfigEnvironment
import cz.danielkouba.ktorStarterpackDk.modules.app.config.ConfigFactoryFromEnv
import cz.danielkouba.ktorStarterpackDk.modules.app.config.applicationConfigFactory
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.skyscreamer.jsonassert.JSONAssert
import kotlin.test.*

class ApplicationTest {

    @BeforeTest()
    fun setup() {
        applicationConfigFactory = ConfigFactoryFromEnv(".env.testing")
    }

    @Test
    fun testRoot() = testApplication {
        application {
            bootstrap()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)

            println(Config)
            val baseUri = "http://localhost:80"
            val apiInfo = ApiInfo(
                app = APP_NAME,
                version = APP_VERSION,
                environment = "UNIT_TEST",
                description = "Kotlin KTOR StarterPack for API development",
                status = "UP",
                api = mapOf(
                    "version" to "v1",
                    "root_url" to "$baseUri/v1",
                    "metrics_url" to "$baseUri/metrics",
                    "health_url" to "$baseUri/health",
                    "swaggerUi_url" to "$baseUri/swagger",
                    "swaggerFile_url" to "$baseUri/swagger/swagger.yml",
                    "articles_url" to "$baseUri/v1/articles",
                )
            )
            JSONAssert.assertEquals(Json.encodeToString(apiInfo), bodyAsText(), true)
        }
    }

    @Test
    fun testMetrics() = testApplication {
        application {
            bootstrap()
        }
        client.get("/metrics").apply {
            assertEquals(HttpStatusCode.OK, status)
            val metrics = bodyAsText()
            assert(metrics.length > 800, { "Metrics response is too short" })
            assert(
                metrics.contains("jvm_memory_used_bytes"),
                { "Metrics response is missing 'jvm_memory_used_bytes' metric" }
            )
            assert(metrics.contains("available"), { "Metrics response is missing 'available' metric" })
            assert(metrics.contains("process_cpu_usage"), { "Metrics response is missing 'process_cpu_usage' metric" })
        }
    }
}
