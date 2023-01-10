package cz.danielkouba.ktorStarterpackDk.modules.article.repo

import kotlinx.coroutines.delay

/**
 * This is mock repository as well, just example of dependency injection
 */
class ArticleApiRepository(private val apiUrl: String) : ArticleMockRepository(), ArticleRepository {
    override suspend fun cleanUp() {
        // pretend activity
        println("ArticleApiRepository cleaning 1")
        delay(100)
        println("ArticleApiRepository cleaning 2")
        delay(100)
        println("ArticleApiRepository cleaning 3")
        delay(100)
        println("ArticleApiRepository cleaning end")
//        val pretendCleanupTimeout = 2000L
//        println("ArticleApiRepository - pretend cleanup for $pretendCleanupTimeout ms")
//        delay(pretendCleanupTimeout)
    }
}