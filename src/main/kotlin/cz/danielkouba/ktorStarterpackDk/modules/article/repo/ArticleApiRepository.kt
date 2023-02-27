package cz.danielkouba.ktorStarterpackDk.modules.article.repo

import cz.danielkouba.ktorStarterpackDk.modules.article.model.Article
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleStatus

/**
 * This is mock repository as well, just example of dependency injection
 */
class ArticleApiRepository(private val apiUrl: String) : ArticleMockRepository(), ArticleRepository {

    override fun populate() {
        clear()
        repeat(10) {
            articles.add(
                Article(
                    id = it.toString(),
                    title = "Article $it",
                    text = "Article $it text",
                    rating = 4.0f,
                    rateCount = 1,
                    status = when (it % 3) {
                        0 -> ArticleStatus.DRAFT
                        1 -> ArticleStatus.PUBLISHED
                        else -> ArticleStatus.HIDDEN
                    }
                )
            )
        }
    }
}
