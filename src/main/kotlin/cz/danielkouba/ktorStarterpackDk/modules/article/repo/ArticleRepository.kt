package cz.danielkouba.ktorStarterpackDk.modules.article.repo

import cz.danielkouba.ktorStarterpackDk.modules.article.model.*

interface ArticleRepository {
    suspend fun findAllArticles(): List<ArticleModel>
    suspend fun findArticlesByStatus(status: ArticleStatus): List<ArticleModel>
    suspend fun createArticle(article: ArticleCreateModel): ArticleModel
    suspend fun findArticleById(id: String): ArticleModel
    suspend fun updateArticle(article: ArticleUpdateModel): ArticleModel
    suspend fun deleteArticle(id: String)
    suspend fun cleanUp()
}
