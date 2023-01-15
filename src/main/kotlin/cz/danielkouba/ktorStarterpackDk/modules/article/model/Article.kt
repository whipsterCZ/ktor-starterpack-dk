package cz.danielkouba.ktorStarterpackDk.modules.article.model

import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ApplicationModel
import cz.danielkouba.ktorStarterpackDk.modules.article.validators.ArticleCollectionValidator
import cz.danielkouba.ktorStarterpackDk.modules.article.validators.ArticleCreateValidator
import cz.danielkouba.ktorStarterpackDk.modules.article.validators.ArticleUpdateValidator
import cz.danielkouba.ktorStarterpackDk.modules.article.validators.ArticleValidator
import io.ktor.server.plugins.requestvalidation.*
import java.time.ZonedDateTime

interface ArticleInterface {
    val title: String
    val text: String
    val createdAt: ZonedDateTime
    val rating: Float?
    val rateCount: Int
    val status: ArticleStatus
}

interface ArticleWithIdInterface : ArticleInterface {
    val id: String
}

/**
 * Article models are not using inheritance.
 *  - Inheritance of data class is discouraged  https://stackoverflow.com/questions/26444145/extend-data-class-in-kotlin
 *  - Inheritance makes models more complex and harder to maintain
 *
 *  All models are be immutable
 */

data class Article(
    override val id: String,
    override val title: String,
    override val text: String,
    override val createdAt: ZonedDateTime = ZonedDateTime.now(),
    override val rating: Float? = null,
    override val rateCount: Int = 0,
    override val status: ArticleStatus = ArticleStatus.DRAFT
) : ApplicationModel<Article>, ArticleWithIdInterface {

    init {
        validateAndThrow()
    }

    override fun validate(): ValidationResult = ArticleValidator(this).validate()
}

data class ArticleCollection(
    val items: List<Article>,
    val hits: Int
) : ApplicationModel<ArticleCollection> {
    init {
        validateAndThrow()
    }

    override fun validate() = ArticleCollectionValidator(this).validate()
}

data class ArticleCreate(
    override val title: String,
    override val text: String,
    override val status: ArticleStatus = ArticleStatus.DRAFT,
    override val createdAt: ZonedDateTime = ZonedDateTime.now(),
    override val rating: Float? = null,
    override val rateCount: Int = 0,
) : ApplicationModel<ArticleCreate>, ArticleInterface {
    init {
        validateAndThrow()
    }

    override fun validate(): ValidationResult = ArticleCreateValidator(this).validate()
}


data class ArticleUpdate(
    override val title: String,
    override val text: String,
    override val status: ArticleStatus,
    override val createdAt: ZonedDateTime = ZonedDateTime.now(),
    override val rating: Float? = null,
    override val rateCount: Int = 0,
) : ApplicationModel<ArticleUpdate>, ArticleInterface {

    init {
        validateAndThrow()
    }

    override fun validate(): ValidationResult = ArticleUpdateValidator(this).validate()
}
