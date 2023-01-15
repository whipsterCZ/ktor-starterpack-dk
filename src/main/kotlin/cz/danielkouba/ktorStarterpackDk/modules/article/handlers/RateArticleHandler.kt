package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.Article
import io.ktor.server.application.ApplicationCall

class RateArticleHandler(
    service: ArticleService,
    exporter: ArticleExportService,
    private val rate: Articles.Article.Rate
) : BaseArticleHandler<Article>(service, exporter) {

    override suspend fun handle(call: ApplicationCall): ArticleRouteResult<Article> {
        val context = reqContext(call)

        /**
         * rate article or throw [NotFoundException] if article doesn't exist.
         * The exception is handled globally.
         */
        val article = service.rateArticle(rate.parent.id, rate.rating, context)

        return ArticleRouteResult.WithModel(article)
    }
}
