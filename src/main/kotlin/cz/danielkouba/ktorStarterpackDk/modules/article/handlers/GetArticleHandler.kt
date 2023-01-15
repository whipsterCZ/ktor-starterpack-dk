package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.Article
import io.ktor.server.application.ApplicationCall

class GetArticleHandler(
    service: ArticleService,
    exporter: ArticleExportService,
    private val article: Articles.Article
) : BaseArticleHandler<Article>(service, exporter) {

    /**
     * Handle the route and return Serializable object or throw exception.
     * Exceptions will be handled automatically by the app exception handler
     *
     * @see [configureErrorHandling]
     */
    override suspend fun handle(call: ApplicationCall): ArticleRouteResult<Article> {
        val context = reqContext(call)

        /**
         * get article if exist - throw [NotFoundException] which is handled globally..
         */
        val article = service.findArticleById(article.id, context)

        return ArticleRouteResult.WithModel(article)
    }
}
