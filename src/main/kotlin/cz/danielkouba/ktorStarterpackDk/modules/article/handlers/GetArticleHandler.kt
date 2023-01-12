package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleModel
import io.ktor.server.application.ApplicationCall

class GetArticleHandler(
    service: ArticleService,
    exporter: ArticleExportService,
    private val article: Articles.Article
) : BaseArticleHandler<ArticleModel>(service, exporter) {

    /**
     * Handle the route and return Serializable object or throw exception.
     * Exceptions will be handled automatically by the app exception handler
     *
     * @see [configureErrorHandling]
     */
    override suspend fun handle(call: ApplicationCall): ArticleResult<ArticleModel> {
        val context = reqContext(call)
        return ArticleResult(
            service.findArticleById(article.id, context)
        )
    }
}
