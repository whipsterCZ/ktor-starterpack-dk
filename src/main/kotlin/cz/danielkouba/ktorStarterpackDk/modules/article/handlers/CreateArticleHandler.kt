package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.lib.model.RouteHandler
import cz.danielkouba.ktorStarterpackDk.lib.model.RouteResult
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.model.Article
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCreateImportV1
import io.ktor.http.*
import io.ktor.server.application.*

class CreateArticleHandler(
    private val service: ArticleService,
    exporter: ArticleExportService
) : RouteHandler<Article>(exporter) {

    override suspend fun handle(call: ApplicationCall): RouteResult<Article> {
        val context = reqContext(call)

        // get the validated ImportModel form request body
        val articleImported = importModel<ArticleCreateImportV1>(call)

        // create internal application model
        val articleToBeCreated = articleImported.toModel()

        // create article
        val articleModel = service.createArticle(articleToBeCreated, context)

        // return request result
        return RouteResult.WithModel(articleModel, HttpStatusCode.Created)
    }
}
