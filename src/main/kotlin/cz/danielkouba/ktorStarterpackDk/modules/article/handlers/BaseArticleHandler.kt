package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.configuration.ReqContext
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.configuration.ReqContextDeclaration
import cz.danielkouba.ktorStarterpackDk.configuration.reqContext
import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ApplicationModel
import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ExportModel
import cz.danielkouba.ktorStarterpackDk.lib.interfaces.RouteHandlerJson
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleModule
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*


data class ArticleResult<T>(
    val model: ApplicationModel<T>,
    val statusCode: HttpStatusCode = HttpStatusCode.OK,
)

abstract class BaseArticleHandler<T : ApplicationModel<T>>(
    protected val service: ArticleService,
    protected val exporter: ArticleExportService,
) : RouteHandlerJson {

    /**
     * Handle the route and return Serializable object or throw exception.
     * Exceptions will be handled automatically by the app exception handler
     *
     * @see [configureErrorHandling]
     */
    abstract suspend fun handle(call: ApplicationCall): ArticleResult<T>

    /**
     * Intermediary method for transforming internal model to external model.
     * Internal model can change in time, but API should have fixed contract
     */
    open suspend fun transform(data: ArticleResult<T>): ExportModel<T> {
//        return data.model as ExportModel<T>
        @Suppress("UNCHECKED_CAST")
        return exporter.export(data.model) as ExportModel<T>
    }

    /**
     * Handles the route and returns the result
     * @param call ApplicationCall
     */
    override suspend fun respondTo(call: ApplicationCall) {
        val data = handle(call)
        val transformed = transform(data)
        call.respond(data.statusCode, transformed as Any)
    }

    fun reqContext(call: ApplicationCall, configuration: ReqContextDeclaration? = null): ReqContext {
        return call.reqContext(ArticleModule::class.simpleName.toString()) {
            configuration?.invoke(this)
        }
    }
}
