val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project
val koin_ktor_version: String by project
val prometheus_version: String by project

plugins {
    kotlin("jvm") version "1.8.0"
    id("io.ktor.plugin") version "2.2.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    id("co.uzzu.dotenv.gradle") version "2.0.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}

group = "cz.danielkouba.ktor-starterpack-dk"
version = "0.0.1"
description = " Starter Pack for Kotlin Ktor API"

application {
    mainClass.set("cz.danielkouba.ktorStarterpackDk.ApplicationKt")

    val environment = env.fetchOrNull("ENVIRONMENT") ?: "production"
    println("Environment: $environment")

    val isDevelopment = listOf("development", "dev").contains(environment)
    if (isDevelopment) {
        println("WARNING auto-reloading is enabled - Singletons may will not be working! @see README.md")
        applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
    }
}

repositories {
    mavenCentral()
    maven("https://artifactory.mallgroup.com/artifactory/libs-release")
}

dependencies {
    implementation(kotlin("script-runtime"))
    // KTOR
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auto-head-response:$ktor_version")
    implementation("io.ktor:ktor-server-resources:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-compression:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("io.ktor:ktor-server-default-headers:$ktor_version")
    implementation("io.ktor:ktor-server-forwarded-header:$ktor_version")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")
//    implementation("io.swagger.codegen.v3:swagger-codegen-generators:1.0.36")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.0")

    // KTOR Monitoring
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("io.ktor:ktor-server-call-id:$ktor_version")
    implementation("io.ktor:ktor-server-metrics-micrometer:$ktor_version")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometheus_version")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.slf4j:slf4j-api:2.0.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("net.logstash.logback:logstash-logback-encoder:7.2")

    // DI
    implementation("io.insert-koin:koin-core:$koin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")
    // Koin for JUnit 5
    testImplementation("io.insert-koin:koin-test-junit5:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_ktor_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_ktor_version")

    // M1 Mac hack
    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.77.Final:osx-aarch_64")
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.JSON)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
    }
    filter {
        exclude("**/style-violations.kt")
        exclude("**/generated/**")
    }
    disabledRules.set(setOf("no-wildcard-imports", "no-unused-imports"))
}

/*    ____     __  __        __  __     ______   ______     ______
    /\  __-.  /\ \/ /       /\ \/ /    /\__  _\ /\  __ \   /\  == \
    \ \ \/\ \ \ \  _"-.     \ \  _"-.  \/_/\ \/ \ \ \/\ \  \ \  __<
     \ \____-  \ \_\ \_\     \ \_\ \_\    \ \_\  \ \_____\  \ \_\ \_\
      \/____/   \/_/\/_/      \/_/\/_/     \/_/   \/_____/   \/_/ /_/
       GINGER    BEAVERS       STARTER-PACK FOR KOTLIN KTOR SERVERS */
