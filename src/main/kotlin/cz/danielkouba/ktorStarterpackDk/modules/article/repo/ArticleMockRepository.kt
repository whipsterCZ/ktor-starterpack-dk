package cz.danielkouba.ktorStarterpackDk.modules.article.repo

import cz.danielkouba.ktorStarterpackDk.modules.article.articleNotFound
import cz.danielkouba.ktorStarterpackDk.modules.article.model.*

/**
 * This is mock repository, just works with in-memory data
 */
open class ArticleMockRepository : ArticleRepository {
    private val articles = mutableListOf<Article>()

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

    fun populate(count: Int = 10) {
        clear()
        repeat(count) {
            articles.add(
                Article(
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
