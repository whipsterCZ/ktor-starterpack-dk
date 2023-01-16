package cz.danielkouba.ktorStarterpackDk.modules.article.handlers

import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.configuration.ReqContextDeclaration
import cz.danielkouba.ktorStarterpackDk.configuration.reqContext
import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ApplicationModel
import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ExportModel
import cz.danielkouba.ktorStarterpackDk.lib.interfaces.RouteHandler
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleExportService
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleModule
import io.ktor.http.*
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.response.*

sealed class ArticleRouteResult<T> {
    public class WithModel<T>(
        val model: T,
        val statusCode: HttpStatusCode = HttpStatusCode.OK
    ) : ArticleRouteResult<T>()

    public class OnlyStatus<T>(val statusCode: HttpStatusCode = HttpStatusCode.OK) :
        ArticleRouteResult<T>()
}

abstract class BaseArticleHandler<T: ApplicationModel>(
    protected val service: ArticleService,
    protected val exporter: ArticleExportService,
) : RouteHandler {

    /**
     * Handle the route and return Serializable object or throw exception.
     * Exceptions will be handled automatically by the app exception handler
     *
     * @see [configureErrorHandling]
     */
    abstract suspend fun handle(call: ApplicationCall): ArticleRouteResult<T>

    /**
     * Intermediary method for transforming internal model to external model.
     * Internal model can change in time, but API should have fixed contract
     */
    open suspend fun exportable(model: T): ExportModel = exporter.export(model)

    /**
     * Handles the route request and respond the result
     *
     * Exceptions will be handled automatically by the app exception handler
     * @see [Application.configureErrorHandling]
     *
     * @param call ApplicationCall
     */
    override suspend fun respondTo(call: ApplicationCall) = when (val result = handle(call)) {
        is ArticleRouteResult.WithModel<T> -> call.respond(result.statusCode, exportable(result.model))
        is ArticleRouteResult.OnlyStatus<T> -> call.respond(result.statusCode)
    }

    fun reqContext(call: ApplicationCall, configuration: ReqContextDeclaration? = null) =
        call.reqContext(ArticleModule::class.simpleName.toString()) {
            configuration?.invoke(this)
        }
}
