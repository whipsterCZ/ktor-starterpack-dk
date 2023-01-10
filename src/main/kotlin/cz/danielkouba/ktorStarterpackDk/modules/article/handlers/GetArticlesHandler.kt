package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import ch.qos.logback.classic.Level
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.Articles
import io.ktor.server.application.*
import io.ktor.server.response.*

class GetArticlesHandler(
    service: ArticleService,
    private val articles: Articles
) : BaseArticleHandler(service) {

    override suspend fun handle(call: ApplicationCall) {
        val context = reqContext(call)

        val articleCollection = if (articles.status != null) {
            service.findArticlesByStatus(articles.status, context)
        } else {
            service.findAllArticles(context).items
        }

        call.respond(articleCollection)
    }
}