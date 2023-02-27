package cz.danielkouba.ktorStarterpackDk.modules.article.repo

import cz.danielkouba.ktorStarterpackDk.modules.article.articleNotFound
import cz.danielkouba.ktorStarterpackDk.modules.article.model.*

/**
 * This is mock repository, just works with in-memory data
 */
open class ArticleMockRepository : ArticleRepository {
    protected val articles = mutableListOf<Article>()

    init {
        populate()
    }

    override suspend fun findAllArticles(): ArticleCollection {
        return ArticleCollection(articles, articles.size)
    }

    override suspend fun findArticlesByStatus(status: ArticleStatus): ArticleCollection {
        val items = articles.filter { it.status == status }
        return ArticleCollection(
            items = items,
            hits = items.size
        )
    }

    override suspend fun findArticleById(id: String): Article {
        return articles.find { it.id == id } ?: articleNotFound(id)
    }

    override suspend fun createArticle(create: ArticleCreate): Article {
        val randomString = (0..10).map { ('a'..'z').random() }.joinToString("")

        val article = Article(
            id = randomString,
            title = create.title,
            text = create.text,
            status = create.status,
            createdAt = create.createdAt,
            rateCount = create.rateCount,
            rating = create.rating
        )

        articles.add(article)

        return article
    }

    override suspend fun updateArticle(id: String, update: ArticleUpdate): Article {
        val index = articles.indexOfFirst { it.id == id }
        if (index == -1) articleNotFound(id)

        val article = Article(
            id = id,
            title = update.title,
            text = update.text,
            status = update.status,
            createdAt = update.createdAt,
            rateCount = update.rateCount,
            rating = update.rating
        )
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

    open fun populate() {
        clear()

        fun createArticle(id: Int, status: ArticleStatus) = Article(
            id = "article-$id",
            title = "Article $id",
            text = "Article $id text",
            rating = 4.0f,
            rateCount = 1,
            status = status
        )

        // add some articles with different statuses
        articles.add(createArticle(1, ArticleStatus.PUBLISHED))
        articles.add(createArticle(2, ArticleStatus.PUBLISHED))
        articles.add(createArticle(3, ArticleStatus.PUBLISHED))

        articles.add(createArticle(11, ArticleStatus.DRAFT))
        articles.add(createArticle(12, ArticleStatus.DRAFT))
        articles.add(createArticle(13, ArticleStatus.DRAFT))

        articles.add(createArticle(21, ArticleStatus.HIDDEN))
        articles.add(createArticle(22, ArticleStatus.HIDDEN))
        articles.add(createArticle(23, ArticleStatus.HIDDEN))
    }
}
