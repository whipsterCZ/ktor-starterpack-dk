package cz.danielkouba.ktorStarterpackDk.modules.article.repo

import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleModel
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleStatus
import cz.danielkouba.ktorStarterpackDk.modules.article.articleNotFound
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCollection
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCreateModel

open class ArticleMockRepository : ArticleRepository {
    private val articles = mutableListOf<ArticleModel>()

    init {
        populate()
    }

    override suspend fun findAllArticles(): ArticleCollection {
        return ArticleCollection(articles, articles.size)
    }

    override suspend fun findArticlesByStatus(status: ArticleStatus): ArticleCollection {
        return ArticleCollection(
            items = articles.filter { it.status == status },
            hits = articles.size
        )
    }

    override suspend fun findArticleById(id: String): ArticleModel {
        return articles.find { it.id == id } ?: articleNotFound(id)
    }

    override suspend fun createArticle(article: ArticleCreateModel): ArticleModel {
        val randomString = (0..10).map { ('a'..'z').random() }.joinToString("")
        val createdArticle = ArticleModel(
            id = randomString,
            title = article.title,
            text = article.text,
            status = ArticleStatus.DRAFT,
        )
        articles.add(createdArticle)
        return createdArticle
    }

    override suspend fun updateArticle(article: ArticleModel): ArticleModel {
        val index = articles.indexOfFirst { it.id == article.id }
        if (index == -1) articleNotFound(article.id)
        articles[index] = article
        return article
    }

    override suspend fun deleteArticle(id: String) {
        if (!articles.removeIf { it.id == id }) articleNotFound(id)
    }

    override suspend fun cleanUp() {
        // do nothing
    }

    fun clear() {
        articles.clear()
    }

    fun populate(count: Int = 10) {
        clear()
        repeat(count) {
            articles.add(
                ArticleModel(
                    id = it.toString(),
                    title = "Article $it",
                    text = "Article $it text",
                    rating = 5.0f,
                    rateCount = 1,
                    status = when (it % 3) {
                        0 -> ArticleStatus.DRAFT
                        1 -> ArticleStatus.PUBLISHED
                        else -> ArticleStatus.DELETED
                    }
                )
            )
        }
    }
}
