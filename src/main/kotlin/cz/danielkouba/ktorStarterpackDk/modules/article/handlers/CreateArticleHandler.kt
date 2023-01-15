package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.model.Article
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCreateImportV1
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

class CreateArticleHandler(
    service: ArticleService,
    exporter: ArticleExportService
) : BaseArticleHandler<Article>(service, exporter) {

    override suspend fun handle(call: ApplicationCall): ArticleRouteResult<Article> {
        val context = reqContext(call)

        // it is validated by the request validation plugin
        val articleImported = call.receive<ArticleCreateImportV1>()

        // create internal application model
        val articleToBeCreated = articleImported.toModel()

        // create article
        val articleModel = service.createArticle(articleToBeCreated, context)

        // return request result
        return ArticleRouteResult.WithModel(articleModel, HttpStatusCode.Created)
    }
}
