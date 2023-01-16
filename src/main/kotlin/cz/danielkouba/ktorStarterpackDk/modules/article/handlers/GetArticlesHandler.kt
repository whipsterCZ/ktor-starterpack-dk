package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.lib.model.RouteHandler
import cz.danielkouba.ktorStarterpackDk.lib.model.RouteResult
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCollection
import io.ktor.server.application.*

class GetArticlesHandler(
    private val service: ArticleService,
    exporter: ArticleExportService,
    private val articles: Articles
) : RouteHandler<ArticleCollection>(exporter) {

    override suspend fun handle(call: ApplicationCall): RouteResult<ArticleCollection> {
        val context = reqContext(call)

        val articleCollection = articles.status?.let {
            service.findArticlesByStatus(it, context)
        } ?: service.findAllArticles(context)

        return RouteResult.WithModel(articleCollection)
    }
}