package cz.danielkouba.ktorStarterpackDk.modules.article.repo

/**
 * This is mock repository as well, just example of dependency injection
 */
class ArticleApiRepository(private val apiUrl: String) : ArticleMockRepository(), ArticleRepository