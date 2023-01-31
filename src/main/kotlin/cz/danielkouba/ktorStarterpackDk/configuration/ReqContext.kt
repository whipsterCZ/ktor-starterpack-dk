package cz.danielkouba.ktorStarterpackDk.configuration

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import cz.danielkouba.ktorStarterpackDk.modules.logger.child
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * provide access to Request Context in routing (call.reqContext)
 * It can't be extension of Route or Routing because it can't be called when router is initialized
 * It has to be called when real request is received
 */
fun ApplicationCall.reqContext(name: String, configuration: ReqContextDeclaration? = null): ReqContext {
    val callContext = ReqContext(name, this)
    configuration?.invoke(callContext)
    return callContext
}

/**
 * this extension is needed to register reqContext extensions
 * it is also example of usage
 */
fun Application.configureReqContext() {
    routing {
        get("/test/req-context") {
            val context = call.reqContext("reqContext") {
                userId("userId")
                logLevel(Level.INFO)
            }
            context.logger.info("testing reqContext | logger INFO")
            context.logger.debug("testing reqContext | logger DEBUG - should not be logged")

            val childContext = context.createChild("child") {
                userId("userId:Child")
                logLevel(Level.DEBUG)
            }
            childContext.logger.info("testing reqContext.child | logger INFO")
            childContext.logger.debug("testing reqContext.child | logger DEBUG - should be logged")

            context.logger.info("testing reqContext #2 | logger INFO")
            context.logger.debug("testing reqContext #2 | logger DEBUG - should not be logged")

            call.respond(context.toString())
        }
    }
}

/**
 * Request Context (Call Context) is a way to pass data between different layers of the application.
 * it contains call specific data such as logger, meta, etc.
 * it prevents to need of making Services for each request. (users has to have separate context)
 * @param scope - name of the context (e.g. "article") Use camelCase
 */
class ReqContext(
    val scope: String,
    val call: ApplicationCall,
    val meta: ReqCallContextMeta = mutableMapOf(),
    val parentContext: ReqContext? = null
) {
    init {
        meta["scope"] = scope
    }

    private val lazyLogger: Logger by lazy {
        if (parentContext?.logger != null) {
            parentContext.logger.child(scope)
        } else {
            // root logger for request context
            val loggerService by call.inject<LoggerService>()
            loggerService.createLogger("reqContext.$scope")
        }
    }
    val logger: Logger
        get() {
            val logger = lazyLogger
            // println("  GET Logger | ${this} | logger: ${logger.name} | loggerContext ${logger.loggerContext}")
            meta.forEach { (key, value) -> logger.loggerContext.putProperty(key, value) }
            return logger
        }

    fun logLevel(level: Level): ReqContext {
        logger.level = level
        return this
    }

    /**
     * create child context with inherited metadata
     * @param scope name of the context (e.g. "article") Use camelCase
     * @param configuration optional configuration of the child context
     */
    fun createChild(scope: String, configuration: ReqContextDeclaration? = null): ReqContext {
        val child = ReqContext(scope, call, meta, this)
        configuration?.invoke(child)
        return child
    }

    // Meta data
    fun userId(userId: String): ReqContext {
        meta["userId"] = userId
        return this
    }

    fun method(methodCalled: String, args: ReqCallContextMeta? = null): ReqContext {
        meta["method"] = methodCalled
        var message = "Method called: $methodCalled"
        args?.let {
            message = args.toMap().toString()
        }
        this.logger.debug(message)
        return this
    }

    fun shopId(shopId: String): ReqContext {
        meta["shopId"] = shopId
        return this
    }
}

typealias ReqCallContextMeta = MutableMap<String, String>
typealias ReqContextDeclaration = ReqContext.() -> Unit
