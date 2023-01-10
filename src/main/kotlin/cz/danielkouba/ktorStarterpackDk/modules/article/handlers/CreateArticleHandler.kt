package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCreateModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class CreateArticleHandler(
    service: ArticleService,
    private val articles: Articles
) : BaseArticleHandler(service) {
    override suspend fun handle(call: ApplicationCall) {
        val context = reqContext(call)

        // mocked article
        val articleModel = ArticleCreateModel(
            title = "title",
            text = "content",
        )

        call.respond(
            HttpStatusCode.Created,
            service.createArticle(articleModel, context)
        )
    }


}
