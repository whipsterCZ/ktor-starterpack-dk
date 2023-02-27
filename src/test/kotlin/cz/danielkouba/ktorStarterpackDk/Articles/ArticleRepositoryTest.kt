package cz.danielkouba.ktorStarterpackDk.Articles

import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCreate
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleStatus
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleUpdate
import cz.danielkouba.ktorStarterpackDk.modules.article.repo.ArticleApiRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import kotlin.test.*

class ArticleRepositoryTest {

    @Test
    fun testGetArticleFlow() {
        runBlocking {
            // create repository
            val articleRepository = ArticleApiRepository("http://test.articles.com")

            // get all articles from repository
            val articles = articleRepository.findAllArticles()
            assert(articles.hits > 0, { "ArticleCollection.hits should be positive number" })
            assert(
                articles.items.size == articles.hits,
                { "ArticleCollection.items should same asi ArticleCollection.hits" }
            )

            // add new article to repository
            val newArticle = ArticleCreate(
                "New article",
                "New article text",
                ArticleStatus.PUBLISHED
            )
            val createdArticle = articleRepository.createArticle(newArticle)
            assert(createdArticle.id.isNotEmpty(), { "created Article.id should be not empty" })
            assert(createdArticle.status == ArticleStatus.PUBLISHED, { "created Article.status should be PUBLISHED" })
            assert(createdArticle.title == "New article", { "created Article.title should be 'New article'" })
            assert(createdArticle.text == "New article text", { "created Article.text should be 'New article text'" })

            // get article in repository
            val fetchedCreatedArticle = articleRepository.findArticleById(createdArticle.id)
            assert(fetchedCreatedArticle.id.isNotEmpty(), { "fetched Article.id should be not empty" })
            assert(
                fetchedCreatedArticle.status == createdArticle.status,
                { "fetched Article.status should be same as created Article.status" }
            )
            assert(
                fetchedCreatedArticle.text == createdArticle.text,
                { "fetched Article.text should be same as created Article.text" }
            )
            assert(
                fetchedCreatedArticle.title == createdArticle.title,
                { "fetched Article.title should be same as created Article.title" }
            )
            assert(
                fetchedCreatedArticle.createdAt.toString() == createdArticle.createdAt.toString(),
                { "fetched Article.createdAt should be same as created Article.createdAt" }
            )
            val articlesAfterCreate = articleRepository.findAllArticles()
            assert(
                articlesAfterCreate.hits == articles.hits + 1,
                { "ArticleCollection.hits after created should be increased by 1" }
            )

            // update article in repository
            val articleUpdate = ArticleUpdate(
                "Updated article",
                "Updated article text",
                ArticleStatus.HIDDEN,
            )
            val updatedArticle = articleRepository.updateArticle(createdArticle.id, articleUpdate)
            assert(
                updatedArticle.id == createdArticle.id,
                { "updated Article.id should be same as created Article.id" }
            )
            assert(
                updatedArticle.status == ArticleStatus.HIDDEN,
                { "updated Article.status should be updated to HIDDEN" }
            )
            assert(
                updatedArticle.title == "Updated article",
                { "updated Article.title should be 'Updated article'" }
            )
            assert(
                updatedArticle.text == "Updated article text",
                { "updated Article.text should be 'Updated article text'" }
            )
            val fetchedUpdatedArticle = articleRepository.findArticleById(createdArticle.id)
            assert(
                fetchedUpdatedArticle.id == updatedArticle.id,
                { "fetchedUpdatedArticle.id should be same as updatedArticle.id" }
            )
            assert(
                fetchedUpdatedArticle.status == updatedArticle.status,
                { "fetchedUpdatedArticle.status should be same as updatedArticle.status" }
            )
            assert(
                fetchedUpdatedArticle.text == updatedArticle.text,
                { "fetchedUpdatedArticle.text should be same as updatedArticle.text" }
            )
            assert(
                fetchedUpdatedArticle.title == updatedArticle.title,
                { "fetchedUpdatedArticle.title should be same as updatedArticle.title" }
            )
            assert(
                fetchedUpdatedArticle.createdAt.toString() == updatedArticle.createdAt.toString(),
                { "fetchedUpdatedArticle.createdAt should be same as updatedArticle.createdAt" }
            )

            // delete article from repository
            articleRepository.deleteArticle(createdArticle.id)
            try {
                articleRepository.findArticleById(createdArticle.id)
                fail("Article should not be found in repository")
            } catch (e: Exception) {
                // ok
            }

            // get all articles from repository
            val articlesAfterDelete = articleRepository.findAllArticles()
            assert(
                articlesAfterDelete.hits == articlesAfterCreate.hits - 1,
                { "ArticleCollection.hits after delete should be decreased by 1" }
            )
        }
    }
}
