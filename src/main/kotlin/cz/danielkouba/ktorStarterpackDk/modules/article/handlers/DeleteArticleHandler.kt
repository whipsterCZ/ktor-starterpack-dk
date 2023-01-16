package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.configuration.reqContext
import cz.danielkouba.ktorStarterpackDk.lib.model.RouteHandler
import cz.danielkouba.ktorStarterpackDk.lib.model.RouteResult
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleModule
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.Article
import io.ktor.http.*
import io.ktor.server.application.*

/**
 * Delete article handler
 *
 * It doesn't inherit from [RouteHandler] because it doesn't return any data
 */
class DeleteArticleHandler(
    private  val service: ArticleService,
    exporter: ArticleExportService,
    private val article: Articles.Article
) : RouteHandler<Article>(exporter) {

    override suspend fun handle(call: ApplicationCall): RouteResult<Article> {
        val context = call.reqContext(ArticleModule::class.simpleName.toString())

        // no exceptions are thrown - idempotent operation (always returns 204)
        service.deleteArticle(article.id, context)

        return RouteResult.OnlyStatus(HttpStatusCode.NoContent)
    }

}