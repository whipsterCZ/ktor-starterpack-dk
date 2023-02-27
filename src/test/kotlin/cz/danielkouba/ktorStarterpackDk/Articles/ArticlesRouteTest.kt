package cz.danielkouba.ktorStarterpackDk.Articles

import cz.danielkouba.ktorStarterpackDk.bootstrap
import cz.danielkouba.ktorStarterpackDk.modules.app.config.ConfigFactoryFromEnv
import cz.danielkouba.ktorStarterpackDk.modules.app.config.applicationConfigFactory
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCollectionExportV1
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleExportV1
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleStatus
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.*

class ArticlesRouteTest {

    @BeforeTest()
    fun setup() {
        applicationConfigFactory = ConfigFactoryFromEnv(".env.testing")
    }

    @Test
    fun testArticleCollection() = testApplication {
        application {
            bootstrap()
        }
        client.get("/v1/articles").apply {
            val articleCollection: ArticleCollectionExportV1 = Json.decodeFromString(bodyAsText())

            assertEquals(HttpStatusCode.OK, status)
            assert(articleCollection.hits == 9, { "ArticleCollection.hits should be 9" })
            assert(
                articleCollection.items.size == articleCollection.hits,
                { "ArticleCollection.items should same asi ArticleCollection.hits" }
            )
        }
    }

    @Test
    fun testArticleCollectionWithStatus() = testApplication {
        application {
            bootstrap()
        }
        client.get("/v1/articles?status=PUBLISHED").apply {
            val articleCollection: ArticleCollectionExportV1 = Json.decodeFromString(bodyAsText())

            assertEquals(HttpStatusCode.OK, status)
            assert(articleCollection.hits == 3, { "ArticleCollection.hits should be 3" })
            assert(
                articleCollection.items.size == articleCollection.hits,
                { "ArticleCollection.items should same asi ArticleCollection.hits" }
            )

            articleCollection.items.forEach {
                assertEquals(it.status, ArticleStatus.PUBLISHED, "Article.status should be PUBLISHED")
            }
        }
    }

    @Test
    fun testGetArticle() = testApplication {
        application {
            bootstrap()
        }
        client.get("/v1/articles/article-1").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun testCrudArticle() = testApplication {
        application {
            bootstrap()
        }
        var articleId = ""
        // Create
        client.post("/v1/articles") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                    {"title":"Test Article","text":"Test Article Content","status":"DRAFT"}
                """.trimIndent()
            )
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            val articleCreated: ArticleExportV1 = Json.decodeFromString(bodyAsText())
            assertEquals(articleCreated.title, "Test Article")
            assertEquals(articleCreated.text, "Test Article Content")
            assertEquals(articleCreated.status, ArticleStatus.DRAFT)
            assert(articleCreated.id.isNotBlank(), { "Article.id should not be blank" })
            articleId = articleCreated.id
        }

        // Get created
        client.get("/v1/articles/$articleId").apply {
            assertEquals(HttpStatusCode.OK, status)
            val articleGet: ArticleExportV1 = Json.decodeFromString(bodyAsText())
            assertEquals(articleGet.title, "Test Article")
            assertEquals(articleGet.text, "Test Article Content")
            assertEquals(articleGet.status, ArticleStatus.DRAFT)
            assertEquals(articleGet.id, articleId)
        }
//
        // Update
        client.put("/v1/articles/$articleId") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                    {"title":"Test Article Updated","text":"Test Article Content Updated","status":"PUBLISHED"}
                """.trimIndent()
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            val articleUpdated: ArticleExportV1 = Json.decodeFromString(bodyAsText())
            assertEquals(articleUpdated.title, "Test Article Updated")
            assertEquals(articleUpdated.text, "Test Article Content Updated")
            assertEquals(articleUpdated.status, ArticleStatus.PUBLISHED)
            assertEquals(articleUpdated.id, articleId)
        }

        // Delete
        client.delete("/v1/articles/$articleId").apply {
            assertEquals(HttpStatusCode.NoContent, status)
        }
        client.get("/v1/articles/$articleId").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }
}
