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

    override suspend fun handle(call: ApplicationCall): ArticleResult<ArticleCollection> {
        val context = reqContext(call)

        return ArticleResult(
            if (articles.status != null) {
                service.findArticlesByStatus(articles.status, context)
            } else {
                service.findAllArticles(context)
            },
        )
    }
}