package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.configuration.reqContext
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleModule
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.Article
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall

/**
 * Delete article handler
 *
 * It doesn't inherit from [BaseArticleHandler] because it doesn't return any data
 */
class DeleteArticleHandler(
    service: ArticleService,
    exporter: ArticleExportService,
    private val article: Articles.Article
) : BaseArticleHandler<Article>(service, exporter) {

    override suspend fun handle(call: ApplicationCall): ArticleRouteResult<Article> {
        val context = call.reqContext(ArticleModule::class.simpleName.toString())

        // no exceptions are thrown - idempotent operation (always returns 204)
        service.deleteArticle(article.id, context)

        return ArticleRouteResult.OnlyStatus(HttpStatusCode.NoContent)
    }

}