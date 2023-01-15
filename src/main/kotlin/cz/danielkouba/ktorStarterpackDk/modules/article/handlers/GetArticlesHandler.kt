package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCollection
import io.ktor.server.application.*

class GetArticlesHandler(
    service: ArticleService,
    exporter: ArticleExportService,
    private val articles: Articles
) : BaseArticleHandler<ArticleCollection>(service, exporter) {

    override suspend fun handle(call: ApplicationCall): ArticleRouteResult<ArticleCollection> {
        val context = reqContext(call)

        val articleCollection = articles.status?.let {
            service.findArticlesByStatus(it, context)
        } ?: service.findAllArticles(context)

        return ArticleRouteResult.WithModel(articleCollection)
    }
}