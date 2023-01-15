package cz.danielkouba.ktorStarterpackDk.configuration

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
//import io.ktor.server.configuration.forwardedheaders.ForwardedHeaders
//import io.ktor.server.configuration.forwardedheaders.XForwardedHeaders
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.json.Json

fun Application.configureHTTP() {
    install(DefaultHeaders) {
        // will send this header with each response
        header("X-Engine", "Ktor")
        header("X-App-Name", Config.appName)
        header("X-App-Version", Config.appVersion)
        header("X-Developer", "Ginger Beavers Team")

    }

    install(AutoHeadResponse) // Automatically respond to a HEAD request for every route that has a GET defined

//    install(ForwardedHeaders) // WARNING: for security, do not include this if not behind a reverse proxy
//    install(XForwardedHeaders) // WARNING: for security, do not include this if not behind a reverse proxy

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
//        allowHeader("MyCustomHeader")
        allowHeader(HttpHeaders.ContentType) // SWAGGER requires this
        anyHost() // SWAGGER requires this ( try to avoid this if possible)
    }

    install(Compression) {
        gzip()
        deflate()
    }

    install(ContentNegotiation) {
        json(
            contentType = ContentType.Application.Json,
            json = Json {
                ignoreUnknownKeys = true
                prettyPrint = false
                isLenient = true
            }
        )
    }

    install(RequestValidation) { }
}
