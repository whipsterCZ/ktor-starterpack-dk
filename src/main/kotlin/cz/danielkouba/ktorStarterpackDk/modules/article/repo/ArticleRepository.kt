package cz.danielkouba.ktorStarterpackDk.modules.article.repo

import cz.danielkouba.ktorStarterpackDk.modules.article.model.*

interface ArticleRepository {
    suspend fun findAllArticles(): ArticleCollection
    suspend fun findArticlesByStatus(status: ArticleStatus): ArticleCollection
    suspend fun createArticle(article: ArticleCreateModel): ArticleModel
    suspend fun findArticleById(id: String): ArticleModel
    suspend fun updateArticle(article: ArticleUpdateModel): ArticleModel
    suspend fun deleteArticle(id: String)

    /**
     * On shutdown release resources, close connections, close websockets etc.
     * All exceptions are handled in onShutdown hook itself
     */
    suspend fun cleanUp()
}
