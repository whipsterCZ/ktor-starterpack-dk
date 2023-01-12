package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCreateModel
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleModel
import io.ktor.http.*
import io.ktor.server.application.*

class CreateArticleHandler(
    service: ArticleService,
    exporter: ArticleExportService,
    private val articles: Articles
) : BaseArticleHandler<ArticleModel>(service, exporter) {

    override suspend fun handle(call: ApplicationCall): ArticleResult<ArticleModel> {
        val context = reqContext(call)

        // mocked article
        val articleCreateModel = ArticleCreateModel(
            title = "title",
            text = "content",
        )

        val articleModel = service.createArticle(articleCreateModel, context)

        return ArticleResult(
            model = articleModel,
            statusCode = HttpStatusCode.Created,
        )
    }
}
