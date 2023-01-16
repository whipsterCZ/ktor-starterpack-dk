package cz.danielkouba.ktorStarterpackDk.modules.article

import cz.danielkouba.ktorStarterpackDk.lib.model.ApplicationModule
import cz.danielkouba.ktorStarterpackDk.modules.article.handlers.*
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleStatus
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import org.koin.core.component.*
import org.koin.core.parameter.parametersOf


/**
 * Article Resource (allows to type-safe routing)
 *
 * WARNING: be careful to not import `io.ktor.server.routing.*` it will disable (override) typed-routing extension
 * @see [Application.enableResourceRouting]
 */
@Serializable
@Resource("articles")
data class Articles(val status: ArticleStatus? = null) {

    @Serializable
    @Resource("{id}")
    data class Article(val parent: Articles = Articles(), val id: String) {
        @Serializable
        @Resource("rate")
        data class Rate(val parent: Article, val rating: Float)
    }

}

class ArticleModule : ApplicationModule() {

    val service: ArticleService by inject()
    private val exporterV1: ArticleExportService by inject { parametersOf("v1") }

    init {
        logger.debug("$name init")
        enableResourceRouting()
        registerDiProvider(ArticleDIProvider(config))
    }

    override fun registerRouting() = routing {
        route("v1") {
            get<Articles> {
                GetArticlesHandler(service, exporterV1, it).respondTo(call)
            }
            post<Articles> {
                CreateArticleHandler(service, exporterV1).respondTo(call)
            }
            get<Articles.Article> {
                GetArticleHandler(service, exporterV1, it).respondTo(call)
            }
            put<Articles.Article> {
                UpdateArticleHandler(service, exporterV1, it).respondTo(call)
            }
            post<Articles.Article.Rate> {
                RateArticleHandler(service, exporterV1, it).respondTo(call)
            }
            delete<Articles.Article> {
                DeleteArticleHandler(service, exporterV1, it).respondTo(call)
            }
        }
    }


    /**
     * On shutdown release resources, close connections, close websockets etc.
     * All exceptions are handled in onShutdown hook itself
     */
    override suspend fun onShutdown() = service.cleanUp()
}