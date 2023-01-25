package cz.danielkouba.ktorStarterpackDk.modules.test

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

val start = System.currentTimeMillis()
fun second() = Math.floor(((System.currentTimeMillis() - start) / 1000).toDouble()).toInt()
fun millis() = (System.currentTimeMillis() - start).toInt()
fun log(message: String) {
    println("${millis()} [${Thread.currentThread().name}] $message")
}

suspend fun operation(operation: String, delay: Long = 1000) {
    log("operation $operation started")
    delay(delay)
    log("operation $operation ended")
}

fun main() {
    log("main started")
    asyncOperationsList()

    println()
    log("main ended - took ${millis()}")
}

fun syncOperations() {
    runBlocking {
        operation("A")
        operation("B")
    }
    log("syncOperations ended - this will be printed after all operations finished")
}

fun asyncOperations() {
    runBlocking {
        val a = async { operation("A") }
        val b = async { operation("B") }
        val c = async { operation("C") }
        val d = async { operation("D") }
        listOf(a, b, c, d).awaitAll()
    }
    log("asyncOperations ended - this will be printed after all operations finished")
}

fun asyncOperationsList() {
    val ops = listOf("A", "B", "C", "D")
    runBlocking {
        ops.map { async { operation(it) } }.awaitAll()
    }
    log("asyncOperationsList ended - this will be printed after all operations finished")
}
