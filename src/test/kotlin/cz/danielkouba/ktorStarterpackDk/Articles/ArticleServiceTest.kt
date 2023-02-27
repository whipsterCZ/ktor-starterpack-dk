package cz.danielkouba.ktorStarterpackDk.Articles

import cz.danielkouba.ktorStarterpackDk.configuration.ReqContext
import cz.danielkouba.ktorStarterpackDk.modules.article.ArticleService
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCreate
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleStatus
import cz.danielkouba.ktorStarterpackDk.modules.article.repo.ArticleMockRepository
import cz.danielkouba.ktorStarterpackDk.modules.logger.LoggerService
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class ArticleServiceTest {

    @Test
    fun testRating() {
        val repo = ArticleMockRepository()
        val service = ArticleService(repo)
        val logger = LoggerService.createRootLogger()
        val reqContext = ReqContext("articleServiceTest", logger)

        runBlocking {
            val article = repo.createArticle(
                ArticleCreate(
                    "Test article",
                    "Test article text",
                    ArticleStatus.PUBLISHED
                )
            )

            assert(article.rating == null, { "Article rating should be null" })
            assert(article.rateCount == 0, { "Article rateCount should be 0" })

            val ratedArticle1 = service.rateArticle(article.id, 5.0f, reqContext)
            assert(ratedArticle1.rating == 5.0f, { "Article rating should be 5.0f (initial)" })
            assert(ratedArticle1.rateCount == 1, { "Article rateCount should be 1 (initial)" })

            val ratedArticle2 = service.rateArticle(article.id, 1.0f, reqContext)
            assert(ratedArticle2.rating == 3.0f, { "Article rating should be 3.0f (average)" })
            assert(ratedArticle2.rateCount == 2, { "Article rateCount should be 2 (average)" })
        }
    }
}
