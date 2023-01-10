package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.lib.interfaces.RouteHandler
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.configuration.ReqContext
import cz.danielkouba.ktorStarterpackDk.configuration.ReqContextDeclaration
import cz.danielkouba.ktorStarterpackDk.configuration.reqContext
import io.ktor.server.application.*

abstract class BaseArticleHandler(protected val service: ArticleService) : RouteHandler {
    fun reqContext(call: ApplicationCall, configuration: ReqContextDeclaration? = null): ReqContext {
        return call.reqContext("article") {
            configuration?.invoke(this)
        }
    }
}