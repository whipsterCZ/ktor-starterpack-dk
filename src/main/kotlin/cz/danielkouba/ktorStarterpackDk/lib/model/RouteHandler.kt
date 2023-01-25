package cz.danielkouba.ktorStarterpackDk.lib.model

import cz.danielkouba.ktorStarterpackDk.configuration.ReqContextDeclaration
import cz.danielkouba.ktorStarterpackDk.configuration.reqContext
import cz.danielkouba.ktorStarterpackDk.lib.interfaces.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException

sealed class RouteResult<T> {
    public class WithModel<T>(
        val model: T,
        val statusCode: HttpStatusCode = HttpStatusCode.OK
    ) : RouteResult<T>()

    public class OnlyStatus<T>(val statusCode: HttpStatusCode = HttpStatusCode.OK) :
        RouteResult<T>()
}

abstract class RouteHandler<T : ApplicationModel>(
    protected val exporter: ModelExporter? = null,
) : RouteHandlerInterface {

    /**
     * Handle the route and return Serializable object or throw exception.
     * Exceptions will be handled automatically by the app exception handler
     *
     * @see [configureErrorHandling]
     */
    abstract suspend fun handle(call: ApplicationCall): RouteResult<T>

    /**
     * Intermediary method for transforming internal model to external model.
     * Internal model can change in time, but API should have fixed contract
     */
    open suspend fun exportable(model: T): ExportModel? = exporter?.export(model)

    /**
     * Handles the route request and respond the result
     *
     * Exceptions will be handled automatically by the app exception handler
     * @see [Application.configureErrorHandling]
     *
     * @param call ApplicationCall
     */
    override suspend fun respondTo(call: ApplicationCall) = when (val result = handle(call)) {
        is RouteResult.WithModel<T> -> call.respond(
            result.statusCode,
            exportable(result.model) ?: result.model
        )

        is RouteResult.OnlyStatus<T> -> call.respond(result.statusCode)
    }

    fun reqContext(call: ApplicationCall, configuration: ReqContextDeclaration? = null) =
        call.reqContext(Class<T>::javaClass.toString()) {
            configuration?.invoke(this)
        }

    @OptIn(ExperimentalSerializationApi::class)
    suspend inline fun <reified T : ImportModel> importModel(call: ApplicationCall): T {
        val importModel = try {
            call.receive<T>()
        } catch (e: Throwable) {
            // Check missing fields
            e.findCause<MissingFieldException>()?.let {
                val errors = it.missingFields.map { ValidationError.Required(it, "Missing field $it") }
                throw ValidationException(Class<T>::javaClass.toString(), errors)
            }
            throw e
        }

        // validated after receiving, because of throwing ValidationException inside receive() is not possible
        importModel.validate()

        return importModel
    }
}

inline fun <reified T : Any> Throwable.findCause(): T? {
    var cause = this.cause
    while (cause != null) {
        if (cause is T) {
            return cause
        }
        cause = cause.cause
    }
    return null
}
