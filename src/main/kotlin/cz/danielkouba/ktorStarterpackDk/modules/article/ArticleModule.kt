package cz.danielkouba.ktorStarterpackDk.modules.article

import cz.danielkouba.ktorStarterpackDk.configuration.ApplicationModule
import cz.danielkouba.ktorStarterpackDk.modules.article.handlers.*
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleStatus
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.core.component.*
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf


@Serializable
@Resource("articles") //!!be careful to not import `io.ktor.server.routing.*` it will disable (override) typed-routing extension
data class Articles(val status: ArticleStatus? = null) {
    @Serializable
    @Resource("{id}")
    data class Article(val parent: Articles = Articles(), val id: String)
}


class ArticleModule : ApplicationModule() {

    val service: ArticleService by inject()
    private val exporterV1: ArticleExportService by inject { parametersOf("v1") }

    init {
        logger.debug("${this::class.simpleName} init")
    }

    override fun routing() {
        application.routing {
            route("v1") {
                get<Articles> {
                    GetArticlesHandler(service, exporterV1, it).respondTo(call)
                }
                post<Articles> {
                    CreateArticleHandler(service, exporterV1, it).respondTo(call)
                }
                get<Articles.Article> {
                    GetArticleHandler(service, exporterV1, it).respondTo(call)
                }
                put<Articles.Article> {
                    UpdateArticleHandler(service, exporterV1, it).respondTo(call)
                }
                delete<Articles.Article> {
                    DeleteArticleHandler(service, it).respondTo(call)
                }
            }
        }

    }

    override fun dependencyInjectionModules(): List<Module> {
        return listOf(articleDIModule)
    }

    /**
     * On shutdown release resources, close connections, close websockets etc.
     * All exceptions are handled in onShutdown hook itself
     */
    override suspend fun onShutdown() = service.cleanUp()
}