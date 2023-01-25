package cz.danielkouba.ktorStarterpackDk

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
        }
        client.get("/health").apply {
            // assertEquals(HttpStatusCode.OK, status)
            println(bodyAsText())
            // assertEquals(mapOf("status" to "up"), this.bodyAsText())
        }
    }
}
