package cz.danielkouba.ktorStarterpackDk.modules.article

import cz.danielkouba.ktorStarterpackDk.configuration.ApplicationModule
import cz.danielkouba.ktorStarterpackDk.modules.article.handlers.*
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleStatus
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.resources.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.koin.core.component.inject
import org.koin.core.module.Module

// this alias needed for loosen coupling of current application and module logic
typealias ReqContext = cz.danielkouba.ktorStarterpackDk.configuration.ReqContext

@Serializable
@Resource("articles") //!!be careful to not import `io.ktor.server.routing.*` it will disable (override) typed-routing extension
data class Articles(val status: ArticleStatus? = null) {
    @Serializable
    @Resource("{id}")
    data class Article(val parent: Articles = Articles(), val id: String)
}


class ArticleModule : ApplicationModule() {

    val service: ArticleService by inject()

    init {
        application.log.info("Article module init")
//        onGracefulShutdown {
//            runBlocking {
//                onShutdown()
//            }
//        }
    }

    override fun routing() {
        application.routing {
            route("v1") {
                get<Articles> {
                    GetArticlesHandler(service, it).handle(call)
                }
                post<Articles> {
                    CreateArticleHandler(service, it).handle(call)
                }
                get<Articles.Article> {
                    GetArticleHandler(service, it).handle(call)
                }
                put<Articles.Article> {
                    UpdateArticleHandler(service, it).handle(call)
                }
                delete<Articles.Article> {
                    DeleteArticleHandler(service, it).handle(call)
                }
            }
        }

    }

    override fun dependencyInjectionModules(): List<Module> {
        return listOf(articleDIModule)
    }

    override suspend fun onShutdown() {
//         release resources, close connections, close websockets etc.
        println("ArticleModule onShutdown")
        service.cleanUp(logger)
        println("ArticleModule onShutdown end")
    }

}

