package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.lib.model.RouteHandler
import cz.danielkouba.ktorStarterpackDk.lib.model.RouteResult
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.Article
import io.ktor.server.application.*

class GetArticleHandler(
    private val service: ArticleService,
    exporter: ArticleExportService,
    private val article: Articles.Article
) : RouteHandler<Article>(exporter) {

    /**
     * Handle the route and return Serializable object or throw exception.
     * Exceptions will be handled automatically by the app exception handler
     *
     * @see [configureErrorHandling]
     */
    override suspend fun handle(call: ApplicationCall): RouteResult<Article> {
        val context = reqContext(call)

        /**
         * get article if exist - throw [NotFoundException] which is handled globally..
         */
        val article = service.findArticleById(article.id, context)

        return RouteResult.WithModel(article)
    }
}
