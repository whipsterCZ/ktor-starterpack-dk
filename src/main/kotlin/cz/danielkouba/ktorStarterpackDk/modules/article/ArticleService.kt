package cz.danielkouba.ktorStarterpackDk.modules.article

import cz.danielkouba.ktorStarterpackDk.configuration.ReqContext
import cz.danielkouba.ktorStarterpackDk.modules.article.model.*
import cz.danielkouba.ktorStarterpackDk.modules.article.repo.ArticleRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

final class ArticleService(
    private val articleRepository: ArticleRepository
) {

    suspend fun findAllArticles(context: ReqContext): ArticleCollection {
        val ctx = createContext(context, "findAllArticles")
        try {
            return articleRepository.findAllArticles()
        } catch (e: Exception) {
            ctx.logger.error("Error while finding all article: ${e.message}", e)
            throw e
        }
    }

    suspend fun findArticlesByStatus(status: ArticleStatus, context: ReqContext): ArticleCollection {
        val ctx = createContext(context, "findArticlesByStatus")
        try {
            return articleRepository.findArticlesByStatus(status)
        } catch (e: Exception) {
            ctx.logger.error("Error while finding article by status: $status", e)
            throw e
        }
    }

    suspend fun createArticle(article: ArticleCreateModel, context: ReqContext): ArticleModel {
        val ctx = createContext(context, "createArticle")
        try {
            return articleRepository.createArticle(article)
        } catch (e: Exception) {
            ctx.meta.put("article", Json.encodeToString(article))
            ctx.logger.error("Error while creating article: $article", e)
            throw e
        }
    }

    suspend fun findArticleById(id: String, context: ReqContext): ArticleModel {
        val ctx = createContext(context, "findArticleById")
        ctx.meta.put("articleId", id)
        ctx.logger.info("Finding article by id: $id")
        try {
            return articleRepository.findArticleById(id)
        } catch (e: Exception) {
            ctx.logger.error("Error finding article by id: $id", e)
            throw e
        }
    }

    suspend fun updateArticle(article: ArticleUpdateModel, context: ReqContext): ArticleModel {
        val ctx = createContext(context, "updateArticle")
        try {
            return articleRepository.updateArticle(article)
        } catch (e: Exception) {
            ctx.meta.put("article", Json.encodeToString(article))
            ctx.logger.error("Error updating article: $article", e)
            throw e
        }
    }

    suspend fun deleteArticle(id: String, context: ReqContext) {
        val ctx = createContext(context, "deleteArticle")
        try {
            articleRepository.deleteArticle(id)
        } catch (e: Exception) {
            ctx.meta.put("articleId", id)
            ctx.logger.error("Error deleting article by id: $id", e)
        }
    }

    /**
     * It is required to create a new context for each method to
     *  - separate context from its parent context & prevent its mutation
     *  - add method name to the context
     *  - create custom meta scope
     */
    fun createContext(context: ReqContext, method: String? = null): ReqContext {
        val ctx = context.createChild(this::class.simpleName.toString())
        method?.let { context.method(it) }
        return ctx
    }

    /**
     * On shutdown release resources, close connections, close websockets etc.
     * All exceptions are handled in onShutdown hook itself
     */
    suspend fun cleanUp() {
        articleRepository.cleanUp()
    }

}