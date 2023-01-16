package cz.danielkouba.ktorStarterpackDk

import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import io.ktor.http.cio.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {

        }
        client.get("/health").apply {
//            assertEquals(HttpStatusCode.OK, status)
            println(bodyAsText())
            //assertEquals(mapOf("status" to "up"), this.bodyAsText())
        }
    }
}