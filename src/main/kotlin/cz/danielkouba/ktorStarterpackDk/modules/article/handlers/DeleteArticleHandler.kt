package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class DeleteArticleHandler(
    service: ArticleService,
    private val article: Articles.Article
) : BaseArticleHandler(service) {

    override suspend fun handle(call: ApplicationCall) {
        val context = reqContext(call)

        service.deleteArticle(article.id, context)
        call.respond(HttpStatusCode.OK)
    }

}