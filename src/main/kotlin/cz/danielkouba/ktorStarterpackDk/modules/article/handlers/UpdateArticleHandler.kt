package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.Article
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCreateImportV1
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleUpdateImportV1
import io.ktor.server.application.*
import io.ktor.server.request.*

class UpdateArticleHandler(
    service: ArticleService,
    exportService: ArticleExportService,
    private val article: Articles.Article
) : BaseArticleHandler<Article>(service, exportService) {

    override suspend fun handle(call: ApplicationCall): ArticleRouteResult<Article> {
        val context = reqContext(call)

        // it is validated by the request validation plugin
        val articleImported = call.receive<ArticleUpdateImportV1>()

        // create internal application model
        val articleToBeUpdated = articleImported.toModel()

        /**
         * Updates article or throw [NotFoundException] if article doesn't exist.
         * The exception is handled globally.
         */
        val updatedArticle = service.updateArticle(article.id, articleToBeUpdated, context)

        return ArticleRouteResult.WithModel(updatedArticle)
    }

}