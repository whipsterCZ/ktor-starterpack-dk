package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.lib.model.RouteHandler
import cz.danielkouba.ktorStarterpackDk.lib.model.RouteResult
import cz.danielkouba.ktorStarterpackDk.lib.model.ValidationError
import cz.danielkouba.ktorStarterpackDk.lib.model.Validator
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.Article
import io.ktor.server.application.*

class RateArticleHandler(
    private val service: ArticleService,
    exporter: ArticleExportService,
    private val rate: Articles.Article.Rate
) : RouteHandler<Article>(exporter) {

    override suspend fun handle(call: ApplicationCall): RouteResult<Article> {
        val context = reqContext(call)


        /**
         * Validate input params
         */
        Validator.create { ->
            check(rate.rating in 1F..5F) {
                ValidationError.Bounds("rating", "Rating must be between 1 and 5")
            }
        }.validate()

        /**
         * rate article or throw [NotFoundException] if article doesn't exist.
         * The exception is handled globally.
         */
        val article = service.rateArticle(rate.parent.id, rate.rating, context)

        return RouteResult.WithModel(article)
    }
}
