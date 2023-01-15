package cz.danielkouba.ktorStarterpackDk.modules.article.repo

import cz.danielkouba.ktorStarterpackDk.modules.article.model.*

interface ArticleRepository {
    suspend fun findAllArticles(): ArticleCollection
    suspend fun findArticlesByStatus(status: ArticleStatus): ArticleCollection
    suspend fun createArticle(create: ArticleCreate): Article
    suspend fun findArticleById(id: String): Article
    suspend fun updateArticle(id: String, update: ArticleUpdate): Article
    suspend fun deleteArticle(id: String)

    /**
     * On shutdown release resources, close connections, close websockets etc.
     * All exceptions are handled in onShutdown hook itself
     */
    suspend fun cleanUp()
}
