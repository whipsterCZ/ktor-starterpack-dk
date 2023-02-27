package cz.danielkouba.ktorStarterpackDk.Articles

import cz.danielkouba.ktorStarterpackDk.lib.model.ValidationErrorCode
import cz.danielkouba.ktorStarterpackDk.lib.model.ValidationException
import cz.danielkouba.ktorStarterpackDk.modules.article.model.Article
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCollection
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCreate
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleStatus
import java.time.ZonedDateTime
import kotlin.test.*

class ArticlesModelTest {

    @Test
    fun testArticleValidator() {
        try {
            Article(
                "",
                "",
                "",
                ZonedDateTime.now().plusDays(1),
                6f,
                rateCount = -1,
            )
            fail("ValidationException should be thrown")
        } catch (e: Throwable) {
            assert(e is ValidationException, { "ValidationException should be thrown" })

            with(e as ValidationException) {
                assert(
                    e.errors.find { it.field == "id" && it.code == ValidationErrorCode.REQUIRED } != null,
                    { "ValidationException should contain error for id field" }
                )
                assert(
                    e.errors.find { it.field == "title" && it.code == ValidationErrorCode.REQUIRED } != null,
                    { "ValidationException should contain error for title field" }
                )
                assert(
                    e.errors.find { it.field == "text" && it.code == ValidationErrorCode.REQUIRED } != null,
                    { "ValidationException should contain error for text field" }
                )
                assert(
                    e.errors.find { it.field == "rateCount" && it.code == ValidationErrorCode.BOUNDS } != null,
                    { "ValidationException should contain error for rateCount field" }
                )
                assert(
                    e.errors.find { it.field == "rating" && it.code == ValidationErrorCode.BOUNDS } != null,
                    { "ValidationException should contain error for rating field" }
                )
                assert(
                    e.errors.find { it.field == "createdAt" && it.code == ValidationErrorCode.BOUNDS } != null,
                    { "ValidationException should contain error for createdAt field" }
                )
            }
        }
    }

    @Test
    fun testArticleCreateValidator() {
        try {
            ArticleCreate(
                "",
                "",
                ArticleStatus.HIDDEN,
                ZonedDateTime.now().minusDays(1)
            )
            fail("ValidationException should be thrown")
        } catch (e: Throwable) {
            assert(e is ValidationException, { "ValidationException should be thrown" })

            with(e as ValidationException) {
                assert(
                    e.errors.find { it.field == "title" && it.code == ValidationErrorCode.REQUIRED } != null,
                    { "ValidationException should contain error for title field" }
                )
                assert(
                    e.errors.find { it.field == "text" && it.code == ValidationErrorCode.REQUIRED } != null,
                    { "ValidationException should contain error for text field" }
                )
                assert(
                    e.errors.find { it.field == "createdAt" && it.code == ValidationErrorCode.BOUNDS } != null,
                    { "ValidationException should contain error for createdAt field" }
                )
            }
        }
    }

    @Test
    fun testArticlesCollectionValidator() {
        try {
            ArticleCollection(
                listOf(
                    Article(
                        "id",
                        "title",
                        "text"
                    )
                ),
                -1,
            )
            fail("ValidationException should be thrown")
        } catch (e: Throwable) {
            assert(e is ValidationException, { "ValidationException should be thrown" })
            with(e as ValidationException) {
                assert(
                    e.errors.find { it.field == "hits" && it.code == ValidationErrorCode.BOUNDS } != null,
                    { "ValidationException should contain error for hits field" }
                )
            }
        }
    }
}
