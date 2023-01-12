package cz.danielkouba.ktorStarterpackDk.modules.article.repo

import kotlinx.coroutines.delay

/**
 * This is mock repository as well, just example of dependency injection
 */
class ArticleApiRepository(private val apiUrl: String) : ArticleMockRepository(), ArticleRepository