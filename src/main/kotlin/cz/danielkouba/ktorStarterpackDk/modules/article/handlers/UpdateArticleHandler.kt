package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleUpdateModel
import io.ktor.server.application.*
import io.ktor.server.response.*

class UpdateArticleHandler(
    service: ArticleService,
    private val article: Articles.Article
) : BaseArticleHandler(service) {

    override suspend fun handle(call: ApplicationCall) {
        val context = reqContext(call)

        // mocked article
        val articleModel = ArticleUpdateModel(
            id = article.id,
            title = "title",
            text = "content",
        )

        call.respond(service.updateArticle(articleModel, context))
    }

}