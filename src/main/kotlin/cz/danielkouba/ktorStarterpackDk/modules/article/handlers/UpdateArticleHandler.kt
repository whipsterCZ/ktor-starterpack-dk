package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleModel
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleUpdateModel
import io.ktor.server.application.*
import io.ktor.server.response.*

class UpdateArticleHandler(
    service: ArticleService,
    exportService: ArticleExportService,
    private val article: Articles.Article
) : BaseArticleHandler<ArticleModel>(service, exportService) {

    override suspend fun handle(call: ApplicationCall): ArticleResult<ArticleModel> {
        val context = reqContext(call)

        // mocked article
        val articleModel = ArticleUpdateModel(
            id = article.id,
            title = "title",
            text = "content",
        )

        return ArticleResult(
            service.updateArticle(articleModel, context)
        )
    }

}