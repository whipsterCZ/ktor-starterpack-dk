package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import io.ktor.server.application.*
import io.ktor.server.response.*

class GetArticleHandler(
    service: ArticleService,
    private val article: Articles.Article
) : BaseArticleHandler(service) {

    override suspend fun handle(call: ApplicationCall) {
        val context = reqContext(call)

        call.respond(service.findArticleById(article.id, context))
    }

}
