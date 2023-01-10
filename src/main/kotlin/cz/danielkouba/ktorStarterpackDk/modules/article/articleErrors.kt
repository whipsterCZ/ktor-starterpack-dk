package cz.danielkouba.ktorStarterpackDk.modules.article

import io.ktor.server.plugins.*

// Error handling best practices https://climbtheladder.com/10-kotlin-exception-handling-best-practices/

/**
 * @throws NotFoundException
 */
fun articleNotFound(id: String? = "(NULL)"): Nothing = throw NotFoundException("Article $id not found")