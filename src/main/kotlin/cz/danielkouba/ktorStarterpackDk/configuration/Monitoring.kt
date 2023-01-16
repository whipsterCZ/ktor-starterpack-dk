package cz.danielkouba.ktorStarterpackDk.configuration

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import java.time.Duration
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
        metricName = this@configureMonitoring.config().metricsPrefix

        timers { call, throwable ->
            val path = call.request.path()
            val version = parseVersion(path)
            tag("version", version)
        }

        registry.config().meterFilter(
            MeterFilter.renameTag("", "route", "path")
        )

        distributionStatisticConfig = DistributionStatisticConfig.Builder()
            .percentilesHistogram(true)
            .maximumExpectedValue(Duration.ofSeconds(5).toNanos().toDouble())
            .minimumExpectedValue(Duration.ofMillis(20).toNanos().toDouble())
            .serviceLevelObjectives(
                Duration.ofMillis(100).toNanos().toDouble(),
                Duration.ofMillis(500).toNanos().toDouble()
            )
            .build()


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

fun parseVersion(path: String, default: String = "v1"): String {
    val versionString: String = (path.split("/").filter { it.isNotEmpty() }.getOrNull(0) ?: "").lowercase()
    return if ((versionString.getOrNull(0) ?: "").toString() == "v") {
        versionString
    } else {
        default
    }
}
