package cz.danielkouba.ktorStarterpackDk.configuration

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.Gauge
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import java.util.concurrent.atomic.AtomicInteger

fun Application.configureMonitoring() {

    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    val availableGauge = Gauge
        .builder("available") { AtomicInteger(1) }
        .description("Service is up and running")
    val upGauge = Gauge
        .builder("up") { AtomicInteger(1) }
        .description("Service is up")


    install(MicrometerMetrics) {
        registry = appMicrometerRegistry

        availableGauge.register(registry)
        upGauge.register(registry)
    }


    routing {
        get("/health") {
            call.respond(mapOf("status" to "UP"))
        }
        get("/metrics") {
            call.respond(appMicrometerRegistry.scrape())
        }
    }
}
