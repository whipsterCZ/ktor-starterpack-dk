package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.configuration.reqContext
import cz.danielkouba.ktorStarterpackDk.lib.interfaces.RouteHandler
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleModule
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

/**
 * Delete article handler
 *
 * It doesn't inherit from [BaseArticleHandler] because it doesn't return any data
 */
class DeleteArticleHandler(
    val service: ArticleService,
    private val article: Articles.Article
) : RouteHandler {

    /**
     * Handle the route and return Serializable object or throw exception.
     * Exceptions will be handled automatically by the app exception handler
     *
     * @see [configureErrorHandling]
     */
    suspend fun handle(call: ApplicationCall) {
        val context = call.reqContext(ArticleModule::class.simpleName.toString())
        service.deleteArticle(article.id, context)
    }

    override suspend fun respondTo(call: ApplicationCall) {
        handle(call)
        call.respond(HttpStatusCode.OK)
    }

}