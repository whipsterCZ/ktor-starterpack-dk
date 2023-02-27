package cz.danielkouba.ktorStarterpackDk.configuration

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import cz.danielkouba.ktorStarterpackDk.modules.logger.child
import cz.danielkouba.ktorStarterpackDk.modules.test.log
import io.ktor.server.application.*
import net.logstash.logback.argument.StructuredArguments.kv
import org.koin.ktor.ext.inject

/**
 * provide access to Request Context in routing (call.reqContext)
 * It can't be extension of Route or Routing because it can't be called when router is initialized
 * It has to be called when real request is received
 *
 * @see [TestModule] and [ArticleService] for example how to use it
 */
fun ApplicationCall.reqContext(name: String, configuration: ReqContextDeclaration? = null): ReqContext {
    val loggerService: LoggerService by inject()
    val logger = loggerService.createLogger("reqContext")
    val callContext = ReqContext(name, logger)
    configuration?.invoke(callContext)
    return callContext
}

/**
 * Request Context (Call Context) is a way to pass data between different layers of the application.
 * it contains call specific data such as logger, meta, etc.
 * it prevents to need of making Services for each request. (users has to have separate context)
 * @param scope - name of the context (e.g. "article") Use camelCase
 *
 * @see [TestModule] and [ArticleService] for example how to use it
 */
class ReqContext(
    val scope: String,
    val logger: Logger,
    private val meta: ReqCallContextMeta = mutableMapOf(),
) {

    // Meta data methods

    /**
     * add user id to the context
     * Example of custom metadata - feel free to add more
     */
    fun userId(userId: String): ReqContext {
        meta["userId"] = userId
        return this
    }

    fun addMeta(key: String, value: String): ReqContext {
        meta[key] = value
        return this
    }

    fun getMeta(key: String): String? {
        return meta[key]
    }

    // Logging methods

    fun logMeta(): ReqContext {
        logger.info("Meta", *meta.entries.map { kv(it.key, it.value) }.toTypedArray())
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
        val childLogger = logger.child(scope)
        val childMeta = meta.toMutableMap()
        val child = ReqContext(scope, childLogger, childMeta)
        configuration?.invoke(child)
        return child
    }
}

typealias ReqCallContextMeta = MutableMap<String, String>
typealias ReqContextDeclaration = ReqContext.() -> Unit
